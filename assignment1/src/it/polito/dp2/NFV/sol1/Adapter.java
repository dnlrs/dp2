package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.Catalogue;
import it.polito.dp2.NFV.sol1.jaxb.Connection;
import it.polito.dp2.NFV.sol1.jaxb.Host;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork;
import it.polito.dp2.NFV.sol1.jaxb.Link;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType;
import it.polito.dp2.NFV.sol1.jaxb.Node;
import it.polito.dp2.NFV.sol1.jaxb.NodeRef;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

public class Adapter {
	
	private final NFVSystemType nfvSystem;
	private final Validator     check;
	
	private HashMap<String, HostReader>    dbHosts; 			  // all host interfaces <host.name, HostReader>
	private HashMap<String, NffgReader>    dbNFFGs; 			  // all NFFG interfaces <nffg.name, NffgReader>
	private HashMap<String, NodeReader>    dbNodes; 			  // all node interfaces <node.name, NodeReader>
	private HashMap<String, VNFTypeReader> dbVNFs;  			  // all VNF  interfaces <vnf.name , VnfReader >
	private HashMap<String, ConnectionPerformanceReader> dbConns; // Connections interfaces <connection.name, ConnectionPerformanceReader>
	private HashMap<String, HashMap<String, LinkReader>> dbLinks; // Links interfaces <nffg.name, <link.name, LinkReader>>
	
	
	
	protected Adapter( NFVSystemType nfvSystem ) throws NfvReaderException {
		
		check = new Validator();

		if ( !( check.isValidNFVSystem(nfvSystem) ) )
			throw new NfvReaderException("Invalid NFVSystem.");
		
		this.nfvSystem = nfvSystem;
		
		// init data structures
		dbHosts = new HashMap<String, HostReader>();
		dbNFFGs = new HashMap<String, NffgReader>();
		dbNodes = new HashMap<String, NodeReader>();
		dbVNFs  = new HashMap<String, VNFTypeReader>();
		dbConns = new HashMap<String, ConnectionPerformanceReader>();
		dbLinks = new HashMap<String, HashMap<String, LinkReader>>();
	}
	
	
	/**
	 * Initializes all interfaces needed for reading information about the 
	 * DP2-NFV System.
	 * 
	 * @param  nfvSystem the JAXB root java object of the XML document
	 * @throws NfvReaderException
	 */
	protected void init() throws NfvReaderException {
		
		InfrastructureNetwork in = nfvSystem.getIN(); 
		if ( !( check.isValidIN( in ) ) )
			throw new NfvReaderException("Invalid Infrastructure Network.");
			
		
		// ------------------------------------------------------------------
		// create hostReader interfaces
		// ------------------------------------------------------------------
		InfrastructureNetwork.Hosts hosts = in.getHosts();
		initHosts( hosts );
		
		// ------------------------------------------------------------------
		// create ConnectionPerformanceReader interfaces 
		// ------------------------------------------------------------------
		InfrastructureNetwork.Connections connections = in.getConnections();
		initConnections(connections);
		
		// ------------------------------------------------------------------
		// create VNFTypeReader interfaces
		// ------------------------------------------------------------------
		Catalogue catalogue = nfvSystem.getCatalogue();
		initVNFs( catalogue);

		// ------------------------------------------------------------------
		// create NffgReader && NodeReader && LinkReader interfaces
		// ------------------------------------------------------------------
		NFVSystemType.DeployedNFFGs deployedNFFGs = nfvSystem.getDeployedNFFGs();
		initNFFGs(deployedNFFGs);
	}
	
	
	/**
	 * Creates the necessary {@link HostReader} interfaces to access data 
	 * information about hosts unmarshalled from the XML file.
	 * 
	 * @param hosts JAXB element containing all hosts' data in the XML document
	 */
	private void initHosts( InfrastructureNetwork.Hosts hosts ) {

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
	 * Creates the necessary {@link ConnectionPerformanceReader} interfaces 
	 * to access data about connections between hosts unmarshalled from the XML document.
	 * 
	 * @param connections JAXB element containing all connections' data in the XML document
	 */
	private void initConnections( InfrastructureNetwork.Connections connections ) {

		List<Connection> liveListXMLConnections = connections.getConnection();
		
		if ( liveListXMLConnections.size() > 0 ) {
			for ( Connection xmlConnection : liveListXMLConnections ) {
				
				if ( !( check.isValidConnection( xmlConnection ) ) )
					continue; // invalid connection
				
				ConnectionPerformanceReaderReal connection = 
						new ConnectionPerformanceReaderReal( xmlConnection );
				
				String cID = new String( xmlConnection.getConnectionID().getSourceHost() + "-" +
				                         xmlConnection.getConnectionID().getDestinationHost() );
				// add connection to connections database
				dbConns.put( cID, connection );
			}
		}
	}
	

	/**
	 * Creates the necessary {@link VNFTypeReader} interfaces to access data
	 * about VNFs unmarshalled from the XML document.
	 * 
	 * @param catalogue JAXB java object containing all VNFs in the XML document
	 */
	private void initVNFs( Catalogue catalogue ) {
		
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
	 * Creates the necessary {@link NffgReader}, {@link NodeReader} and 
	 * {@link LinkReader } interfaces to access data about NFFGs, Nodes 
	 * and Links unmarshalled from the XML document.
	 * 
	 * @param deployedNFFGs JAXB java object containing all NFFGs in the XML document
	 */
	private void initNFFGs( NFVSystemType.DeployedNFFGs deployedNFFGs ) {

		List<NFFG> liveListXMLNFFGs = deployedNFFGs.getNffg();

		if ( liveListXMLNFFGs.size() > 0 ) {
			for ( NFFG xmlNFFG : liveListXMLNFFGs ) {
				
				if ( !( check.isValidNFFG( xmlNFFG ) ) )
					continue; // invalid NFFG
				
				List<Node> liveListXMLNodes = xmlNFFG.getNodes().getNode();
				Set<String> nffgNodeNames = new HashSet<String>();
				
				// create Links HashMap associated with current NFFG
				HashMap<String, LinkReader> hmLinkInterfaces = new HashMap<String, LinkReader>();
				
				// manage nodes if NFFG has nodes
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

	
	protected ConnectionPerformanceReader getConnectionPerformance(String cID) {
		if ( cID == null )
			return null;
		
		return dbConns.get( cID );
	}


	protected HostReader getHost(String hostName) {
		if ( hostName == null )
			return null;
		
		return dbHosts.get( hostName );
	}


	protected Set<HostReader> getHosts() {
		return  new HashSet<HostReader>( dbHosts.values() );
	}


	protected NffgReader getNFFG( String nffgName ) {
		if ( nffgName == null )
			return null;
		
		return  dbNFFGs.get( nffgName );
	}


	protected Set<NffgReader> getNFFGs( Calendar date ) {
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


	protected Set<VNFTypeReader> getVNFCatalog() {
		return new HashSet<VNFTypeReader>( dbVNFs.values() );
	}


	protected VNFTypeReader getVNF(String vnfName) {
		if ( vnfName == null )
			return null;
		
		return dbVNFs.get(vnfName);
	}


	protected Set<LinkReader> getLinks(String nffgName, Set<String> links) {
		if ( ( nffgName == null ) || ( links == null ) )
			return new HashSet<LinkReader>();
		
		Set<LinkReader>             setLinks = new HashSet<LinkReader>();
		HashMap<String, LinkReader> hmLinks  = dbLinks.get( nffgName );
		
		for ( String linkName : links )
			if ( hmLinks.containsKey( linkName ) )
				setLinks.add( hmLinks.get( linkName ) );
		
		return setLinks;
	}


	protected NodeReader getNode(String nodeName) {
		if ( nodeName == null )
			return null;
		
		return dbNodes.get(nodeName);
	}


	protected Set<NodeReader> getNodes(Set<String> nodes) {
		if ( nodes == null )
			return new HashSet<NodeReader>();
		
		Set<NodeReader> setNodes = new HashSet<NodeReader>();
		
		for ( String nodeName : nodes )
			if ( dbNodes.containsKey( nodeName ) )
				setNodes.add( dbNodes.get( nodeName ) );
		
		return setNodes;
	}	
}
