package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
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

/**
 * This class serves as an adapter between the NFV System interfaces and
 * the data read from the XML file.
 * <p>
 * Since the unmarshalling may be done without validation against the schema
 * (and even with the schema it will only report errors but won't stop
 * unmarshalling) this class also performs validity checks using the
 * {@link Validator} class to check the presence of compulsory data objects
 * and performing more complex logical checks itself after the data has
 * been loaded into memory.
 * <p>
 * <b> If any validation or data error arises, the library will return an
 * empty NFV System through the NFV System interfaces. </b>
 * <p>
 * This class maintains a set of databases with all possible interfaces in
 * order to improve memory use, i.e. if the library user asks several times
 * for the same interface the same object will be returned every time.
 * Databases are built when class instance is created, after the unmarshalling.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class Adapter {

	private final NFVSystemType nfvSystem;
	private final Validator     validator;

	private HashMap<String, HostReader>    dbHosts; 			  // all host interfaces <host.name, HostReader>
	private HashMap<String, NffgReader>    dbNFFGs; 			  // all NFFG interfaces <nffg.name, NffgReader>
	private HashMap<String, NodeReader>    dbNodes; 			  // all node interfaces <node.name, NodeReader>
	private HashMap<String, VNFTypeReader> dbVNFs;  			  // all VNF  interfaces <vnf.name , VnfReader >

	private HashMap<String, ConnectionPerformanceReader> dbConns; // Connections interfaces <connection.name, ConnectionPerformanceReader>
	private HashMap<String, HashMap<String, LinkReader>> dbLinks; // Links interfaces <nffg.name, <link.name, LinkReader>>


	/**
	 * Default constructor: creates an empty NFV System.
	 */
	protected Adapter() {
		this.nfvSystem = null;
		this.validator = null;
	}

	/**
	 * Constructor with parameters: prepares the NFV System to be accessed
	 * through the NFV System Interfaces.
	 *
	 * @param  nfvSystem the unmarshalled {@link NFVSystemType}
	 * @throws NfvReaderException
	 */
	protected Adapter( NFVSystemType nfvSystem )
			throws NfvReaderException {

		validator = new Validator();

		if ( !( validator.isValidNFVSystem( nfvSystem ) ) )
			throw new NfvReaderException( "Adapter: Invalid NFV System" );

		this.nfvSystem = nfvSystem;

		// init data structures
		dbHosts = new HashMap<String, HostReader>();
		dbNFFGs = new HashMap<String, NffgReader>();
		dbNodes = new HashMap<String, NodeReader>();
		dbVNFs  = new HashMap<String, VNFTypeReader>();
		dbConns = new HashMap<String, ConnectionPerformanceReader>();
		dbLinks = new HashMap<String, HashMap<String, LinkReader>>();

		init();
	}



	/* ----------------------------------------------------------------------
	 *  DATA ACCESS
	 ----------------------------------------------------------------------*/
	/**
	 * Gives access to a {@link ConnectionPerformanceReader} interface given
	 * its ID "sourceHostName-dstHostName"
	 *
	 * @param  cID the connection ID
	 * @return     a {@link ConnectionPerformanceReader} interface, null if
	 *             connection doesn't exists or the NFVSystem is empty
  	 */
	protected ConnectionPerformanceReader getConnectionPerformance( String cID ) {
		if ( ( cID == null ) || ( nfvSystem == null ) )
			return null;


		return dbConns.get( cID );
	}

	/**
	 * Gives access to a {@link HostReader} interface given the Host name.
	 *
	 * @param hostName the host name
	 * @return         a {@link HostReader}, null if Host doesn't exist or
	 *                 the NFV System is empty
	 */
	protected HostReader getHost( String hostName ) {
		if ( ( hostName == null ) || ( nfvSystem == null ) )
			return null;

		return dbHosts.get( hostName );
	}

	/**
	 * Gives access to all {@link HostReader} interfaces in the NFV System.
	 *
	 * @return the set of {@link HostReader} interfaces in the NFV System,
	 *         the set may be empty.
	 */
	protected Set<HostReader> getHosts() {
		if ( nfvSystem == null )
			return new HashSet<HostReader>();

		return new HashSet<HostReader>( dbHosts.values() );
	}


	/**
	 * Gives access to a {@link NffgReader} interface given the NFFG name.
	 *
	 * @param nffgName the NFFG name
	 * @return         a {@link NffgReader} interface, null if the NFFG
	 *                 doesn't exist of the NFV System is empty
	 */
	protected NffgReader getNFFG( String nffgName ) {
		if ( ( nffgName == null ) || ( nfvSystem == null ) )
			return null;

		return  dbNFFGs.get( nffgName );
	}


	/**
	 * Gives access to all {@link NffgReader} interfaces starting from
	 * {@code date}.
	 *
	 * @param date
	 * @return     a set of {@link NffgReader} interfaces, the set may
	 *             be empty
	 */
	protected Set<NffgReader> getNFFGs( Calendar date ) {
		if ( nfvSystem == null )
			return new HashSet<NffgReader>();

		if ( date == null )
			return new HashSet<NffgReader>( dbNFFGs.values() );

		Set<NffgReader> setNFFGs = new HashSet<NffgReader>();
		for ( String key : dbNFFGs.keySet() ) {
			NffgReader nffg = dbNFFGs.get(key);

			if ( nffg.getDeployTime().compareTo(date) >= 0 ) {
                setNFFGs.add(nffg);
            }
		}

		return setNFFGs;
	}


	/**
	 * Gives access to the VNF Types catalogue.
	 *
	 * @return a set of {@link VNFTypeReader} interfaces,
	 *         the set may be empty if the NFV System is empty
	 */
	protected Set<VNFTypeReader> getVNFCatalog() {
		if ( nfvSystem == null )
			return new HashSet<VNFTypeReader>();

		return new HashSet<VNFTypeReader>( dbVNFs.values() );
	}


	/**
	 * Gives access to a {@link VNFTypeReader} interface given the name
	 * of the VNF.
	 *
	 * @param vnfName
	 * @return        a {@link VNFTypeReader} interface, null if if
	 *                doesn't exist or the NFV System is empty
	 */
	protected VNFTypeReader getVNF( String vnfName ) {
		if ( ( vnfName == null ) || ( nfvSystem == null ) )
			return null;

		return dbVNFs.get( vnfName );
	}


	/**
	 * Gives access to all the {@link LinkReader} interfaces requested
	 * by name from an NFFG.
	 *
	 * @param nffgName the NFFG name
	 * @param links    the names of the
	 * @return         a set of {@link LinkReader} interfaces,
	 *                 the set may be empty
	 */
	protected Set<LinkReader> getLinks( String nffgName, Set<String> links ) {
		if ( ( nffgName == null ) || ( links == null ) || ( nfvSystem == null ) )
			return new HashSet<LinkReader>();

		Set<LinkReader>             setLinks = new HashSet<LinkReader>();
		HashMap<String, LinkReader> hmLinks  = dbLinks.get( nffgName );

		for ( String linkName : links )
			if ( hmLinks.containsKey( linkName ) ) {
                setLinks.add( hmLinks.get( linkName ) );
            }

		return setLinks;
	}


	/**
	 * Gives access to a {@link NodeReader} interface given the Node name.
	 *
	 * @param nodeName
	 * @return         a {@link NodeReader} interface, null if the node
	 *                 doesn't exist or the NFV System is empty
	 */
	protected NodeReader getNode( String nodeName ) {
		if ( ( nodeName == null ) || ( nfvSystem == null ) )
			return null;

		return dbNodes.get(nodeName);
	}

	/**
	 * Gives access to all {@link NodeReader} interfaces requested by Node
	 * names.
	 *
	 * @param  nodes a set with node names
	 * @return       a set with {@link NodeReader} interfaces requested,
	 *               the set may be empty
	 */
	protected Set<NodeReader> getNodes( Set<String> nodes ) {
		if ( ( nodes == null ) || ( nfvSystem == null ) )
			return new HashSet<NodeReader>();

		Set<NodeReader> setNodes = new HashSet<NodeReader>();

		for ( String nodeName : nodes )
			if ( dbNodes.containsKey( nodeName ) ) {
                setNodes.add( dbNodes.get( nodeName ) );
            }

		return setNodes;
	}




	/* ----------------------------------------------------------------------
	 * NFV SYSTEM INTERFACES INITIALIZATION
	 ----------------------------------------------------------------------*/
	/**
	 * Initializes all interfaces needed for reading information about the
	 * DP2-NFV System.
	 *
	 * @param  nfvSystem the JAXB root java object of the XML document
	 * @throws NfvReaderException
	 */
	private void init()
			throws NfvReaderException {

		InfrastructureNetwork in = nfvSystem.getIN();
		if ( !( validator.isValidIN( in ) ) )
			throw new NfvReaderException( "init: invalid Infrastructure Network" );

		// create hostReader interfaces -------------------------------------
		InfrastructureNetwork.Hosts hosts = in.getHosts();
		initHosts( hosts );

		// create ConnectionPerformanceReader interfaces --------------------
		InfrastructureNetwork.Connections connections = in.getConnections();
		initConnections( connections );

		// create VNFTypeReader interfaces ----------------------------------
		Catalogue catalogue = nfvSystem.getCatalogue();
		initVNFs( catalogue );

		// create NffgReader && NodeReader && LinkReader interfaces ---------
		NFVSystemType.DeployedNFFGs deployedNFFGs = nfvSystem.getDeployedNFFGs();
		initNFFGs( deployedNFFGs );

		// last checks ------------------------------------------------------
		checkHosts();
		checkLinks();
	}


	/**
	 * Creates the necessary {@link HostReader} interfaces to access data
	 * information about hosts unmarshalled from the XML file.
	 *
	 * @param hosts JAXB element containing all hosts' data in the XML document
	 */
	private void initHosts( InfrastructureNetwork.Hosts hosts )
			throws NfvReaderException {

		List<Host> liveListXMLHosts = hosts.getHost();

		if ( liveListXMLHosts.size() == 0 )
			return; // no hosts in the NFV System

		for ( Host xmlHost : liveListXMLHosts ) {

			if ( !( validator.isValidHost( xmlHost ) ) )
				throw new NfvReaderException( "initHosts: found invalid Host" );

			Host.AllocatedNodes allocatedNodes      = xmlHost.getAllocatedNodes();
			List<NodeRef>       liveListXMLNodeRefs = allocatedNodes.getNode();
			Set<String>         nodes               = new HashSet<String>();

			if ( liveListXMLNodeRefs.size() > 0 ) {
				for ( NodeRef xmlNodeRef : liveListXMLNodeRefs ) {

					if ( !( validator.isValidNodeRef( xmlNodeRef ) ) )
						throw new NfvReaderException( "initHosts: found invalid NodeRef" );
					try {
						nodes.add( xmlNodeRef.getName() );
					} catch ( Exception e ) {
						throw new NfvReaderException( e.getMessage() );
					}

				}
			}

			if ( nodes.size() > xmlHost.getMaxVNFs() )
				throw new NfvReaderException( "initHosts: allocated nodes exceedes Host's max VNFs" );

			try {
				HostReaderReal host = new HostReaderReal(this, xmlHost, nodes);
				dbHosts.put( host.getName(), host ); // Add HostReader to HostReader database
			} catch ( Exception e ) {
				throw new NfvReaderException( e.getMessage() );
			}

		}
	}


	/**
	 * Creates the necessary {@link ConnectionPerformanceReader} interfaces
	 * to access data about connections between hosts unmarshalled from the XML document.
	 *
	 * @param connections JAXB element containing all connections' data in the XML document
	 */
	private void initConnections( InfrastructureNetwork.Connections connections )
			throws NfvReaderException {

		List<Connection> liveListXMLConnections = connections.getConnection();

		if ( liveListXMLConnections.size() == 0 )
			return; // no connections in NFV System

		for ( Connection xmlConnection : liveListXMLConnections ) {

			if ( !( validator.isValidConnection( xmlConnection ) ) )
				throw new NfvReaderException( "initConnections: found invalid Connection" );
			if ( !( dbHosts.containsKey( xmlConnection.getConnectionID().getSourceHost() ) ) )
				throw new NfvReaderException( "initConnections: Connection source Host doesn't exist" );
			if ( !( dbHosts.containsKey( xmlConnection.getConnectionID().getDestinationHost() ) ) )
				throw new NfvReaderException( "initConnections: Connection destination Host doesn't exist" );

			try {
				ConnectionPerformanceReaderReal connection =
						new ConnectionPerformanceReaderReal( xmlConnection );

				String cID = new String( xmlConnection.getConnectionID().getSourceHost() + "-" +
				     		             xmlConnection.getConnectionID().getDestinationHost() );
				dbConns.put( cID, connection ); // add connection to connections database
			} catch ( Exception e ) {
				throw new NfvReaderException( e.getMessage() );
			}

		}
	}


	/**
	 * Creates the necessary {@link VNFTypeReader} interfaces to access data
	 * about VNFs unmarshalled from the XML document.
	 *
	 * @param catalogue JAXB java object containing all VNFs in the XML document
	 */
	private void initVNFs( Catalogue catalogue )
			throws NfvReaderException {

		List<VNF> liveListXMLVNFs = catalogue.getVnf();

		if ( liveListXMLVNFs.size() == 0 )
			return; // no VNF types in the NFV System

		for ( VNF xmlVNF : liveListXMLVNFs ) {

			if ( !( validator.isValidVNF( xmlVNF ) ) )
				throw new NfvReaderException( "initVNFs: found invalid VNF" );

			try {
				VNFTypeReaderReal vnf = new VNFTypeReaderReal( xmlVNF );
				dbVNFs.put( xmlVNF.getName(), vnf); // add VNF to VNFs database
			} catch ( Exception e ) {
				throw new NfvReaderException( e.getMessage() );
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
	private void initNFFGs( NFVSystemType.DeployedNFFGs deployedNFFGs )
			throws NfvReaderException {

		List<NFFG> liveListXMLNFFGs = deployedNFFGs.getNffg();
		if ( liveListXMLNFFGs.size() == 0 )
			return; // no deployed NFFGs in the NFV System


		for ( NFFG xmlNFFG : liveListXMLNFFGs ) {

			if ( !( validator.isValidNFFG( xmlNFFG ) ) )
				throw new NfvReaderException( "initVNFs: found invalid NFFG" );


			HashMap<String, LinkReader> hmLinkInterfaces = new HashMap<String, LinkReader>(); // Links HashMap for current NFFG
			Set<String>                 nffgNodeNames    = new HashSet<String>();             // node names in current NFFG
			List<Node>                  liveListXMLNodes = xmlNFFG.getNodes().getNode();
			if ( liveListXMLNodes.size() > 0 ) {
				for ( Node xmlNode : liveListXMLNodes ) {

					if ( !( validator.isValidNode( xmlNode ) ) )
						throw new NfvReaderException( "initVNFs: found invalid Node" );


					Set<String> nodeLinkNames    = new HashSet<String>(); // link names (links starting from current node (xmlNode)
					List<Link>  liveListXMLLinks = xmlNode.getLinks().getLink();
					if ( liveListXMLLinks.size() > 0 ) {
						for ( Link xmlLink : liveListXMLLinks ) {

							if ( !( validator.isValidLink( xmlLink ) ) )
								throw new NfvReaderException( "initVNFs: found invalid Link" );

							try {
								nodeLinkNames.add( xmlLink.getName() ); // add link (name) to node's list of links

								LinkReaderReal link = new LinkReaderReal( this, xmlLink );
								hmLinkInterfaces.put( xmlLink.getName(), link ); // add link to links database for current NFFG
							} catch ( Exception e ) {
								throw new NfvReaderException( e.getMessage() );
							}
						}
					}

					try {
						nffgNodeNames.add( xmlNode.getName() ); // add node (name) to nffg's list of nodes

						NodeReaderReal node = new NodeReaderReal( this, xmlNode, nodeLinkNames );
						dbNodes.put( xmlNode.getName(), node ); // add node to nodes database
					} catch ( Exception e ) {
						throw new NfvReaderException( e.getMessage() );
					}
				}
			}


			try {
				NffgReaderReal nffg = new NffgReaderReal( this, xmlNFFG, nffgNodeNames );
				dbNFFGs.put( xmlNFFG.getName(), nffg ); // add NFFG reader to database
				dbLinks.put( xmlNFFG.getName(), hmLinkInterfaces ); // add links to links database under current NFFG
			} catch ( Exception e ) {
				throw new NfvReaderException( e.getMessage() );
			}
		}
	}




	/**
	 * Checks for each host if the maximum amount of memory or
	 * storage have been exceeded.
	 *
	 * @throws NfvReaderException
	 */
	private void checkHosts()
			throws NfvReaderException {

		for ( HostReader hostI : getHosts() ) {

			int availableMemory  = hostI.getAvailableMemory();
			int availableStorage = hostI.getAvailableStorage();

			for ( NodeReader nodeI : hostI.getNodes() ) {

				int requiredMemory  = nodeI.getFuncType().getRequiredMemory();
				int requiredStorage = nodeI.getFuncType().getRequiredStorage();

				availableMemory  -= requiredMemory;
				availableStorage -= requiredStorage;

				if ( ( availableMemory < 0 ) || ( availableStorage < 0 ) )
					throw new NfvReaderException( "checkHosts: Host limits exceeded" );
			}
		}
	}



	/**
	 * Checks if every link is valid, i.e. there exists also a connection
	 * between the Hosts hosting the nodes end points of the link.
	 *
	 * @throws NfvReaderException
	 */
	private void checkLinks()
			throws NfvReaderException {

		for ( NffgReader nffgI : getNFFGs( null ) ) {
			for ( NodeReader nodeI : nffgI.getNodes() ) {
				for ( LinkReader linkI : nodeI.getLinks() ) {

					String srcHostName = linkI.getSourceNode().getHost().getName();
					String dstHostName = linkI.getDestinationNode().getHost().getName();

					String connectionID = new String( srcHostName + "-" + dstHostName );

					if ( !( dbConns.containsKey( connectionID ) ) )
						throw new NfvReaderException( "checkLinks: found invalid Link (no Connection exists)" );
				}
			}
		}
	}

}
