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

public class NfvReaderReal implements NfvReader {
	
	private final NFVSystemType nfvSystem;
	
	private HashMap<String, HostReader>    dbHosts; // map of host interfaces <host.name, HostReader>
	private HashMap<String, NffgReader>    dbNFFGs; // map of NFFG interfaces <nffg.name, NffgReader>
	private HashMap<String, NodeReader>    dbNodes; // map of node interfaces <node.name, NodeReader>
	private HashMap<String, VNFTypeReader> dbVNFs;  // map of VNF  interfaces <vnf.name , VnfReader >
	private HashMap<String, ConnectionPerformanceReader> dbConns; // Connections interfaces <connection.name, ConnectionPerformanceReader>
	private HashMap<String, HashMap<String, LinkReader>> dbLinks; // Links interfaces <nffg.name, <link.name, LinkReader>>
	

	protected NfvReaderReal() throws Exception {
	
		final String contxtPath     = new String( "it.polito.dp2.NFV.sol1.jaxb" );
		final String fileSysPro     = new String( "it.polito.dp2.NFV.sol1.NfvInfo.file" );
		final String schemaLocation = new String( "/xsd/nfvInfo.xsd" );
		
		String schemaFile;
		String xmlFile;
		
		Properties sysProps = System.getProperties();
		
		/*
		 * get Schema to use in the validation process
		 */
		schemaFile = new String( sysProps.getProperty("user.dir") + schemaLocation );
		
		/* ------------------------------------------------------------------
		 * unmarshal xml file 
		 */
		JAXBContext jc  = JAXBContext.newInstance( contxtPath );
		Unmarshaller um = jc.createUnmarshaller();
		
		/* ------------------------------------------------------------------
		 * Set up validation
		 */
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
     
            Schema schema = sf.newSchema(new File( schemaFile ));
            um.setSchema(schema);
            
            um.setEventHandler(
                new ValidationEventHandler() {
                    // allow unmarshalling to continue even if there are errors
                    public boolean handleEvent(ValidationEvent ve) {
                        // ignore warnings
                        if (ve.getSeverity() != ValidationEvent.WARNING) {
                            ValidationEventLocator vel = ve.getLocator();
                            System.out.println("Line:Col[" + vel.getLineNumber() +
                                ":" + vel.getColumnNumber() +
                                "]:" + ve.getMessage());
                        }
                        return true;
                    }
                }
            );
        } catch ( SAXException se ) {
            throw new SAXException(se);
        }
        
		
        /* ------------------------------------------------------------------ 
		 * get XML file to load 
		 */
        sysProps.list(System.out);
		System.out.println("property: " + sysProps.getProperty( fileSysPro ) );
		xmlFile = new String( sysProps.getProperty( fileSysPro ) );
        
        
        /* ------------------------------------------------------------------
		 * unmarshal
		 */
		Object obj = um.unmarshal( new FileInputStream( xmlFile ) );

		@SuppressWarnings("unchecked")
		JAXBElement<NFVSystemType> element = (JAXBElement<NFVSystemType>) obj;
		
		nfvSystem = element.getValue();
		
		dbHosts = new HashMap<String, HostReader>();
		dbNodes = new HashMap<String, NodeReader>();
		dbVNFs  = new HashMap<String, VNFTypeReader>();
		dbConns = new HashMap<String, ConnectionPerformanceReader>();
		dbLinks = new HashMap<String, HashMap<String, LinkReader>>();
		
		init();
		
	}
	
	private void init() throws Exception {
		
		Validator check = new Validator();
		
		InfrastructureNetwork in = nfvSystem.getIN(); 
		
		if ( in == null ) // NFV system didn't have a infrastructure network
			throw new Exception("Invalid Infrastructure Network.");
			
		
		// ------------------------------------------------------------------
		// create hosts readers
		// ------------------------------------------------------------------
		InfrastructureNetwork.Hosts hosts = in.getHosts();
		
		if ( hosts == null ) // the infrastructure network doesn't have any host
			throw new Exception("No hosts in Infrastructure Network.");
		
		List<Host> listHosts = hosts.getHost();
		// TODO: list size?
		if ( listHosts.size() <= 0 ) // the infrastructure network doesn't have any host
			throw new Exception("No hosts in Infrastructure Network.");
		
		for ( Host h : listHosts ) {
			
			if ( !(check.isValidHost( h )) ) 
				continue; // invalid host
			
			Host.AllocatedNodes allocatedNodes = h.getAllocatedNodes();
			
			List<NodeRef> listNodeRefs = allocatedNodes.getNode();
			Set<String> nodes = new HashSet<String>();
			
			for ( NodeRef nr : listNodeRefs ) {
				
				if ( !(check.isValidNodeRef( nr )) )
					continue; // invalid nodeRef
				
				nodes.add( nr.getName() );
			}

			HostReaderReal host = new HostReaderReal(this);
			host.setHost(h);
			host.setNodes(nodes);
			
			// Add HostReader to HostReader database
			dbHosts.put(host.getName(), host);
		}
		
		
		// ------------------------------------------------------------------
		// create connections performance readers
		// ------------------------------------------------------------------
		InfrastructureNetwork.Connections connections = in.getConnections();
		
		if ( connections == null ) // the infrastructure network doesn't have any connection
			throw new Exception("No connections in Infrastructure Network.");
		
		List<Connection> listConnections = connections.getConnection();
		// TODO: list size?
		if ( listConnections.size() <= 0 ) // the infrastructure network doesn't have any connection
			throw new Exception("No connections in Infrastructure Network.");
		
		for ( Connection c : listConnections ) {
			
			if ( !(check.isValidConnection( c )) )
				continue; // invalid connection
			
			ConnectionPerformanceReaderReal connection = new ConnectionPerformanceReaderReal();
			connection.setConnection(c);
			
			String cID = new String( c.getConnectionID().getSourceHost() + "-" +
			                         c.getConnectionID().getDestionationHost() );
			// add connection to connections database
			dbConns.put( cID, connection );
		}
		
		// ------------------------------------------------------------------
		// create VNF type readers
		// ------------------------------------------------------------------
		Catalogue catalogue = nfvSystem.getCatalogue();
		
		if ( catalogue == null )
			throw new Exception("No catalogue found in nfv system.");
		
		List<VNF> listVNFs = catalogue.getVnf();
		// TODO: list size zero?
		if ( listVNFs.size() == 0 )
			throw new Exception("No VNFs found in catalogue.");
		
		for ( VNF v : listVNFs ) {
			
			if ( !(check.isValidVNF( v )) )
				continue; // invalid VNF
			
			VNFTypeReaderReal vnf = new VNFTypeReaderReal();
			vnf.setVNF(v);
			
			// add VNF to VNFs database
			dbVNFs.put(v.getName(), vnf);
		}
		
		// ------------------------------------------------------------------
		// create NFFG readers && nodes readers && links
		// ------------------------------------------------------------------
		NFVSystemType.DeployedNFFGs deployedNFFGs = nfvSystem.getDeployedNFFGs();
		
		if ( deployedNFFGs == null )
			throw new Exception("No deployedNffgs found.");
		
		List<NFFG> listNFFGs = deployedNFFGs.getNffg();
		// TODO: check list size?????
		if ( listNFFGs.size() == 0 )
			throw new Exception("No deployedNffgs found.");
		
		for ( NFFG n : listNFFGs ) {
			
			if ( !(check.isValidNFFG( n )) )
				continue; // invalid NFFG
			
			List<Node> listNodes = n.getNodes().getNode();
			Set<String> setNodes = new HashSet<String>();
			
			// create Links HashMap associated with current NFFG
			HashMap<String, LinkReader> hmLinks = new HashMap<String, LinkReader>();
			

			for ( Node nd : listNodes ) {
				
				if ( !(check.isValidNode( nd )) )
					continue; // invalid node
				
				// ----------------------------------------------------------
				// manage node && links
				// ----------------------------------------------------------
				
				List<Link> listLinks = nd.getLinks().getLink();
				Set<String> setLinks = new HashSet<String>();
				
				for ( Link l : listLinks ) {
					
					if ( !(check.isValidLink( l )) )
						continue; // invalid Link
					
					LinkReaderReal link = new LinkReaderReal(this);
					link.setLink(l);
					
					setLinks.add( l.getName() );    // add link to node
					hmLinks.put(l.getName(), link); // add link to links database for current NFFG
				}
				
				
				NodeReaderReal node = new NodeReaderReal(this);
				node.setNode( nd );
				node.setLinks( setLinks );
				
				setNodes.add( nd.getName() ); // add node to nffg's list of nodes
				dbNodes.put(nd.getName(), node);
				
			}
			
			NffgReaderReal nffg = new NffgReaderReal(this);
			nffg.setNffg( n );
			nffg.setNodes( setNodes );
			
			// add links to links database under current NFFG
			dbLinks.put(n.getName(), hmLinks);

			// add NFFG reader to database
			dbNFFGs.put(n.getName(), nffg);
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
