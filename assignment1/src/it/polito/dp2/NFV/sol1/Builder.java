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
 * @author Daniel C. Rusu
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
		
		NFVSystemType nfvSystem = of.createNFVSystemType();

		InfrastructureNetwork in = buildIN(); // build Infrastructure Network
		nfvSystem.setIN( in );                // add Infrastructure Network to NFVSystem
		
		Catalogue catalogue = buildCatalogue(); // build Catalogue
		nfvSystem.setCatalogue( catalogue );    // add Catalogue to NFVSystem
		
		NFVSystemType.DeployedNFFGs deployedNFFGs = buildDeployedNFFGs(); // deployed NFFGs
		nfvSystem.setDeployedNFFGs( deployedNFFGs ); // add deployedNFFGs to NFVSystem

		return nfvSystem;
	}
	
	
	
	/**
	 * Builds an Infrastructure Network object from NFVSystem interfaces.
	 * 
	 * @param nfvs NFVSystem to be updated with Infrastructure Network
	 */
	private InfrastructureNetwork buildIN() {

		// retrieve hosts ---------------------------------------------------
		InfrastructureNetwork.Hosts hosts = 
				of.createInfrastructureNetworkHosts();
		
		List<Host> hosts_list = hosts.getHost(); // live list
		Host       host       = null;
				
		ArrayList<HostReader> allhosts = new ArrayList<HostReader>();

		Set<HostReader> hosts_set = monitor.getHosts();
		
		for ( HostReader hr : hosts_set ) {
			host = buildHost( hr );
			hosts_list.add( host );
			allhosts.add( hr );
		}

		// retrieve connections between hosts -------------------------------
		InfrastructureNetwork.Connections connections = 
				of.createInfrastructureNetworkConnections();
		
		List<Connection> connections_list = connections.getConnection(); // live list
		Connection       connection       = null;
		
		for ( int i = 0; i < allhosts.size(); i++ )
			for ( int j = 0; j < allhosts.size(); j++ ) {
				connection = buildConnection( allhosts.get(i), 
						                      allhosts.get(j) );
				if ( connection != null )
					connections_list.add( connection );
			}

		// prepare IN -------------------------------------------------------
		InfrastructureNetwork in = of.createInfrastructureNetwork();

		in.setHosts( hosts );             // add hosts to IN
		in.setConnections( connections ); // add connections to IN
		
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

		// retrieve VNFs ----------------------------------------------------
		Catalogue catalogue = of.createCatalogue();
		
		List<VNF> vnfs = catalogue.getVnf(); // live list
		VNF       vnf  = null;
		
		Set<VNFTypeReader> vnfreads = monitor.getVNFCatalog();

		for ( VNFTypeReader vnftr : vnfreads ) {
			vnf = buildVNF( vnftr );
			vnfs.add( vnf );
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
		
		// retrieve NFFGs ---------------------------------------------------
		NFVSystemType.DeployedNFFGs deployedNFFGs = 
				of.createNFVSystemTypeDeployedNFFGs();
		
		List<NFFG> nffg_list = deployedNFFGs.getNffg(); // live list
		NFFG       nffg      = null;
		
		Set<NffgReader> nffgs_set = monitor.getNffgs(null);

		for ( NffgReader nr : nffgs_set ) {
			nffg = buildNFFG( nr );
			nffg_list.add( nffg );
		}
		
		return deployedNFFGs;
	}
	
	

	/**
	 * Creates a new Host JAXB java object by reading required data from a
	 * NFVSystem HostReader interface.
	 * 
	 * @param  hr HostReader interface
	 * @return    a new Host JAXB java object
	 */
	private Host buildHost( HostReader hr ) {

		// retrieve available memory  and storage ---------------------------
		SizeInMB am = buildSizeInMB( BigInteger.valueOf( hr.getAvailableMemory()  ) );
		SizeInMB as = buildSizeInMB( BigInteger.valueOf( hr.getAvailableStorage() ) );
		
		// retrieve all nodes allocated in current host ---------------------
		Host.AllocatedNodes allocatedNodes = of.createHostAllocatedNodes();

		List<NodeRef> nodeRef_list   = allocatedNodes.getNode(); // live list
		NodeRef       noderef        = null; // Node "Reference"
				

		Set<NodeReader> nodes_set = hr.getNodes();

		for ( NodeReader nr : nodes_set ) {
			noderef = of.createNodeRef();
			
			noderef.setName( nr.getName() );                     // node's name
			noderef.setAssociatedNFFG( nr.getNffg().getName() ); // node's nffg

			nodeRef_list.add( noderef );
		}

		// prepare host data structure --------------------------------------
		Host host = of.createHost();
		host.setName( hr.getName() );
		host.setMaxVNFs( hr.getMaxVNFs() );
		host.setInstalledMemory( am );
		host.setInstalledStorage( as );
		host.setAllocatedNodes( allocatedNodes );

		return host;
	}

	
	
	/**
	 * Creates a new SizeInMB JAXB java object given a BigInteger value.
	 * 
	 * @param  value a BigInteger value
	 * @return       a new SizeInMB JAXB java object
	 */
	private SizeInMB buildSizeInMB(BigInteger value) {
		SizeInMB sim = of.createSizeInMB();
		sim.setValue( value );
		sim.setUnit( sim.getUnit() );

		return sim;
	}

	
	
	
	/**
	 * Creates a new Connection JAXB java object given two end point hosts.
	 * 
	 * @param  sourceHost the source Host
	 * @param  destHost   the destination Host
	 * @return            a new Connection JAXB java object
	 */
	private Connection buildConnection( HostReader sourceHost,
			                              HostReader destHost    ) {


		ConnectionPerformanceReader cpr = 
				monitor.getConnectionPerformance( sourceHost, destHost );
		
		if ( cpr == null )
			return null;
		
		// retrieve connection ID (source and destination host name) --------
		Connection.ConnectionID cID = of.createConnectionConnectionID();
		cID.setSourceHost( sourceHost.getName() );
		cID.setDestionationHost( destHost.getName() );

		// retrieve connection Throughput -----------------------------------
		Throughput tp = of.createThroughput();
		tp.setValue( cpr.getThroughput() );
		tp.setUnit( tp.getUnit() );

		// retrieve connection Latency --------------------------------------
		Latency lat = of.createLatency();
		lat.setValue( cpr.getLatency() );
		lat.setUnit( lat.getUnit() );

		// prepare connection -----------------------------------------------
		Connection connection = of.createConnection();
		connection.setAverageThroughput( tp );
		connection.setLatency( lat );
		connection.setConnectionID( cID );

		return connection;
	}
	
	

	/**
	 * Creates a new VNF JAXB java object by reading required data from a
	 * VNFTypeReader interface.
	 * 
	 * @param  vnftr a VNFTypeReader interface
	 * @return       a new VNF JAXB java object
	 */
	private VNF buildVNF( VNFTypeReader vnftr ) {

		// retrieve required memory and storage -----------------------------
		SizeInMB rm = buildSizeInMB( BigInteger.valueOf( vnftr.getRequiredMemory()  ) );
		SizeInMB rs = buildSizeInMB( BigInteger.valueOf( vnftr.getRequiredStorage() ) );
		
		// retrieve functional type string ----------------------------------
		String ftstr = vnftr.getFunctionalType().value();

		// prepare VNF ------------------------------------------------------
		VNF vnf = of.createVNF();
		vnf.setName( vnftr.getName() );
		vnf.setRequiredMemory( rm );
		vnf.setRequiredStorage( rs );
		vnf.setFunctionalType( FunctionalTypeEnum.fromValue( ftstr ) );

		return vnf;
	}

	

	/**
	 * Creates a new NFFG JAXB java object by reading required data from a
	 * NffgReader interface.
	 * 
	 * @param  nr a NffgReader interface
	 * @return    a new NFFG JAXB java object
	 */
	private NFFG buildNFFG( NffgReader nr ) {

		// retrieve nffg's nodes --------------------------------------------
		NFFG.Nodes nodes = of.createNFFGNodes();

		List<Node> nodes_list = nodes.getNode(); // live list
		Node       node       = null;
		
		Set<NodeReader> nodes_set = nr.getNodes();
		
		for ( NodeReader ndr : nodes_set ) {
			node = buildNode ( ndr );
			nodes_list.add( node );
		}
		
		// retrieve nffg's deploy time --------------------------------------
		Calendar c  = nr.getDeployTime();
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis( c.getTimeInMillis() );
		
		XMLGregorianCalendar time = null;
		try {
			
			 time = DatatypeFactory.newInstance().newXMLGregorianCalendar( gc );
			
		} catch (DatatypeConfigurationException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		} catch ( NullPointerException npe ) {
			System.err.println( npe.getMessage() );
			npe.printStackTrace();
			System.exit(-1);
		}

		// prepare NFFG -----------------------------------------------------
		NFFG nffg = of.createNFFG();
		nffg.setName( nr.getName() );
		nffg.setDeployTime( time );
		nffg.setNodes( nodes );

		return nffg;
	}
	
	
	
	/**
	 * Creates a new Node JAXB java object by reading required data form a
	 * NodeReader interface.
	 * 
	 * @param  nr a NodeReader interface
	 * @return    a new Node JAXB java object
	 */
	private Node buildNode ( NodeReader nr ) {

		// retrieve links ---------------------------------------------------
		Node.Links links = of.createNodeLinks();

		List<Link> links_list = links.getLink();
		Link       link       = null;
		
		Set<LinkReader>  links_set = nr.getLinks();
		for ( LinkReader lr : links_set ) {
			link = buildLink( lr );
			links_list.add( link );
		}

		// Prepare node
		Node node = of.createNode();
		node.setName( nr.getName() );
		node.setFunctionalType( nr.getFuncType().getName() );
		node.setHostingHost( nr.getHost().getName() );
		node.setAssociatedNFFG( nr.getNffg().getName() );
		node.setLinks( links );

		return node;
	}
	
	

	/**
	 * Creates a new Link JAXB java object by reading required data from a
	 * LinkReader interface.
	 * 
	 * @param  lr a LinkReader interface
	 * @return    a new Link JAXB java object
	 */
	private Link buildLink ( LinkReader lr ) {

		// retrieve link minThroughput --------------------------------------
		Throughput tp = of.createThroughput();
		tp.setValue( lr.getThroughput() );
		tp.setUnit( tp.getUnit() );

		// retrieve link maxLatency -----------------------------------------
		Latency lat = of.createLatency();
		lat.setValue( lr.getLatency() );
		lat.setUnit( lat.getUnit() );

        // prepare Link -----------------------------------------------------
		Link link = of.createLink();
		link.setName( lr.getName() );
		link.setSourceNode( lr.getSourceNode().getName() );
		link.setDestinationNode( lr.getDestinationNode().getName() );

		return link;
	}

}
