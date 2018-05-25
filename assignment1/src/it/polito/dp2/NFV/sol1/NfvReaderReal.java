package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.*;

/**
 * This class implements the NvfReader interface.
 * 
 * NOTE: 
 * This class maintains a set of databases with all possible interfaces in 
 * order to improve memory use, i.e. if the library user asks several times 
 * for the same interface the same object will be returned every time.
 * Databases are built when class is created, after the unmarshalling.
 * 
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvReaderReal implements NfvReader {
	
	private static final String JAXB_CLASSES_PACKAGE = "it.polito.dp2.NFV.sol1.jaxb";
	private static final String SCHEMA_LOCATION      = "/xsd/nfvInfo.xsd";

	private static final String PROPERTY_XML_FILE    = "it.polito.dp2.NFV.sol1.NfvInfo.file";
	private static final String PROPERTY_USER_DIR    = "user.dir";
	
	private final NFVSystemType nfvSystem;
	
	private HashMap<String, HostReader>    dbHosts; 			  // all host interfaces <host.name, HostReader>
	private HashMap<String, NffgReader>    dbNFFGs; 			  // all NFFG interfaces <nffg.name, NffgReader>
	private HashMap<String, NodeReader>    dbNodes; 			  // all node interfaces <node.name, NodeReader>
	private HashMap<String, VNFTypeReader> dbVNFs;  			  // all VNF  interfaces <vnf.name , VnfReader >
	private HashMap<String, ConnectionPerformanceReader> dbConns; // Connections interfaces <connection.name, ConnectionPerformanceReader>
	private HashMap<String, HashMap<String, LinkReader>> dbLinks; // Links interfaces <nffg.name, <link.name, LinkReader>>
	

	protected NfvReaderReal() throws Exception {
		
		/* ------------------------------------------------------------------
		 * unmarshal xml file 
		 */
		JAXBContext jc  = JAXBContext.newInstance( JAXB_CLASSES_PACKAGE );
		Unmarshaller um = jc.createUnmarshaller();
		
		/* ------------------------------------------------------------------
		 * Set up validation
		 */
        try {

        	
        	/*
        	 * get Schema to use in the validation process
        	 */
        	
        	String schemaFile = System.getProperty(PROPERTY_USER_DIR);
        	if ( schemaFile == null )
        		throw new NullPointerException("Could not get schema");
        	
        	schemaFile.concat(SCHEMA_LOCATION);
        	
        	SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File( schemaFile ));

            um.setSchema(schema);
            um.setEventHandler(
                new ValidationEventHandler() {
                    // if there are errors, stop unmashalling
                    public boolean handleEvent(ValidationEvent ve) {
                        if (ve.getSeverity() != ValidationEvent.WARNING) {
                            ValidationEventLocator vel = ve.getLocator();
                            System.out.println("Line:Col[" + vel.getLineNumber() +
                                ":" + vel.getColumnNumber() +
                                "]:" + ve.getMessage());
                            return false;
                        }
                        return true;
                    }
                }
            );
        } catch ( Exception e ) {
            System.err.println(e); // no validation
        } 
        
		
        /* ------------------------------------------------------------------ 
		 * get XML file to load 
		 */
		String xmlFile = System.getProperty( PROPERTY_XML_FILE );
		if ( xmlFile == null )
			throw new NullPointerException("Could not get XML file to read.");
        
        
        /* ------------------------------------------------------------------
		 * unmarshal
		 */
		Object obj = um.unmarshal( new FileInputStream( xmlFile ) );

		@SuppressWarnings("unchecked")
		JAXBElement<NFVSystemType> element = (JAXBElement<NFVSystemType>) obj;
		
		nfvSystem = element.getValue();
		
		if ( nfvSystem == null )
			throw new NullPointerException("Null Element.");
		
		// init data structures
		dbHosts = new HashMap<String, HostReader>();
		dbNodes = new HashMap<String, NodeReader>();
		dbNFFGs = new HashMap<String, NffgReader>();
		dbVNFs  = new HashMap<String, VNFTypeReader>();
		dbConns = new HashMap<String, ConnectionPerformanceReader>();
		dbLinks = new HashMap<String, HashMap<String, LinkReader>>();
		
		init();
		
	}
	
	
	private void init() throws Exception {
		
		Validator check = new Validator();
		
		if ( !( check.isValidNFVSystem(nfvSystem) ) )
			throw new Exception("Invalid NFVSystem.");
		
		InfrastructureNetwork in = nfvSystem.getIN(); 
		
		if ( !( check.isValidIN( in ) ) )
			throw new Exception("Invalid Infrastructure Network.");
			
		
		// ------------------------------------------------------------------
		// create hosts readers
		// ------------------------------------------------------------------
		InfrastructureNetwork.Hosts hosts = in.getHosts();
		initHosts(hosts, check);
		
		// ------------------------------------------------------------------
		// create connections performance readers
		// ------------------------------------------------------------------
		InfrastructureNetwork.Connections connections = in.getConnections();
		initConnections(connections, check);
		
		// ------------------------------------------------------------------
		// create VNF type readers
		// ------------------------------------------------------------------
		Catalogue catalogue = nfvSystem.getCatalogue();
		initVNFs( catalogue, check );

		// ------------------------------------------------------------------
		// create NFFG readers && nodes readers && links
		// ------------------------------------------------------------------
		NFVSystemType.DeployedNFFGs deployedNFFGs = nfvSystem.getDeployedNFFGs();
		initNFFGs(deployedNFFGs, check);
	}
	
	
	/**
	 * Creates a set of hostReader interfaces useful to access data 
	 * unmarshalled from the XML file.
	 * 
	 * @param hosts
	 * @param check
	 */
	private void initHosts( InfrastructureNetwork.Hosts hosts, Validator check ) {

		List<Host> liveListXMLHosts = hosts.getHost();

		if ( liveListXMLHosts.size() > 0 ) {

			for ( Host xmlHost : liveListXMLHosts ) {
				
				if ( !( check.isValidHost( xmlHost ) ) ) 
					continue; // invalid host
				
				Host.AllocatedNodes allocatedNodes = xmlHost.getAllocatedNodes();

				List<NodeRef> liveListXMLNodeRefs = allocatedNodes.getNode();
				Set<String> nodes = new HashSet<String>();
				
				if ( liveListXMLNodeRefs.size() > 0 ) {
					for ( NodeRef xmlNodeRef : liveListXMLNodeRefs ) {
						
						if ( !( check.isValidNodeRef( xmlNodeRef ) ) )
							continue; // invalid nodeRef
						
						nodes.add( xmlNodeRef.getName() );
					}
				}
				
				HostReaderReal host = new HostReaderReal(this, xmlHost, nodes);
				// Add HostReader to HostReader database
				dbHosts.put( host.getName(), host );
			}
		}
	}
	
	
	/**
	 * Creates a set of ConnectionPerformanceReader interfaces useful to access data
	 * unmarshalled from the XML file.
	 * 
	 * @param connections
	 * @param check
	 */
	private void initConnections( InfrastructureNetwork.Connections connections, Validator check ) {

		List<Connection> liveListXMLConnections = connections.getConnection();
		
		if ( liveListXMLConnections.size() > 0 ) {
			for ( Connection xmlConnection : liveListXMLConnections ) {
				
				if ( !( check.isValidConnection( xmlConnection ) ) )
					continue; // invalid connection
				
				ConnectionPerformanceReaderReal connection = new ConnectionPerformanceReaderReal( xmlConnection );
				
				String cID = new String( xmlConnection.getConnectionID().getSourceHost() + "-" +
				                         xmlConnection.getConnectionID().getDestinationHost() );
				// add connection to connections database
				dbConns.put( cID, connection );
			}
		}
	}
	
	
	/**
	 * Creates a set of {@link VNFTypeReader} interfaces useful to access data
	 * unmarshalled from the XML file.
	 * 
	 * @param catalogue
	 * @param check
	 */
	private void initVNFs( Catalogue catalogue, Validator check ) {
		
		List<VNF> liveListXMLVNFs = catalogue.getVnf();

		if ( liveListXMLVNFs.size() > 0 ) {
			for ( VNF xmlVNF : liveListXMLVNFs ) {
				
				if ( !( check.isValidVNF( xmlVNF ) ) )
					continue; // invalid VNF
				
				VNFTypeReaderReal vnf = new VNFTypeReaderReal( xmlVNF );
				
				// add VNF to VNFs database
				dbVNFs.put( xmlVNF.getName(), vnf);
			}
		}
	}
	
	
	/**
	 * Creates a set of {@link NffgReader} interfaces useful to access data
	 * unmarshalled from the XML file.
	 * 
	 * @param deployedNFFGs
	 * @param check
	 */
	private void initNFFGs( NFVSystemType.DeployedNFFGs deployedNFFGs, Validator check ) {

		List<NFFG> liveListXMLNFFGs = deployedNFFGs.getNffg();

		if ( liveListXMLNFFGs.size() > 0 ) {
			for ( NFFG xmlNFFG : liveListXMLNFFGs ) {
				
				if ( !( check.isValidNFFG( xmlNFFG ) ) )
					continue; // invalid NFFG
				
				List<Node> liveListXMLNodes = xmlNFFG.getNodes().getNode();
				Set<String> nffgNodeNames = new HashSet<String>();
				
				// create Links HashMap associated with current NFFG
				HashMap<String, LinkReader> hmLinkInterfaces = new HashMap<String, LinkReader>();
				
				// manage nodes if nffg has nodes
				if ( liveListXMLNodes.size() > 0 ) {
					for ( Node xmlNode : liveListXMLNodes ) {
						
						if ( !( check.isValidNode( xmlNode ) ) )
							continue; // invalid node
												
						List<Link> liveListXMLLinks = xmlNode.getLinks().getLink();
						Set<String> setLinks = new HashSet<String>();
						
						// manage links if node has links
						if ( liveListXMLLinks.size() > 0 ) {
							for ( Link xmlLink : liveListXMLLinks ) {
								
								if ( !( check.isValidLink( xmlLink ) ) )
									continue; // invalid Link
								
								setLinks.add( xmlLink.getName() );    // add link to node
								
								LinkReaderReal link = new LinkReaderReal( this, xmlLink );
								hmLinkInterfaces.put( xmlLink.getName(), link ); // add link to links database for current NFFG
							}
						}
						
						
						nffgNodeNames.add( xmlNode.getName() ); // add node to nffg's list of nodes
						
						NodeReaderReal node = new NodeReaderReal(this, xmlNode, setLinks);
						dbNodes.put( xmlNode.getName(), node );
						
					}
				}
				
				NffgReaderReal nffg = new NffgReaderReal( this, xmlNFFG, nffgNodeNames );
				
				// add links to links database under current NFFG
				dbLinks.put(xmlNFFG.getName(), hmLinkInterfaces);				
	
				// add NFFG reader to database
				dbNFFGs.put(xmlNFFG.getName(), nffg);
			}	
		}
	}
	
	
	/**
	 * Retrieves a ConnectionPerformanceReader if one exists between hosts
	 * passed as arguments.
	 * 
	 * @param  sourceHost source Host end point of connection
	 * @param  destHost   destination Host end point of connection
	 * @return            a new ConnectionPerformanceReader interface, null if
	 *                    a connection doesn't exist or an error occurred
	 */
	@Override
	public ConnectionPerformanceReader 
	getConnectionPerformance(HostReader sourceHost, HostReader destHost ) {
		
		if ( sourceHost == null || destHost == null )
			return null; // wrong arguments
		
		String sHost = sourceHost.getName();
		String dHost = destHost.getName();
		
		if ( sHost == null || dHost == null )
			return null; // source or destination hosts didn't have a name
		
		String key = new String( sHost+"-"+dHost );
		ConnectionPerformanceReader cpri = dbConns.get(key);
		
		return cpri; // a connection between sourceHost and destHost doesn't exist
	}

	
	
	/**
	 * Retrieves a HostReader if one exists with the name passed as argument.
	 * 
	 * @param  hostName host name to be retrieved
	 * @return          a HostReader interface or null if host doesn't exist
	 */
	@Override
	public HostReader getHost(String hostName) {
		
		if ( hostName == null )
			return null; // wrong parameter
		
		return dbHosts.get( hostName );
	}

	
	/**
	 * Retrieves the set of all hosts present in the NFV System.
	 * 
	 * @return a set of interfaces to read all hosts in the NFV System
	 */
	@Override
	public Set<HostReader> getHosts() {
		
		return new HashSet<HostReader>( dbHosts.values() );
	}

	
	/**
	 * gives access to a single NFFG given its name.
	 * 
	 * @param nffgName the NFFG name
	 * @return         an interface for reading NFFG's data
	 */
	@Override
	public NffgReader getNffg(String nffgName) {
		
		if ( nffgName == null )
			return null;
		
		return dbNFFGs.get( nffgName );
	}

	
	
	/**
	 * Retrieves a set of interfaces to read all NFFG deployed after the date
	 * passed as argument. If date is null, all NFFGs interfaces are returned.
	 * 
	 * @param  date start of time
	 * @return      
	 */
	@Override
	public Set<NffgReader> getNffgs(Calendar date) {
		
		if ( date == null )
			return new HashSet<NffgReader>( dbNFFGs.values() );
		
		
		Set<NffgReader> setNFFGs = new HashSet<NffgReader>();
		
		for ( String key : dbNFFGs.keySet() ) {
			NffgReader nffg = dbNFFGs.get(key);
			
			if ( nffg.getDeployTime().compareTo(date) >= 0 )
				setNFFGs.add(nffg);
		}
		
		return setNFFGs;
	}

	
	
	/**
	 * Retrieves the VNF Catalogue.
	 * 
	 * @return the set of interfaces for all VNF functions
	 */
	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		return new HashSet<VNFTypeReader>( dbVNFs.values() );
	}

	
	
	/**
	 * Retrieves the interface to access a specific VNF given its name.
	 * 
	 * @param vnfName VNF's name
	 * @return        a interface to access the VNF
	 */
	protected VNFTypeReader getVNF(String vnfName) {
		if ( vnfName == null )
			return null;
		
		return dbVNFs.get(vnfName);
	}
	
	
	/**
	 * Retrieves a set of interfaces to links given their names.
	 * 
	 * @param  nffgName the NFFG's name
	 * @param  links    the links names to be retrieved
	 * @return          a set of interfaces to links
	 */
	protected Set<LinkReader> getLinks( String nffgName, Set<String> links ) {
		
		if ( links == null )
			return new HashSet<LinkReader>();
		
		Set<LinkReader> setLinks = new HashSet<LinkReader>();
		
		HashMap<String, LinkReader> hmLinks = dbLinks.get( nffgName );
		
		for ( String linkName : links )
			setLinks.add( hmLinks.get( linkName ) );
		
		return setLinks;
	}
	
	/**
	 * Returns a interface to a node, given its name.
	 * 
	 * @param  nodeName the node's name
	 * @return          an interface to the node requested
	 */
	protected NodeReader getNode( String nodeName ) {
		
		if ( nodeName == null )
			return null;
		
		return dbNodes.get(nodeName);
	}
	
	/**
	 * Retrieves a set of interfaces to nodes given their names.
	 * 
	 * @param  nodes nodes name to retrieve interfaces to
	 * @return       a set of interfaces to nodes
	 */
	protected Set<NodeReader> getNodes( Set<String> nodes ) {
		
		if ( nodes == null )
			return new HashSet<NodeReader>();
		
		Set<NodeReader> setNodes = new HashSet<NodeReader>();
		
		for ( String nodeName : nodes )
			setNodes.add( dbNodes.get( nodeName ) );
		
		return setNodes;

	}
	
}
