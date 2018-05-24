package it.polito.dp2.NFV.sol1;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import java.math.BigInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.SchemaFactoryConfigurationError;

import org.xml.sax.SAXException;

import it.polito.dp2.NFV.*;           // import data creator library
import it.polito.dp2.NFV.sol1.jaxb.*; // import java content classes generated by binding compiler


public class NfvInfoSerializer {
	private NfvReader monitor   = null;
	private String    outputXML = null;
	private ObjectFactory of    = null;

	/**
	 * Class constructor.
	 * 
	 * @throws NfvReaderException
	 */
	public NfvInfoSerializer() throws NfvReaderException {
		NfvReaderFactory factory = NfvReaderFactory.newInstance();
		monitor = factory.newNfvReader();
		of      = new ObjectFactory();
	}


	/**
	 * Class constructor setting the name of the XML file to be created.
	 * 
	 * @param oxml
	 * @throws NfvReaderException
	 */
	public NfvInfoSerializer(String oxml) throws NfvReaderException {
		NfvReaderFactory factory = NfvReaderFactory.newInstance();
		monitor   = factory.newNfvReader();
		of        = new ObjectFactory();
		outputXML = new String(oxml);
	}


	public static void main(String[] args) {


		// Check args
		if ( (args.length == 0) || (args.length > 1) ) {
			System.out.println("ERR: usage: java <program_name> <outputXML>");
			System.exit(-1);
		}

		// Instantiate data generator
		NfvInfoSerializer nis = null;

		try {

			nis = new NfvInfoSerializer(args[0]);

		} catch (NfvReaderException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Start data retrieval and marshalling
		try {

			nis.do_work();

		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}

	/**
	 * Retrieves data from NFVSystem interfaces and marshalls it to the output
	 * XML file.
	 */
	private void do_work() {
		
		final String contxtPath     = new String( "it.polito.dp2.NFV.sol1.jaxb" );
		final String namespace      = new String( "http://www.example.org/nfvInfo" );
		final String schemaLocation = new String( "/xsd/nfvInfo.xsd" );
		
		String schemaFile = null;
		Schema schema     = null;
		
		try {
			/* --------------------------------------------------------------
			 *  get current working directory (which should be [ROOT])
			 *  and find the xml schema to be used for validation
			 *  under "[ROOT]/xsd/"
			 */
			Properties sysProps = System.getProperties();

			schemaFile = new String( sysProps.getProperty("user.dir") + schemaLocation );
			
			SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			schema = sf.newSchema( new File( schemaFile ) );
		} 
		catch ( SecurityException se )                 { schema = null; System.err.println(se);   } 
		catch ( SAXException saxe )                    { schema = null; System.err.println(saxe); } 
		catch ( SchemaFactoryConfigurationError sfce ) { schema = null; System.err.println(sfce); } 
		catch ( NullPointerException npe )             { schema = null; System.err.println(npe);  } 
		catch ( IllegalArgumentException iae )         { schema = null; System.err.println(iae);  } 
		
		/* ------------------------------------------------------------------
		 * retrieve data to marshal 
		 */
		NFVSystemType nfvSystem = of.createNFVSystemType();

		retrieveIN( nfvSystem );            // Infrastructure Network
		retrieveCatalogue( nfvSystem );     // Catalogue
		retrieveDeployedNFFGs( nfvSystem ); // deployed NFFGs
		
		
		/* ------------------------------------------------------------------
		 * create jaxb context and marshaller 
		 */
		try {
			JAXBContext jc = JAXBContext.newInstance( contxtPath );
			Marshaller  m  = jc.createMarshaller();
			
			/* --------------------------------------------------------------
			 * set marshaller properties
			 */
			try {
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				
				if ( schemaFile != null )
					m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, (namespace+" "+schemaFile) );				
				
				if ( schema != null ) {
					
					m.setSchema(schema);
					m.setEventHandler(
							new ValidationEventHandler() {
								@Override
								public boolean handleEvent(ValidationEvent event) {
									
									ValidationEventLocator vel = event.getLocator();
									
									System.out.println("Line:Col[" + 
									                   vel.getLineNumber() + ":" + 
									                   vel.getColumnNumber() + "]:" +
									                   event.getMessage());
									
									if ( event.getSeverity() == ValidationEvent.WARNING )
										return true;
									
									return false;
								}
							}
					);
				}
			} 
			catch ( PropertyException pre )             { System.err.println(pre); } 
			catch ( IllegalArgumentException iae )      { System.err.println(iae); }
			catch ( UnsupportedOperationException uoe ) { System.err.println(uoe); }
			catch ( JAXBException jbe )                 { System.err.println(jbe); }
		
		
			/* --------------------------------------------------------------
			 * marshal
			 */
			JAXBElement<NFVSystemType> nfvRootElement = of.createNFVSystem(nfvSystem);
			
			JAXB.marshal(nfvRootElement, new FileOutputStream(outputXML) );
		
		} catch ( JAXBException jbe ) { 
			jbe.printStackTrace();
			System.exit(-1); 
		} catch ( DataBindingException dbe ) {
			dbe.printStackTrace();
			System.exit(-1);
		} catch ( FileNotFoundException fne ) {
			fne.printStackTrace(); 
			System.exit(-1);
		} catch ( SecurityException see ) { 
			see.printStackTrace();     
			System.exit(-1);
        }
	}
	
	/**
	 * Retrieves all the Infrastructure Network data structure from
	 * NFVSystem interfaces and creates java objects to be
	 * marshalled into the XML file.
	 * 
	 * @param nfvs NFVSystem to be updated with Infrastructure Network
	 */
	protected void retrieveIN(NFVSystemType nfvs) {

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

		nfvs.setIN( in ); // add IN to NFVSystem
	}
	
	
	/**
	 * Retrieves all the Catalogue data structure from NFVSystem
	 * interfaces and creates corresponding jaxb java objects to be
	 * marshalled into the XML file
	 * 
	 * @param nfvs NFVSystem to be updated with Catalogue
	 */
	private void retrieveCatalogue(NFVSystemType nfvs) {

		// retrieve VNFs ----------------------------------------------------
		Catalogue catalogue = of.createCatalogue();
		
		List<VNF> vnfs = catalogue.getVnf(); // live list
		VNF       vnf  = null;
		
		Set<VNFTypeReader> vnfreads = monitor.getVNFCatalog();

		for ( VNFTypeReader vnftr : vnfreads ) {
			vnf = buildVNF( vnftr );
			vnfs.add( vnf );
		}

		nfvs.setCatalogue( catalogue ); // add catalogue to NFVSystem
	}

	
	/**
	 * Retrieves all the deployed NFFGs from NFVSystem interfaces and
	 * creates corresponding java jaxb java objects to be marshalled into
	 * the XML file.
	 * 
	 * @param nfvs NFVSystem to be updated with deployed NFFGs
	 */
	private void retrieveDeployedNFFGs(NFVSystemType nfvs) {
		
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

		nfvs.setDeployedNFFGs( deployedNFFGs ); // add deployedNFFGs to NFVSystem
	}

	
	/**
	 * Creates a new Host jaxb java object by reading required data from a
	 * NFVSystem HostReader interface.
	 * 
	 * @param  hr HostReader interface
	 * @return    a new Host jaxb java object
	 */
	public Host buildHost( HostReader hr ) {

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
	 * Creates a new SizeInMB jaxb java object given a BigInteger value.
	 * 
	 * @param  value a BigInteger value
	 * @return       a new SizeInMB jaxb java object
	 */
	protected SizeInMB buildSizeInMB(BigInteger value) {
		SizeInMB sim = of.createSizeInMB();
		sim.setValue( value );
		sim.setUnit( sim.getUnit() );

		return sim;
	}

	
	/**
	 * Creates a new Connection jaxb java object given two end point hosts.
	 * 
	 * @param  sourceHost the source Host
	 * @param  destHost   the destination Host
	 * @return            a new Connection jaxb java object
	 */
	protected Connection buildConnection( HostReader sourceHost,
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
	 * Creates a new VNF jaxb java object by reading required data from a
	 * VNFTypeReader interface.
	 * 
	 * @param  vnftr a VNFTypeReader interface
	 * @return       a new VNF jaxb java object
	 */
	protected VNF buildVNF( VNFTypeReader vnftr ) {

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
	 * Creates a new NFFG jaxb java object by reading required data from a
	 * NffgReader interface.
	 * 
	 * @param  nr a NffgReader interface
	 * @return    a new NFFG jaxb java object
	 */
	protected NFFG buildNFFG( NffgReader nr ) {

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
	 * Creates a new Node jaxb java object by reading required data form a
	 * NodeReader interface.
	 * 
	 * @param  nr a NodeReader interface
	 * @return    a new Node jaxb java object
	 */
	protected Node buildNode ( NodeReader nr ) {

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
	 * Creates a new Link jaxb java object by reading required data from a
	 * LinkReader interface.
	 * 
	 * @param  lr a LinkReader interface
	 * @return    a new Link jaxb java object
	 */
	protected Link buildLink ( LinkReader lr ) {

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
