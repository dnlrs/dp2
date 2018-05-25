package it.polito.dp2.NFV.sol1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
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
 * This class reads data from it.polito.dp2.NFV System interfaces and builds
 * JAXB java objects that are marshallable into an XML file.
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 *
 */
public class Builder  {
	
	private NfvReader     monitor; // Access to NFV System interfaces
	private ObjectFactory of;      // JAXB object factory
	
	
	protected Builder() throws NfvReaderException {
		
		NfvReaderFactory factory = NfvReaderFactory.newInstance();
		
		monitor   = factory.newNfvReader();
		of        = new ObjectFactory();
		
	}
	
	
	
	/**
	 * Creates a root marshallable JAXBElement (document root) from an 
	 * existing NFVSystemType object.
	 * 
	 * @param  nfvSystem a NFVSystemType Object
	 * @return           a JAXBElement (document root)
	 */
	protected JAXBElement<NFVSystemType> getRootElement( NFVSystemType nfvSystem ) {
		
		if ( nfvSystem == null )
			return null;
		
		return of.createNFVSystem(nfvSystem);
	}
	
	
	
	/**
	 * Builds a NFVSystemType object.
	 * 
	 * @return a NFVSystemType object
	 */
	protected NFVSystemType buildNFVSystemType() {
		
		InfrastructureNetwork       in            = buildIN(); // build Infrastructure Network
		Catalogue                   catalogue     = buildCatalogue(); // build Catalogue
		NFVSystemType.DeployedNFFGs deployedNFFGs = buildDeployedNFFGs(); // deployed NFFGs
		

		NFVSystemType nfvSystem = of.createNFVSystemType();
		nfvSystem.setIN( in );                       // add Infrastructure Network to NFVSystem
		nfvSystem.setCatalogue( catalogue );         // add Catalogue to NFVSystem
		nfvSystem.setDeployedNFFGs( deployedNFFGs ); // add deployedNFFGs to NFVSystem

		return nfvSystem;
	}
	
	
	
	/**
	 * Builds an Infrastructure Network object from NFVSystem interfaces.
	 * 
	 * @param nfvs NFVSystem to be updated with Infrastructure Network
	 */
	private InfrastructureNetwork buildIN() {

		InfrastructureNetwork.Hosts hosts = 
				of.createInfrastructureNetworkHosts();
		
		InfrastructureNetwork.Connections connections = 
				of.createInfrastructureNetworkConnections();

		
		// retrieve host interfaces -----------------------------------------
		Set<HostReader>       setHostInterfaces = monitor.getHosts();
		
		/*
		 * NOTE: if no hosts then no connections
		 */
		if ( !( setHostInterfaces.isEmpty() ) ) {
			
			// read hosts --------------------------------------------------- 
			List<Host> liveListXMLHosts = hosts.getHost();
			for ( HostReader hostInterface : setHostInterfaces ) {
				Host host = buildHost( hostInterface );
				
				if ( host != null )
					liveListXMLHosts.add( host );
			}
		
			// read connections between hosts -------------------------------
			List<Connection> liveListXMLConnections = connections.getConnection();
			
			/*
			 * NOTE: there may be also connection between a host and itself
			 */
			for ( HostReader sourceHostInterface : setHostInterfaces )
				for ( HostReader destHostInterface : setHostInterfaces ) {
					
					Connection connection = 
							buildConnection( sourceHostInterface, destHostInterface );
					
					if ( connection != null )
						liveListXMLConnections.add( connection );
				}

		}
		
		// prepare IN -------------------------------------------------------
		InfrastructureNetwork in = of.createInfrastructureNetwork();

		in.setHosts( hosts );             // add hosts to Infrastructure Network
		in.setConnections( connections ); // add connections to Infrastructure Network
		
		return in;
	}
	
	
	
	/**
	 * Retrieves all the Catalogue data structure from NFVSystem
	 * interfaces and creates corresponding JAXB java objects to be
	 * marshalled into the XML file
	 * 
	 * @param nfvs NFVSystem to be updated with Catalogue
	 */
	private Catalogue buildCatalogue() {

		Catalogue catalogue = of.createCatalogue();
		
		Set<VNFTypeReader> setVNFsInterfaces = monitor.getVNFCatalog();
		
		if ( !( setVNFsInterfaces.isEmpty() ) ) {
			List<VNF> liveListVNFs = catalogue.getVnf(); 
			
			for ( VNFTypeReader vnfInterface : setVNFsInterfaces ) {
				VNF vnf = buildVNF( vnfInterface );
				
				if ( vnf != null )
					liveListVNFs.add( vnf );
			}
		}
		
		return catalogue;
	}
	
	
	
	/**
	 * Retrieves all the deployed NFFGs from NFVSystem interfaces and
	 * creates corresponding java JAXB java objects to be marshalled into
	 * the XML file.
	 * 
	 * @param nfvs NFVSystem to be updated with deployed NFFGs
	 */
	private NFVSystemType.DeployedNFFGs buildDeployedNFFGs() {
		
		NFVSystemType.DeployedNFFGs deployedNFFGs = 
				of.createNFVSystemTypeDeployedNFFGs();
		
		Set<NffgReader> setNFFGsInterfaces = monitor.getNffgs(null);
		
		if ( !( setNFFGsInterfaces.isEmpty() ) ) {			
			List<NFFG> liveListNFFGs = deployedNFFGs.getNffg(); // live list
			
	
			for ( NffgReader nffgInterface : setNFFGsInterfaces ) {
				NFFG nffg = buildNFFG( nffgInterface );
				
				if ( nffg != null )
					liveListNFFGs.add( nffg );
			}
		}
		
		return deployedNFFGs;
	}
	
	

	/**
	 * Creates a new Host JAXB java object by reading required data from a
	 * NFVSystem HostReader interface.
	 * 
	 * @param  hI HostReader interface
	 * @return    a new Host JAXB java object, null if host is invalid
	 */
	private Host buildHost( HostReader hI ) {
		
		if ( hI == null )
			return null; // invalid argument
		
		if ( ( hI.getName() == null ) || ( hI.getMaxVNFs() < 0 ) )
			return null; // invalid host

		// retrieve Available Memory and Storage ---------------------------
		SizeInMB am = buildSizeInMB( BigInteger.valueOf( hI.getAvailableMemory()  ) );
		SizeInMB as = buildSizeInMB( BigInteger.valueOf( hI.getAvailableStorage() ) );
		
		if ( ( am == null ) || ( as == null ) )
			return null; // invalid values means invalid host
		
		// retrieve all nodes allocated in current host ---------------------
		Host.AllocatedNodes allocatedNodes = of.createHostAllocatedNodes();

		Set<NodeReader> setNodesInterfaces = hI.getNodes();
		
		if ( !( setNodesInterfaces.isEmpty() ) ) {
			List<NodeRef> liveListXMLNodeRefs   = allocatedNodes.getNode();					
	
			for ( NodeReader nodeInterface : setNodesInterfaces ) {
				NffgReader nffgInterface = nodeInterface.getNffg();
				
				if ( nffgInterface == null )
					continue;
				
				String nodeName = nodeInterface.getName();
				String nffgName = nffgInterface.getName();
				
				if ( ( nodeName == null ) || ( nffgName == null ) )
					continue;
				
				NodeRef nodeRef = of.createNodeRef();
				nodeRef.setName( nodeName );
				nodeRef.setAssociatedNFFG( nffgName );
	
				liveListXMLNodeRefs.add( nodeRef );
			}
		}

		// prepare host data structure --------------------------------------
		Host host = of.createHost();
		host.setName( hI.getName() );
		host.setMaxVNFs( hI.getMaxVNFs() );
		host.setInstalledMemory( am );
		host.setInstalledStorage( as );
		host.setAllocatedNodes( allocatedNodes );

		return host;
	}

	
	
	/**
	 * Creates a new SizeInMB JAXB java object given a BigInteger value.
	 * 
	 * @param  value a BigInteger value
	 * @return       a new SizeInMB JAXB java object, null if value is invalid
	 */
	private SizeInMB buildSizeInMB(BigInteger value) {
		
		if ( value == null )
			return null; // invalid argument
		
		if ( value.intValue() < 0  )
			return null; // invalid value
		
		SizeInMB sim = of.createSizeInMB();
		sim.setValue( value );
		sim.setUnit( sim.getUnit() );

		return sim;
	}

	
	
	
	/**
	 * Creates a new Connection JAXB java object given two end point hosts.
	 * 
	 * @param  sourceHostI the source Host
	 * @param  destHostI   the destination Host
	 * @return            a new Connection JAXB java object, null if errors
	 */
	private Connection buildConnection( HostReader sourceHostI, 
			                            HostReader destHostI    ) {

		if ( ( sourceHostI == null ) || ( destHostI == null ) )
			return null; // invalid arguments

		ConnectionPerformanceReader connectionInterface = 
				monitor.getConnectionPerformance( sourceHostI, destHostI );
		
		if ( connectionInterface == null )
			return null;
		
		// retrieve connection ID (source and destination host name) --------
		if ( ( sourceHostI.getName() == null ) || ( destHostI.getName() == null ) )
			return null; // connection must have valid endpoints
		
		Connection.ConnectionID connectionID = of.createConnectionConnectionID();
		connectionID.setSourceHost( sourceHostI.getName() );
		connectionID.setDestinationHost( destHostI.getName() );

		// retrieve connection Throughput -----------------------------------
		if ( connectionInterface.getThroughput() < 0 )
			return null; // connection must have valid throughput
		
		Throughput throughput = of.createThroughput();
		throughput.setValue( connectionInterface.getThroughput() );
		throughput.setUnit( throughput.getUnit() );

		// retrieve connection Latency --------------------------------------
		if ( connectionInterface.getLatency() < 0 )
			return null; // connection must have valid latency
		
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
	 * VNFTypeReader interface.
	 * 
	 * @param  vnfInterface a VNFTypeReader interface
	 * @return       a new VNF JAXB java object
	 */
	private VNF buildVNF( VNFTypeReader vnfInterface ) {
		
		if ( vnfInterface == null )
			return null; // invalid argument
		
		if ( vnfInterface.getName() == null )
			return null; // vnf must have a name
		
		// retrieve required memory and storage -----------------------------
		SizeInMB rm = buildSizeInMB( BigInteger.valueOf( vnfInterface.getRequiredMemory()  ) );
		SizeInMB rs = buildSizeInMB( BigInteger.valueOf( vnfInterface.getRequiredStorage() ) );
		
		// retrieve functional type string ----------------------------------
		if ( vnfInterface.getFunctionalType() == null )
			return null; // vnf must have a functional type
		
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
	 * NffgReader interface.
	 * 
	 * @param  nffgInterface a NffgReader interface
	 * @return    a new NFFG JAXB java object, null if errors
	 */
	private NFFG buildNFFG( NffgReader nffgInterface ) {
		
		if ( nffgInterface == null )
			return null; // invalid argument
		
		if ( nffgInterface.getName() == null )
			return null; // NFFG must have a name
		
		if ( nffgInterface.getDeployTime() == null )
			return null; // NFFG must have a deploy time
		
		// retrieve nffg's nodes --------------------------------------------
		NFFG.Nodes nodes = of.createNFFGNodes();

		Set<NodeReader> setNodeInterfaces = nffgInterface.getNodes();

		if ( !( setNodeInterfaces.isEmpty() ) ) {
			List<Node> liveListXMLNodes = nodes.getNode(); // live list
			
			for ( NodeReader nodeInterface : setNodeInterfaces ) {
				Node node = buildNode ( nodeInterface );
				
				if ( node != null )
					liveListXMLNodes.add( node );
			}
		}
		
		// retrieve nffg's deploy time --------------------------------------
		Calendar c  = nffgInterface.getDeployTime();
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis( c.getTimeInMillis() );
		
		XMLGregorianCalendar time = null;
		try {
			
			 time = DatatypeFactory.newInstance().newXMLGregorianCalendar( gc );
			
		} catch (DatatypeConfigurationException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return null;
		} catch ( NullPointerException npe ) {
			System.err.println( npe.getMessage() );
			npe.printStackTrace();
			return null; 
		}
		
		if ( time == null )
			return null; // no time, no nffg

		// prepare NFFG -----------------------------------------------------
		NFFG nffg = of.createNFFG();
		nffg.setName( nffgInterface.getName() );
		nffg.setDeployTime( time );
		nffg.setNodes( nodes );

		return nffg;
	}
	
	
	
	/**
	 * Creates a new Node JAXB java object by reading required data form a
	 * NodeReader interface.
	 * 
	 * @param  nodeInterface a NodeReader interface
	 * @return    a new Node JAXB java object, null if errors
	 */
	private Node buildNode ( NodeReader nodeInterface ) {
		
		if ( nodeInterface == null )
			return null; // invalid argument
		
		if ( ( nodeInterface.getName() == null ) ||
			 ( nodeInterface.getHost() == null ) ||
			 ( nodeInterface.getNffg() == null ) ||
			( nodeInterface.getFuncType() == null ) ) 
			return null; // invalid node
		
		if ( ( nodeInterface.getHost().getName() == null ) ||
			 ( nodeInterface.getNffg().getName() == null ) ||
			 ( nodeInterface.getFuncType().getName() == null ) )
			return null; // invalid node

		// retrieve links ---------------------------------------------------
		Node.Links links = of.createNodeLinks();

		Set<LinkReader>  setLinkInterfaces = nodeInterface.getLinks();

		if ( !( setLinkInterfaces.isEmpty() ) ) {
			List<Link> liveListXMLLinks = links.getLink();
			
			for ( LinkReader linkInterface : setLinkInterfaces ) {
				Link link = buildLink( linkInterface );
				
				if ( link != null )
					liveListXMLLinks.add( link );
			}
		}

		// Prepare node -----------------------------------------------------
		Node node = of.createNode();
		node.setLinks( links );
		node.setName( nodeInterface.getName() );
		node.setHostingHost( nodeInterface.getHost().getName() );
		node.setAssociatedNFFG( nodeInterface.getNffg().getName() );
		node.setFunctionalType( nodeInterface.getFuncType().getName() );

		return node;
	}
	
	

	/**
	 * Creates a new Link JAXB java object by reading required data from a
	 * LinkReader interface.
	 * 
	 * @param  linkInterface a LinkReader interface
	 * @return    a new Link JAXB java object
	 */
	private Link buildLink ( LinkReader linkInterface ) {
		
		if ( linkInterface == null )
			return null; // invalid argument
		
		if ( ( linkInterface.getName() == null ) ||
			 ( linkInterface.getSourceNode() == null ) ||
			 ( linkInterface.getDestinationNode() == null ) )
			return null; // invalid link
		
		if ( ( linkInterface.getSourceNode().getName() == null ) || 
			 ( linkInterface.getDestinationNode().getName() == null ) )
			return null; // invalid link
		
		// retrieve link minThroughput --------------------------------------
		Throughput throughput = null;
		if ( linkInterface.getThroughput() != 0 ) {
			throughput = of.createThroughput();
			throughput.setValue( linkInterface.getThroughput() );
			throughput.setUnit( throughput.getUnit() );
		}

		// retrieve link maxLatency -----------------------------------------
		Latency latency = null;
		if ( linkInterface.getLatency() != 0 ) {
			latency = of.createLatency();
			latency.setValue( linkInterface.getLatency() );
			latency.setUnit( latency.getUnit() );
		}

        // prepare Link -----------------------------------------------------
		Link link = of.createLink();
		link.setMaxLatency(latency);
		link.setMinThroughput(throughput);
		link.setName( linkInterface.getName() );
		link.setSourceNode( linkInterface.getSourceNode().getName() );
		link.setDestinationNode( linkInterface.getDestinationNode().getName() );
		
		return link;
	}

}
