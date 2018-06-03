package it.polito.dp2.NFV.sol1;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.Catalogue;
import it.polito.dp2.NFV.sol1.jaxb.Connection;
import it.polito.dp2.NFV.sol1.jaxb.FunctionalTypeEnum;
import it.polito.dp2.NFV.sol1.jaxb.Host;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork;
import it.polito.dp2.NFV.sol1.jaxb.Latency;
import it.polito.dp2.NFV.sol1.jaxb.Link;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType;
import it.polito.dp2.NFV.sol1.jaxb.Node;
import it.polito.dp2.NFV.sol1.jaxb.NodeRef;
import it.polito.dp2.NFV.sol1.jaxb.ObjectFactory;
import it.polito.dp2.NFV.sol1.jaxb.SizeInMB;
import it.polito.dp2.NFV.sol1.jaxb.Throughput;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

/**
 * This class reads data from {@link it.polito.dp2.NFV} System interfaces and builds
 * JAXB java objects that are marshallable into an XML file.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class Builder  {

	private final static String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*";

	private NfvReader     monitor; // Access to NFV System interfaces
	private ObjectFactory of;      // JAXB object factory


	protected Builder()
			throws NfvReaderException {

		try {

			NfvReaderFactory factory = NfvReaderFactory.newInstance();

			monitor   = factory.newNfvReader();
			of        = new ObjectFactory();

		} catch ( FactoryConfigurationError fce ) {
			throw new NfvReaderException( fce.getMessage() );
		} catch ( Exception e ) {
			throw new NfvReaderException( e.getMessage() );
		}
	}



	/**
	 * Creates a root marshallable {@link JAXBElement} (document root) from an
	 * existing {@link NFVSystemType} object.
	 *
	 * @param  nfvSystem a {@link NFVSystemType} Object
	 * @return           a {@link JAXBElement} object (document root)
	 * @throws NfvReaderException
	 */
	protected JAXBElement<NFVSystemType> getRootElement( NFVSystemType nfvSystem )
			throws NfvReaderException {
		if ( nfvSystem == null )
			throw new NfvReaderException( "getRootElement: null argument" );

		return of.createNFVSystem(nfvSystem);
	}



	/**
	 * Builds a {@link NFVSystemType} object.
	 *
	 * @return a {@link NFVSystemType} object
	 * @throws NfvReaderException
	 */
	protected NFVSystemType buildNFVSystemType()
			throws NfvReaderException {
		InfrastructureNetwork       in            = buildIN();            // build Infrastructure Network
		Catalogue                   catalogue     = buildCatalogue();     // build Catalogue
		NFVSystemType.DeployedNFFGs deployedNFFGs = buildDeployedNFFGs(); // deployed NFFGs

		NFVSystemType nfvSystem = of.createNFVSystemType();
		nfvSystem.setIN( in );                       // add Infrastructure Network to NFVSystem
		nfvSystem.setCatalogue( catalogue );         // add Catalogue to NFVSystem
		nfvSystem.setDeployedNFFGs( deployedNFFGs ); // add deployedNFFGs to NFVSystem

		return nfvSystem;
	}



	/**
	 * Builds an {@link InfrastructureNetwork} object from NFVSystem interfaces.
	 *
	 * @return a {@link InfrastructureNetwork} JAXB java object
	 * @throws NfvReaderException
	 */
	private InfrastructureNetwork buildIN()
			throws NfvReaderException {

		InfrastructureNetwork.Hosts hosts =
				of.createInfrastructureNetworkHosts();

		InfrastructureNetwork.Connections connections =
				of.createInfrastructureNetworkConnections();

		Set<HostReader> setHostInterfaces = monitor.getHosts();

		/* NOTE: if no hosts then no connections */
		if ( !( setHostInterfaces.isEmpty() ) ) {

			// read hosts ---------------------------------------------------
			List<Host> liveListXMLHosts = hosts.getHost();
			for ( HostReader hostInterface : setHostInterfaces ) {
				Host host = buildHost( hostInterface );
				try {
					liveListXMLHosts.add( host );
				} catch ( Exception e ) {
					throw new NfvReaderException( e.getMessage() );
				}
			}

			// read connections between hosts -------------------------------
			List<Connection> liveListXMLConnections = connections.getConnection();
			/* NOTE: there may be also connection between a host and itself */
			for ( HostReader sourceHostInterface : setHostInterfaces ) {
                for ( HostReader destHostInterface : setHostInterfaces ) {
					Connection connection =
							buildConnection( sourceHostInterface, destHostInterface );

					if ( connection != null ) {
						try {
							liveListXMLConnections.add( connection );
						} catch ( Exception e ) {
							throw new NfvReaderException( e.getMessage() );
						}
					}
				}
            }
		}

		// prepare IN -------------------------------------------------------
		InfrastructureNetwork in = of.createInfrastructureNetwork();
		in.setHosts( hosts );
		in.setConnections( connections );

		return in;
	}



	/**
	 * Retrieves all the Catalogue data structure from NFVSystem
	 * interfaces and creates corresponding JAXB java objects to be
	 * marshalled into the XML file
	 *
	 * @return a {@link Catalogue} JAXB java object
	 * @throws NfvReaderException
	 */
	private Catalogue buildCatalogue()
			throws NfvReaderException {

		Catalogue catalogue = of.createCatalogue();

		Set<VNFTypeReader> setVNFsInterfaces = monitor.getVNFCatalog();

		if ( !( setVNFsInterfaces.isEmpty() ) ) {
			List<VNF> liveListVNFs = catalogue.getVnf();

			for ( VNFTypeReader vnfInterface : setVNFsInterfaces ) {
				VNF vnf = buildVNF( vnfInterface );
				try {
					liveListVNFs.add( vnf );
				} catch ( Exception e ) {
					throw new NfvReaderException( e.getMessage() );
				}
			}
		}

		return catalogue;
	}



	/**
	 * Retrieves all the deployed NFFGs from NFVSystem interfaces and
	 * creates corresponding java JAXB java objects to be marshalled into
	 * the XML file.
	 *
	 * @return a {@link NFVSystemType.DeployedNFFGs} JAXB java object
	 * @throws NfvReaderException
	 */
	private NFVSystemType.DeployedNFFGs buildDeployedNFFGs()
			throws NfvReaderException {

		NFVSystemType.DeployedNFFGs deployedNFFGs =
				of.createNFVSystemTypeDeployedNFFGs();

		Set<NffgReader> setNFFGsInterfaces = monitor.getNffgs( null );

		if ( !( setNFFGsInterfaces.isEmpty() ) ) {
			List<NFFG> liveListNFFGs = deployedNFFGs.getNffg();

			for ( NffgReader nffgInterface : setNFFGsInterfaces ) {
				NFFG nffg = buildNFFG( nffgInterface );
				try {
					liveListNFFGs.add( nffg );
				} catch ( Exception e ) {
					throw new NfvReaderException( e.getMessage() );
				}
			}
		}

		return deployedNFFGs;
	}



	/**
	 * Creates a new Host JAXB java object by reading required data from a
	 * NFVSystem {@link HostReader} interface.
	 *
	 * @param  hI a {@link HostReader} interface
	 * @return    a {@link Host} JAXB java object
	 * @throws NfvReaderException
	 */
	private Host buildHost( HostReader hI )
			throws NfvReaderException {

		if ( hI == null )
			throw new NfvReaderException( "buildHost: null argument" );

		if ( ( hI.getName() == null ) || ( hI.getMaxVNFs() < 0 ) )
			throw new NfvReaderException( "buildHost: invalid Host" );

		if ( !( hI.getName().matches(NAME_REGEX) ) )
			throw new NfvReaderException( "buildHost: bad host name" );

		// retrieve Available Memory and Storage ---------------------------
		SizeInMB am = buildSizeInMB( BigInteger.valueOf( hI.getAvailableMemory()  ) );
		SizeInMB as = buildSizeInMB( BigInteger.valueOf( hI.getAvailableStorage() ) );

		// retrieve all nodes allocated in current host ---------------------
		Host.AllocatedNodes allocatedNodes = of.createHostAllocatedNodes();

		Set<NodeReader> setNodesInterfaces = hI.getNodes();

		if ( !( setNodesInterfaces.isEmpty() ) ) {

			int availableMemory  = hI.getAvailableMemory();
			int availableStorage = hI.getAvailableStorage();
			int maxVNFs          = hI.getMaxVNFs();

			List<NodeRef> liveListXMLNodeRefs   = allocatedNodes.getNode();

			for ( NodeReader nodeInterface : setNodesInterfaces ) {

				NffgReader nffgInterface = nodeInterface.getNffg();
				if ( nffgInterface == null )
					throw new NfvReaderException( "buildHost: node without NFFG" );

				if ( ( nodeInterface.getFuncType().getRequiredMemory() < 0 ) ||
					 ( nodeInterface.getFuncType().getRequiredStorage() < 0 ) )
					throw new NfvReaderException( "buildHost: invalid node memory or storage" );

				availableMemory  -= nodeInterface.getFuncType().getRequiredMemory();
				availableStorage -= nodeInterface.getFuncType().getRequiredStorage();
				maxVNFs--;

				if ( ( availableMemory < 0 ) || ( availableStorage < 0 ) || ( maxVNFs < 0 ) )
					throw new NfvReaderException( "buildHost: host capacities exceeded" );


				String nodeName = nodeInterface.getName();
				String nffgName = nffgInterface.getName();

				if ( ( nodeName == null ) || ( nffgName == null ) )
					throw new NfvReaderException( "buildHost: null node/NFFG name " );

				if ( !( nodeName.matches(NAME_REGEX) ) || !( nffgName.matches(NAME_REGEX)  ) )
					throw new NfvReaderException( "buildHost: bad node/NFFG name" );


				NodeRef nodeRef = of.createNodeRef();
				nodeRef.setName( nodeName );
				nodeRef.setAssociatedNFFG( nffgName );

				try {
					liveListXMLNodeRefs.add( nodeRef );
				} catch ( Exception e ) {
					throw new NfvReaderException( e.getMessage() );
				}
			}
		}

		// prepare host data structure --------------------------------------
		Host host = of.createHost();
		host.setName( hI.getName() );
		host.setInstalledMemory( am );
		host.setInstalledStorage( as );
		host.setMaxVNFs( hI.getMaxVNFs() );
		host.setAllocatedNodes( allocatedNodes );

		return host;
	}



	/**
	 * Creates a new {@link SizeInMB} JAXB java object given a BigInteger value.
	 *
	 * @param  value a {@link BigInteger} value
	 * @return       a new {@link SizeInMB} JAXB java object
	 * @throws NfvReaderException
	 */
	private SizeInMB buildSizeInMB(BigInteger value)
			throws NfvReaderException {

		if ( value == null )
			throw new NfvReaderException( "buildSizeInMB: null argument" );

		if ( value.intValue() < 0  )
			throw new NfvReaderException( "buildSizeInMB: invalid value" );

		SizeInMB sim = of.createSizeInMB();
		sim.setValue( value );
		sim.setUnit( sim.getUnit() );

		return sim;
	}




	/**
	 * Creates a new Connection JAXB java object given two end point hosts.
	 *
	 * @param  sourceHostI the source Host ({@link HostReader} interface)
	 * @param  destHostI   the destination Host ({@link HostReader} interface)
	 * @return             a new Connection JAXB java object, null if no
	 *                     connection exists between specified hosts
	 * @throws NfvReaderException
	 */
	private Connection buildConnection( HostReader sourceHostI, HostReader destHostI )
			throws NfvReaderException {

		if ( ( sourceHostI == null ) || ( destHostI == null ) )
			throw new NfvReaderException( "buildConnection: null argument" );

		ConnectionPerformanceReader connectionInterface =
				monitor.getConnectionPerformance( sourceHostI, destHostI );

		if ( connectionInterface == null )
			return null; // no connection between hosts

		// retrieve connection ID (source and destination host name) --------
		if ( ( sourceHostI.getName() == null ) || ( destHostI.getName() == null ) )
			throw new NfvReaderException( "buildConnection: invalid endpoints" );

		Connection.ConnectionID connectionID = of.createConnectionConnectionID();
		connectionID.setSourceHost( sourceHostI.getName() );
		connectionID.setDestinationHost( destHostI.getName() );

		// retrieve connection Throughput -----------------------------------
		if ( connectionInterface.getThroughput() < 0 )
			throw new NfvReaderException( "buildConnection: invalid throughput" );

		Throughput throughput = of.createThroughput();
		throughput.setValue( connectionInterface.getThroughput() );
		throughput.setUnit( throughput.getUnit() );

		// retrieve connection Latency --------------------------------------
		if ( connectionInterface.getLatency() < 0 )
			throw new NfvReaderException( "buildConnection: invalid latency" );

		Latency latency = of.createLatency();
		latency.setValue( connectionInterface.getLatency() );
		latency.setUnit( latency.getUnit() );

		// prepare connection -----------------------------------------------
		Connection connection = of.createConnection();
		connection.setConnectionID( connectionID );
		connection.setAverageThroughput( throughput );
		connection.setLatency( latency );

		return connection;
	}



	/**
	 * Creates a new VNF JAXB java object by reading required data from a
	 * {@link VNFTypeReader} interface.
	 *
	 * @param  vnfInterface a {@link VNFTypeReader} interface
	 * @return              a new VNF JAXB java object
	 * @throws NfvReaderException
	 */
	private VNF buildVNF( VNFTypeReader vnfInterface )
			throws NfvReaderException {

		if ( vnfInterface == null )
			throw new NfvReaderException( "buildVNF: null argument" );

		if ( vnfInterface.getName() == null )
			throw new NfvReaderException( "buildVNF: invalid VNF name" );

		if ( ( vnfInterface.getRequiredMemory() < 0 ) || ( vnfInterface.getRequiredStorage() < 0 ) )
			throw new NfvReaderException( "builfVNF: invalid VNF requirements" );

		// retrieve required memory and storage -----------------------------
		SizeInMB rm = buildSizeInMB( BigInteger.valueOf( vnfInterface.getRequiredMemory()  ) );
		SizeInMB rs = buildSizeInMB( BigInteger.valueOf( vnfInterface.getRequiredStorage() ) );

		// retrieve functional type string ----------------------------------
		if ( vnfInterface.getFunctionalType() == null )
			throw new NfvReaderException( "buildVNF: invalid functional type" );

		String functionalTypeName = vnfInterface.getFunctionalType().value();

		// prepare VNF ------------------------------------------------------
		VNF vnf = of.createVNF();
		vnf.setName( vnfInterface.getName() );
		vnf.setRequiredMemory( rm );
		vnf.setRequiredStorage( rs );
		vnf.setFunctionalType( FunctionalTypeEnum.fromValue( functionalTypeName ) );

		return vnf;
	}



	/**
	 * Creates a new NFFG JAXB java object by reading required data from a
	 * {@link NffgReader} interface.
	 *
	 * @param  nffgInterface a {@link NffgReader} interface
	 * @return               a new NFFG JAXB java object
	 * @throws NfvReaderException
	 */
	private NFFG buildNFFG( NffgReader nffgInterface )
			throws NfvReaderException {

		if ( nffgInterface == null )
			throw new NfvReaderException( "buildNFFG: null argument" );

		if ( nffgInterface.getName() == null )
			throw new NfvReaderException( "buildNFFG: null NFFG name" );

		if ( !( nffgInterface.getName().matches( NAME_REGEX ) ) )
			throw new NfvReaderException( "buildNFFG: invalid NFFG name" );

		if ( nffgInterface.getDeployTime() == null )
			throw new NfvReaderException( "buildNFFG: invalid NFFG deploy time" );

		// retrieve nffg's nodes --------------------------------------------
		NFFG.Nodes nodes = of.createNFFGNodes();

		Set<NodeReader> setNodeInterfaces = nffgInterface.getNodes();

		if ( !( setNodeInterfaces.isEmpty() ) ) {
			List<Node> liveListXMLNodes = nodes.getNode(); // live list

			for ( NodeReader nodeInterface : setNodeInterfaces ) {
				Node node = buildNode ( nodeInterface );
				try {
					liveListXMLNodes.add( node );
				} catch ( Exception e ) {
					throw new NfvReaderException( e.getMessage() );
				}
			}
		}

		// retrieve nffg's deploy time --------------------------------------
		Calendar c  = nffgInterface.getDeployTime();

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis( c.getTimeInMillis() );

		XMLGregorianCalendar time = null;
		try {
			 time = DatatypeFactory.newInstance().newXMLGregorianCalendar( gc );
		} catch (Exception e) {
			throw new NfvReaderException( e.getMessage() );
		}

		// prepare NFFG -----------------------------------------------------
		NFFG nffg = of.createNFFG();
		nffg.setName( nffgInterface.getName() );
		nffg.setDeployTime( time );
		nffg.setNodes( nodes );

		return nffg;
	}



	/**
	 * Creates a new Node JAXB java object by reading required data form a
	 * {@link NodeReader} interface.
	 *
	 * @param  nodeInterface a {@link NodeReader} interface
	 * @return               a new Node JAXB java object
	 * @throws NfvReaderException
	 */
	private Node buildNode ( NodeReader nodeInterface )
			throws NfvReaderException {

		if ( nodeInterface == null )
			throw new NfvReaderException( "buildNode: null argument" );

		// check if node has all necessary data
		if ( ( nodeInterface.getName()     == null ) ||
			 ( nodeInterface.getHost()     == null ) ||
			 ( nodeInterface.getNffg()     == null ) ||
			 ( nodeInterface.getFuncType() == null ) )
			throw new NfvReaderException( "buildNode: null node data" );

		// check if node name is correct
		if ( !( nodeInterface.getName().matches( NAME_REGEX ) ) )
			throw new NfvReaderException( "buildNode: invalid Node name" );


		if ( ( monitor.getHost( nodeInterface.getHost().getName() ) == null ) ||
			 ( monitor.getNffg( nodeInterface.getNffg().getName() ) == null ) )
			throw new NfvReaderException( "buildNode: inexistent hosting host/NFFG" );

		// retrieve links ---------------------------------------------------
		Node.Links links = of.createNodeLinks();

		Set<LinkReader>  setLinkInterfaces = nodeInterface.getLinks();

		if ( !( setLinkInterfaces.isEmpty() ) ) {
			List<Link> liveListXMLLinks = links.getLink();

			for ( LinkReader linkInterface : setLinkInterfaces ) {
				Link link = buildLink( linkInterface );
				try {
					liveListXMLLinks.add( link );
				} catch ( Exception e ) {
					throw new NfvReaderException( e.getMessage() );
				}
			}
		}

		// Prepare node -----------------------------------------------------
		Node node = of.createNode();
		node.setName( nodeInterface.getName() );
		node.setHostingHost( nodeInterface.getHost().getName() );
		node.setAssociatedNFFG( nodeInterface.getNffg().getName() );
		node.setFunctionalType( nodeInterface.getFuncType().getName() );
		node.setLinks( links );

		return node;
	}



	/**
	 * Creates a new Link JAXB java object by reading required data from a
	 * {@link LinkReader} interface.
	 *
	 * @param  linkInterface a {@link LinkReader} interface
	 * @return               a new Link JAXB java object
	 * @throws NfvReaderException
	 */
	private Link buildLink ( LinkReader linkInterface )
			throws NfvReaderException {

		if ( linkInterface == null )
			throw new NfvReaderException( "buildLink: null argument" );

		if ( ( linkInterface.getName()            == null ) ||
			 ( linkInterface.getSourceNode()      == null ) ||
			 ( linkInterface.getDestinationNode() == null ) )
			throw new NfvReaderException( "buildLink: invalid link" );

		if ( !( linkInterface.getName().matches( NAME_REGEX ) ) )
			throw new NfvReaderException( "buildLink: invalid link name" );

		if ( ( linkInterface.getSourceNode().getName()      == null ) ||
			 ( linkInterface.getDestinationNode().getName() == null ) )
			throw new NfvReaderException( "buildLink: missing link endpoint names" );

		if ( ( monitor.getNffg( linkInterface.getSourceNode().getNffg().getName() )      == null ) ||
			 ( monitor.getNffg( linkInterface.getDestinationNode().getNffg().getName() ) == null ) )
			throw new NfvReaderException( "buildLink: invalid endpoints" );

		if ( monitor.getConnectionPerformance( linkInterface.getSourceNode().getHost(),
				                               linkInterface.getDestinationNode().getHost() ) == null )
			throw new NfvReaderException( "buildLink: invalid link, no connection between hosts hosting node enpoints" );


		// retrieve link minThroughput --------------------------------------
		Throughput throughput = null; // NOTE: link throughput may be missing
		if ( linkInterface.getThroughput() != 0 ) {

			{
				HostReader srcHost = linkInterface.getSourceNode().getHost();
				HostReader dstHost = linkInterface.getDestinationNode().getHost();
				if ( linkInterface.getThroughput() < monitor.getConnectionPerformance( srcHost, dstHost ).getThroughput() )
					throw new NfvReaderException( "buildLink: throughput exceeding connection limits" );
			}

			throughput = of.createThroughput();
			throughput.setValue( linkInterface.getThroughput() );
			throughput.setUnit( throughput.getUnit() );
		}

		// retrieve link maxLatency -----------------------------------------
		Latency latency = null; // NOTE: link latency may be missing
		if ( linkInterface.getLatency() != 0 ) {

			{
				HostReader srcHost = linkInterface.getSourceNode().getHost();
				HostReader dstHost = linkInterface.getDestinationNode().getHost();
				if ( linkInterface.getLatency() < monitor.getConnectionPerformance( srcHost, dstHost ).getLatency() )
					throw new NfvReaderException( "buildLink: latency exceeding connection limits" );
			}

			latency = of.createLatency();
			latency.setValue( linkInterface.getLatency() );
			latency.setUnit( latency.getUnit() );
		}

        // prepare Link -----------------------------------------------------
		Link link = of.createLink();
		link.setName( linkInterface.getName() );
		link.setSourceNode( linkInterface.getSourceNode().getName() );
		link.setDestinationNode( linkInterface.getDestinationNode().getName() );
		link.setMaxLatency( latency );
		link.setMinThroughput( throughput );

		return link;
	}

}
