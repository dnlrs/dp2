package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
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
import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.*;

public class NfvReaderImpl implements NfvReader {
	
	private final NFVSystemType nfvSystem;
	private final String        xmlFile;
	
//	private final HashMap<String, HostReaderImpl>    dbHosts;
//	private final HashMap<String, NodeReaderImpl>    dbNodes;
//	private final HashMap<String, NffgReaderImpl>    dbNffgs;
//	private final HashMap<String, VNFTypeReaderImpl> dbVNFs;
//	private final HashMap<String, ConnectionPerformanceReaderImpl> dbConns;
//	private final HashMap<String, HashMap<String, LinkReaderImpl>> dbLinks;
	
	
	public NfvReaderImpl() throws Exception {
	
		final String contxtPath     = new String( "it.polito.dp2.NFV.sol1.jaxb" );
		final String fileSysPro     = new String( "it.polito.dp2.NFV.sol1.NfvInfo.file" );
		final String schemaLocation = new String( "/xsd/nfvInfo.xsd" );
		
		String schemaFile;
		
		/* ------------------------------------------------------------------ 
		 * get XML file to load 
		 */
		Properties sysProps = System.getProperties();
		xmlFile = new String( sysProps.getProperty( fileSysPro ) );
		
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
        } catch (org.xml.sax.SAXException se) {
            throw new SAXException(se);
        }
        
		/* ------------------------------------------------------------------
		 * unmarshal
		 */
		Object obj = um.unmarshal( new FileInputStream( xmlFile ) );

		@SuppressWarnings("unchecked")
		JAXBElement<NFVSystemType> element = (JAXBElement<NFVSystemType>) obj;
		
		nfvSystem = element.getValue();
		
//		dbHosts = new HashMap<String, HostReaderImpl>();
//		dbNffgs = new HashMap<String, NffgReaderImpl>();
//		dbNodes = new HashMap<String, NodeReaderImpl>();
//		dbLinks = new HashMap<String, HashMap<String, LinkReaderImpl>>();
//		dbVNFs  = new HashMap<String, VNFTypeReaderImpl>();
//		dbConns = new HashMap<String, ConnectionPerformanceReaderImpl>();
//		
//		initNfvReader();
	}
//	
//	private void initNfvReader() throws Exception {
//		
//		InfrastructureNetwork in = nfvSystem.getIN(); 
//		
//		if ( in == null ) // NFV system didn't have a infrastructure network
//			throw new Exception("Invalid Infrastructure Network.");
//			
//		// ------------------------------------------------------------------
//		// create hosts
//		// ------------------------------------------------------------------
//		InfrastructureNetwork.Hosts hosts = in.getHosts();
//		
//		if ( hosts == null ) // the infrastructure network doesn't have any host
//			throw new Exception("No hosts in Infrastructure Network.");
//		
//		List<Host> listHosts = hosts.getHost();
//		
//		if ( listHosts.size() <= 0 ) // the infrastructure network doesn't have any host
//			throw new Exception("No hosts in Infrastructure Network.");
//		
//		for ( Host h : listHosts ) {
//			
//			HostReaderImpl host = createHost( h );
//			
//			if ( host == null )
//				continue; // invalid host
//			
//			if ( !( dbHosts.containsKey( host.getName() ) ) )
//				dbHosts.put(host.getName(), host);
//		}
//		
//		// ------------------------------------------------------------------
//		// create connections
//		// ------------------------------------------------------------------
//		InfrastructureNetwork.Connections connections = in.getConnections();
//		
//		if ( connections == null ) // the infrastructure network doesn't have any connection
//			throw new Exception("No connections in Infrastructure Network.");
//		
//		List<Connection> listConnections = connections.getConnection();
//		
//		if ( listConnections.size() <= 0 ) // the infrastructure network doesn't have any connection
//			throw new Exception("No connections in Infrastructure Network.");
//		
//		for ( Connection c : listConnections ) {
//			
//			ConnectionPerformanceReaderImpl connection = createConnection( c );
//			
//			if ( connection == null )
//				continue; // invalid connection
//			
//			if ( !( dbConns.containsKey( connection.getConnectionID() ) ) ) {
//				dbConns.put(connection.getConnectionID(), connection);
//			}
//			
//		}
//		
//		// ------------------------------------------------------------------
//		// create catalogue
//		// ------------------------------------------------------------------
//		Catalogue catalogue = nfvSystem.getCatalogue();
//		
//		if ( catalogue == null )
//			throw new Exception("No catalogue found in nfv system.");
//		
//		List<VNF> listVNFs = catalogue.getVnf();
//		
//		if ( listVNFs.size() == 0 )
//			throw new Exception("No VNFs found in catalogue.");
//		
//		for ( VNF v : listVNFs ) {
//			
//			VNFTypeReaderImpl vnf = createVNF( v );
//			
//			if ( vnf == null )
//				continue; // invalid VNF
//			
//			if ( !( dbVNFs.containsKey( vnf.getName() ) ) )
//				dbVNFs.put(vnf.getName(), vnf);
//		}
//		
//		// ------------------------------------------------------------------
//		// create catalogue
//		// ------------------------------------------------------------------
//		NFVSystemType.DeployedNFFGs deployedNFFGs = nfvSystem.getDeployedNFFGs();
//		
//		if ( deployedNFFGs == null )
//			throw new Exception("No deployedNffgs found.");
//		
//		List<NFFG> listNFFGs = deployedNFFGs.getNffg();
//		
//		if ( listNFFGs.size() == 0 )
//			throw new Exception("No deployedNffgs found.");
//		
//		for ( NFFG n : listNFFGs ) {
//			
//			NffgReaderImpl nffg = createNFFG( n );
//			
//			if ( nffg == null )
//				continue;
//			
//			if ( !( dbNffgs.containsKey( nffg.getName() ) ) )
//				dbNffgs.put(nffg.getName(), nffg);
//		}
//		
//		
//		
//		
//	}
	
//	
//	/**
//	 * Creates a HostReader readable object and fills it with
//	 * data taken from the jaxb object passed as argument.
//	 *  
//	 * NOTE: does not set host allocated nodes references 
//	 * 
//	 * @param  h jaxb Host Object
//	 * @return   a HostReader readable object
//	 */
//	private HostReaderImpl createHost( Host h ) {
//		
//		if ( h == null )
//			return null; // wrong parameter
//		
//		if ( h.getName() == null )
//			return null; // host without name
//		
//		if ( dbHosts.containsKey( h.getName() ) )
//			return dbHosts.get( h.getName() ); // host already created
//		
//		
//		SizeInMB s_am = h.getInstalledMemory();
//		SizeInMB s_as = h.getInstalledStorage();
//		
//		if ( ( s_am == null ) || ( s_as == null ) )
//			return null; // host doesn't have installed Memory and installed Storage
//		
//		BigInteger am = s_am.getValue();
//		BigInteger as = s_as.getValue();
//		
//		if ( ( am.intValue() < 0 ) || ( as.intValue() < 0 ) )
//			return null; // wrong values for available memory or storage
//		
//		if ( h.getMaxVNFs() < 0 )
//			return null; // wrong value for maxVNFs
//		
//		
//		HostReaderImpl host = new HostReaderImpl();
//		
//		host.setName( h.getName() );
//		host.setMaxVNFs( h.getMaxVNFs() );
//		host.setAvailableMemory( am.intValue() );
//		host.setAvailableStorage( as.intValue() );
//
//		// reference to allocated nodes will be added once all nodes have been created
//		Set<NodeReader> setNodeReader = new HashSet<NodeReader>();
//		host.setNodes( setNodeReader );
//		
//		return host;
//		
//	}
//	
	
//	
//	/**
//	 * Creates a ConnectionPerformanceReader accessible object with data taken
//	 * from the jaxb object passed as parameter.
//	 * 
//	 * @param  c jaxb Connection object
//	 * @return   a ConnectionPerformanceReader accessible object
//	 */
//	private ConnectionPerformanceReaderImpl createConnection( Connection c ) {
//		
//		if ( c == null )
//			return null; // wrong parameter
//		
//		Connection.ConnectionID cID = c.getConnectionID();
//		
//		if ( cID == null )
//			return null; // missing connection ID
//		
//		Throughput throughput = c.getAverageThroughput();
//		if ( throughput == null )
//			return null; // missing average throughput
//		
//		Latency latency = c.getLatency();
//		if ( latency == null )
//			return null; // missing latency
//		
//		if ( ( throughput.getValue() < 0 ) || ( latency.getValue() < 0 ) )
//			return null; // wrong throughput or latency values
//		
//		String sHost = cID.getSourceHost();
//		String dHost = cID.getDestionationHost();
//		
//		if ( ( sHost == null ) || ( dHost == null ) )
//			return null; // missing connection end points
//
//		ConnectionPerformanceReaderImpl connection = 
//				new ConnectionPerformanceReaderImpl(sHost, dHost);
//		
//		connection.setLatency(latency.getValue());
//		connection.setThroughput(throughput.getValue());
//		
//		return connection;
//	}
//	
//	
//	/**
//	 * Creates a VNFTypeReader accessible object with data from
//	 * the jaxb VNF object passed as argument.
//	 * 
//	 * @param  v jaxb VNF object
//	 * @return   a VNFTypeReader accessible object
//	 */
//	private VNFTypeReaderImpl createVNF( VNF v ) {
//		
//		if ( v == null )
//			return null; // invalid argument
//		
//		if ( v.getName() == null )
//			return null; // VNF hasn't got a name
//		
//		if ( ( v.getRequiredMemory() == null ) || ( v.getRequiredStorage() == null ) )
//			return null; // VNF hasn't got required memory or storage
//		
//		if ( ( v.getRequiredMemory().getValue().intValue() < 0 ) || 
//			 ( v.getRequiredStorage().getValue().intValue() < 0 )   )
//			return null; // wrong required memory or storage values
//		
//		if ( v.getFunctionalType() == null )
//			return null; // wrong functional type
//		
//		VNFTypeReaderImpl vnf = new VNFTypeReaderImpl();
//		
//		vnf.setName( v.getName() );
//		vnf.setRequiredMemory( v.getRequiredMemory().getValue().intValue() );
//		vnf.setRequiredStorage( v.getRequiredStorage().getValue().intValue() );
//		vnf.setFunctionctionalType( FunctionalType.fromValue( v.getFunctionalType().value() ) );
//		
//		return vnf;
//	}
//	
//	
//	
//	
//	
//	private NffgReaderImpl createNFFG( NFFG n ) {
//		
//		if ( n == null )
//			return null; // wrong parameter
//		
//		if ( n.getName() == null )
//			return null; // missing nffg name
//		
//		if ( n.getDeployTime() == null )
//			return null; // missing deploy time
//		
//		
//		
//	}
//	
//	
//	
//	
	
	
	/**
	 * Searches for a connection in the infrastructure network between the two
	 * hosts passed as parameters.
	 * 
	 * @param  sourceHost source host end point of connection
	 * @param  destHost   destination host end point of connection
	 * @return            a new ConnectionPerformanceReader interface, null if
	 *                    a connection doesn't exist or an error occurred
	 */
	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader sourceHost, 
																HostReader destHost ) {
		
		if ( sourceHost == null || destHost == null )
			return null; // wrong arguments
		
		String sHost = sourceHost.getName();
		String dHost = destHost.getName();
		
		if ( sHost == null || dHost == null )
			return null; // source or destination hosts didn't have a name
		
		InfrastructureNetwork in = nfvSystem.getIN(); 
		
		if ( in == null )
			return null; // NFV system didn't have a infrastructure network
		
		InfrastructureNetwork.Connections connections = in.getConnections();
		
		if ( connections == null )
			return null; // the infrastructure network doesn't have any connection
		
		
		List<Connection> connections_list = connections.getConnection();
		
		if ( connections_list.size() == 0 )
			return null; // the infrastructure network doesn't have any connection 
		
		/* ------------------------------------------------------------------
		 * find required connection
		 */
		for ( Connection c : connections_list ) {
			 
			if ( sHost.equals( c.getConnectionID().getSourceHost() ) &&
				 dHost.equals( c.getConnectionID().getDestionationHost() ) ) {
				
				Latency    l = c.getLatency();
				Throughput t = c.getAverageThroughput();
				
				if ( ( l == null ) || ( t == null ) )
					return null; // latency of throughput missing
				
				int latency      = l.getValue();
				float throughput = t.getValue();
				
				if ( ( latency < 0 ) || ( throughput < 0.0 )  )
					return null; // wrong values for latency or throughputs
				
				ConnectionPerformanceReaderImpl cpri = 
						new ConnectionPerformanceReaderImpl();
				
				cpri.setLatency( latency );
				cpri.setThroughput( throughput );
				
				return cpri;
			}
		}
		
		return null; // a connection between sourceHost and destHost doesn't exist
	}

	
	
	
	@Override
	public HostReader getHost(String hostName) {
		
		if ( hostName == null )
			return null; // wrong parameter
		
		InfrastructureNetwork in = nfvSystem.getIN();
		
		if ( in == null )
			return null; // NFV system didn't have a infrastructure network
		
		InfrastructureNetwork.Hosts hosts = in.getHosts();
		
		if ( hosts == null )
			return null; // the infrastructure network doesn't have any host
		
		List<Host> hosts_list = hosts.getHost();
		
		if ( hosts_list.size() == 0 )
			return null; // the infrastructure network doesn't have any host
		
		for ( Host h : hosts_list ) {
			
			if ( hostName.equals( h.getName() ) ) {
				
				SizeInMB s_am = h.getInstalledMemory();
				SizeInMB s_as = h.getInstalledStorage();
				
				if ( ( s_am == null ) || ( s_as == null ) )
					return null; // host doesn't have installed Memory and installed Storage
				
				BigInteger am = s_am.getValue();
				BigInteger as = s_as.getValue();
				
				if ( ( am.intValue() < 0 ) || ( as.intValue() < 0 ) )
					return null; // wrong values for available memory or storage
				
				if ( h.getMaxVNFs() < 0 )
					return null; // wrong value for maxVNFs
				
				
				/* ----------------------------------------------------------
				 * get nodes deployed in current host
				 */
				Set<NodeReader> setNodeReader = new HashSet<NodeReader>();
				
				Host.AllocatedNodes allocatedNodes = h.getAllocatedNodes();

				if ( allocatedNodes != null ) {
					
					List<NodeRef> listNodeRef = allocatedNodes.getNode();
					
					if ( listNodeRef.size() >  0 ) {
						
						for ( NodeRef nr : listNodeRef ) {
							
							String nodeName = nr.getName();
							String nffgName = nr.getAssociatedNFFG();
							
							if ( ( nodeName == null ) || ( nffgName == null ) )
								continue; // node name or NFFG name are invalid
								
							NffgReader nffg = this.getNffg(nffgName);
								
							if ( nffg == null )
								continue; // NFFG doesn't exist
									
							NodeReader node = nffg.getNode(nodeName);
									
							if ( node == null ) 			
								continue; // node doesn't exist
							
							setNodeReader.add(node); // success: found node
						}
					}
				}
				
				HostReaderImpl host = new HostReaderImpl();
				
				host.setName( h.getName() );
				host.setMaxVNFs( h.getMaxVNFs() );
				host.setAvailableMemory( am.intValue() );
				host.setAvailableStorage( as.intValue() );
				host.setNodes( setNodeReader );
				return host;
			}
			
		}
		
		return null; // no host exists with name hostName
	}

	@Override
	public Set<HostReader> getHosts() {
		
		InfrastructureNetwork in = nfvSystem.getIN();
		
		if ( in == null )
			return new HashSet<HostReader>(); // NFV system didn't have a infrastructure network
		
		InfrastructureNetwork.Hosts hosts = in.getHosts();
		
		if ( hosts == null )
			return new HashSet<HostReader>(); // the infrastructure network doesn't have any host
		
		List<Host> hosts_list = hosts.getHost();
		
		if ( hosts_list.size() == 0 )
			return new HashSet<HostReader>(); // the infrastructure network doesn't have any host
		
		
		Set<HostReader> setHostReader = new HashSet<HostReader>();
		
		for ( Host h : hosts_list ) {
			
			SizeInMB s_am = h.getInstalledMemory();
			SizeInMB s_as = h.getInstalledStorage();
			
			if ( ( s_am == null ) || ( s_as == null ) )
				continue; // host doesn't have installed Memory and installed Storage
			
			BigInteger am = s_am.getValue();
			BigInteger as = s_as.getValue();
			
			if ( ( am.intValue() < 0 ) || ( as.intValue() < 0 ) )
				continue; // wrong values for available memory or storage
			
			if ( h.getMaxVNFs() < 0 )
				continue; // wrong value for maxVNFs
			
			
			/* ----------------------------------------------------------
			 * get nodes deployed in current host
			 */
			Set<NodeReader> setNodeReader = new HashSet<NodeReader>();
			
			Host.AllocatedNodes allocatedNodes = h.getAllocatedNodes();

			if ( allocatedNodes != null ) {
				
				List<NodeRef> listNodeRef = allocatedNodes.getNode();
				
				if ( listNodeRef.size() > 0 ) {
					
					for ( NodeRef nr : listNodeRef ) {
						
						String nodeName = nr.getName();
						String nffgName = nr.getAssociatedNFFG();
						
						if ( ( nodeName == null ) || ( nffgName == null ) )
							continue; // node name or NFFG name are invalid
							
						NffgReader nffg = this.getNffg(nffgName);
							
						if ( nffg == null )
							continue; // NFFG doesn't exist
								
						NodeReader node = nffg.getNode(nodeName);
								
						if ( node == null ) 			
							continue; // node doesn't exist
						
						setNodeReader.add(node); // success: found node
					}
				}
			}
			
			HostReaderImpl host = new HostReaderImpl();
			
			host.setName( h.getName() );
			host.setMaxVNFs( h.getMaxVNFs() );
			host.setAvailableMemory( am.intValue() );
			host.setAvailableStorage( as.intValue() );
			host.setNodes( setNodeReader );
			
			setHostReader.add(host);
			
		}
		
		return setHostReader;
	}

	@Override
	public NffgReader getNffg(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<NffgReader> getNffgs(Calendar arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

}
