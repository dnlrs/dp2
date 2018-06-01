package it.polito.dp2.NFV.sol2;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab2.AlreadyLoadedException;
import it.polito.dp2.NFV.lab2.ExtendedNodeReader;
import it.polito.dp2.NFV.lab2.NoGraphException;
import it.polito.dp2.NFV.lab2.ReachabilityTester;
import it.polito.dp2.NFV.lab2.ServiceException;
import it.polito.dp2.NFV.lab2.UnknownNameException;

/**
 * An implementation of the {@link ReachabilityTester} interface.
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class ReachabilityTesterReal implements ReachabilityTester {
	
	private final PrintStream debug = new PrintStream( "debug.txt" );
//	private final static boolean SEQUENTIAL = true;
	private final static boolean SEQUENTIAL = false;
	
	private final static int    SLEEPING_TIME_MS              = 100; // experimental, avoid "aggressive polling"
	private final static String PROPERTY_NAME_NAME            = "name";
	
	private final static String RELATIONSHIP_FORWARDSTO_TYPE  = "ForwardsTo";
	private final static String RELATIONSHIP_ALLOCATEDON_TYPE = "AllocatedOn";
	
	private final static String NODE_LABEL = "Node";
	private final static String HOST_LABEL = "Host";
	
	
	private final NfvReader         monitor; // Access to NFV System interfaces
	private final Neo4jSimpleWebAPI neo4jWS; // (my) Neo4j WebService API			
	private final IDsMappingService map;     // keeps mapping with graph nodes IDs
	private final ObjectFactory     of;      // WADL/JAXB object factory
	
	private final Set<String> loadedNFFGs;   // keeps trace of loaded NFFGs
	
	
	protected ReachabilityTesterReal() 
			throws NfvReaderException, Exception {
		
		try {

			NfvReaderFactory factory = NfvReaderFactory.newInstance();
		
			monitor = factory.newNfvReader();
			of      = new ObjectFactory();
			neo4jWS = Neo4jSimpleWebAPI.newInstance();
		
		} catch ( FactoryConfigurationError fce ) {
			throw new Exception( fce.getMessage() );
		} 
		
		map         = new IDsMappingService();		
		loadedNFFGs = new HashSet<String>();
	}
	
	
	
	@Override
	public void loadGraph( String nffgName )
			throws UnknownNameException, AlreadyLoadedException, ServiceException {
		
		
		if ( nffgName == null )
			throw new UnknownNameException( "loadGraph: null argument" );

		if ( monitor.getNffg( nffgName ) == null )
			throw new UnknownNameException( "loadGraph: inexistent NFFG" );

		if ( isLoaded( nffgName ) )
			throw new AlreadyLoadedException( "loadGraph: NFFG already loaded" );
		
		map.newNffgLinkToRelMapping( nffgName ); // init relationsihpID mappings for this NFFG

		Set<LinkReader> setOFLinkInterfaces = new HashSet<LinkReader>();
		
		try {
			/* 1. for each node in the NFFG:
			 * 1.a. Prepare an XML Node with data taken from the node interface
			 * 1.b. Ask the web service to create a new graph node according to
			 *      the XML Node
			 * 1.c. Save the returned graph node id 
			 * 
			 * 2. new graph node from this NFFG node's hosting host
			 * 2.a. Prepare an XML Node with data taken from the host hosting
			 *      the current nodeI
			 * 2.b. Ask the web service to create a new graph node according to
			 *      the XML Node
			 * 2.c. Save the returned graph node id
			 * 2.d. If there already was a graph node for this host, simply 
			 *      retrieve its id
			 *      
			 * 3. create relationship between node and host
			 * 3.a. Prepare an XML Relationship with necessary data
			 * 3.b. Ask web service to create a new relationship between the
			 *      graph nodes
			 * 3.c. Save the returned relationship id
			 * 3.d. Save all links found in the NFFG
			 */
			neo4jWS.newClient();
			
			for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
				debug.println("node: "+nodeI.getName());
				
				// 1.a.
				Node nodeXMLNode = createXMLNodeFromNodeReader( nodeI ); // 1.a	
				// 1.b.
				String nodeGraphNodeID = neo4jWS.createGraphNode( nodeXMLNode );
				neo4jWS.createNodeLabel( nodeGraphNodeID, nodeXMLNode.getLabels() );
				// 1.c.
				map.addNode( nodeGraphNodeID, nodeI.getName() );
				
				// 2
				HostReader hostI       = nodeI.getHost();
				String hostGraphNodeID = null; 
				if ( map.hostNameIsPresent( hostI.getName() ) ) {
					// 2.d.
					hostGraphNodeID = map.getGraphNodeIDFromHostName( hostI.getName() );
				} else {
					// 2.a.
					Node hostXMLNode = createXMLNodeFromHostReader( hostI );
					// 2.b.
					hostGraphNodeID  = neo4jWS.createGraphNode( hostXMLNode );
					neo4jWS.createNodeLabel( hostGraphNodeID, hostXMLNode.getLabels() );
					// 2.c.
					map.addHost( hostGraphNodeID, hostI.getName() );
				}
				
				// 3. / 3.a.
				Relationship allocatedOnRelationship = 
						createXMLAllocatedOnRel( nodeGraphNodeID, hostGraphNodeID );
				// 3.b.
				String allocatedOnRelationshipID = 
						neo4jWS.createNodeRelationship( nodeGraphNodeID, allocatedOnRelationship );
				// 3.c.
				map.addLink(nffgName, 
						    new String( nodeI.getName() + "-" + hostI.getName() ), /* relationship local name "nodeName-hostName" */
						    allocatedOnRelationshipID);
				// 3.d.
				setOFLinkInterfaces.addAll( nodeI.getLinks() );
				
				for ( LinkReader linkI : nodeI.getLinks() )
					debug.println("    link: "+ linkI.getName() + " [ to "+linkI.getDestinationNode().getName() + " ]");
				
			}

			/* 4. for each link in the NFFG create a relationship on the web
			 *    service
			 * 4.a. Retrieve the source graph node and the destination graph
			 *      node of each link according to its source NFFG node and
			 *      destination NFFG node
			 * 4.b. Create an XML Relationship with necessary data
			 * 4.c. Ask web service to create a new relationship between
			 *      the graph nodes
			 * 4.d. Save the relationship id
			 */    
			for ( LinkReader linkI : setOFLinkInterfaces ) {
				// 4.a.
				String srcNodeName    = linkI.getSourceNode().getName();
				String dstNodeName    = linkI.getDestinationNode().getName();
				String srcGraphNodeID = map.getGraphNodeIDFromNodeName( srcNodeName );
				String dstGraphNodeID = map.getGraphNodeIDFromNodeName( dstNodeName );
				// 4.b.
				Relationship forwardsToRelationship = 
						createXMLForwardToRel( srcGraphNodeID, dstGraphNodeID );
				// 4.c.
				String forwardsToRelationshipID = 
						neo4jWS.createNodeRelationship( srcGraphNodeID, forwardsToRelationship);
				// 4.d.
				map.addLink( nffgName, linkI.getName(), forwardsToRelationshipID );
			}

			neo4jWS.closeClient();
			
		} catch ( WebApplicationException | ProcessingException e ) {
			throw new ServiceException( e.getMessage() );
		} catch ( Exception e ) {
			throw new ServiceException( e.getMessage() );
		}
		
		// remember loaded NFFGs
		loadedNFFGs.add( nffgName );
	}

	
	@Override
	public Set<ExtendedNodeReader> getExtendedNodes( String nffgName )
			throws UnknownNameException, NoGraphException, ServiceException {
		
		if ( SEQUENTIAL ) {

			return syncGetExtendedNodes( nffgName );
			
		} else {
				
			return asyncGetExtendedNodes( nffgName );
		}
		
	}
	
	
	
	
	private Set<ExtendedNodeReader> syncGetExtendedNodes( String nffgName )
			throws UnknownNameException, NoGraphException, ServiceException {
	
		if ( nffgName == null )
			throw new UnknownNameException( "getExtendedNodes: null argument" );
		
		if ( monitor.getNffg( nffgName ) == null )
			throw new UnknownNameException( "getExtendedNodes: inexistent NFFG" );
		
		if ( !( isLoaded( nffgName ) ) )
			throw new NoGraphException( "getExtendedNodes: NFFG is not loaded" );
		
		Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();
		
		neo4jWS.newClient();
		
		for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
			
			debug.print("getting reachable hosts for node " + nodeI.getName()+"...");
			
			try {
				
				String nodeGraphNodeID = map.getGraphNodeIDFromNodeName( nodeI.getName() );
				
				Set<String> types = new HashSet<String>();
				types.add( RELATIONSHIP_FORWARDSTO_TYPE );
				types.add( RELATIONSHIP_ALLOCATEDON_TYPE );
				
				Nodes xmlNodes = neo4jWS.getReacheableNodesFromNodeS( nodeGraphNodeID, 
//								                                     new HashSet<String>(), /* types */
																	 types,
								                                     HOST_LABEL );
			
				Set<HostReader> setOfHostReaders = new HashSet<HostReader>();
				for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {
					
					String nodeName = null;
					for ( Property property : xmlNode.getProperties().getProperty() )
						if ( property.getName().compareTo( PROPERTY_NAME_NAME ) == 0 ) {
							nodeName = property.getValue();
							break;
						}
					
					if ( nodeName != null ) {
						HostReader reachableHostI = monitor.getHost( nodeName );
						setOfHostReaders.add( reachableHostI );
					} else {
						throw new ServiceException( "getExtendedNodes: \"name\" property not found" );
					}
				}
				
				ExtendedNodeReaderReal extendedNode = 
						new ExtendedNodeReaderReal( nodeI, setOfHostReaders );
				result.add( extendedNode );

			} catch ( WebApplicationException | ProcessingException e ) {
				throw new ServiceException( e.getMessage() );
			} catch ( Exception e ) {
				throw new ServiceException( e.getMessage() );
			}
			
			debug.println("done.");
			
		}
		
		neo4jWS.closeClient();
		
		return result;
		
	}
	
	
	private Set<ExtendedNodeReader> asyncGetExtendedNodes( String nffgName )
			throws UnknownNameException, NoGraphException, ServiceException {
		

		if ( nffgName == null )
		throw new UnknownNameException( "getExtendedNodes: null argument" );
	
		if ( monitor.getNffg( nffgName ) == null )
			throw new UnknownNameException( "getExtendedNodes: inexistent NFFG" );
		
		if ( !( isLoaded( nffgName ) ) )
			throw new NoGraphException( "getExtendedNodes: NFFG is not loaded" );
		
		neo4jWS.newClient();
		
		ConcurrentMap<NodeReader, Future<Nodes>> mapOfFutureNodes = 
				new ConcurrentHashMap<NodeReader, Future<Nodes>>();
		
		// for each node request to the web service the reachable hosts
		for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {

			String nodeGraphNodeID = map.getGraphNodeIDFromNodeName( nodeI.getName() );
			try {
				
				Future<Nodes> futureNodes = 
						neo4jWS.getReacheableNodesFromNode( nodeGraphNodeID, 
								new HashSet<String>(), /* types */ 
								HOST_LABEL );
						
				mapOfFutureNodes.put( nodeI, futureNodes );
			
			} catch ( Exception e ) {
				throw new ServiceException( e.getMessage() );
			}
		}
		
		
		Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();
		// get ready results for every node
		while ( !( mapOfFutureNodes.isEmpty() ) ) {
			
			for ( NodeReader nodeI : mapOfFutureNodes.keySet() ) {
				
				Future<Nodes> futureNodes = mapOfFutureNodes.get( nodeI );
				
				if ( futureNodes.isCancelled() )
					throw new ServiceException( "getExtendedNodes: request was cancelled" );
				
				if ( !( futureNodes.isDone() ) ) {
					continue; // this node is not yet completed
				}
				debug.print("DEBUG: node \"" + nodeI.getName() + "\" is ");
				debug.println("ready");
				
				try {
					
					Nodes xmlNodes                   = futureNodes.get();	
					Set<HostReader> setOfHostReaders = new HashSet<HostReader>();
					
					for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {
						
						boolean propertyFound = false;
						for ( Property property : xmlNode.getProperties().getProperty() )
							if ( property.getName().compareTo( PROPERTY_NAME_NAME ) == 0 ) {
								setOfHostReaders.add( monitor.getHost( property.getValue() ) );
								propertyFound = true;
								break;
							}
						if ( !propertyFound )
							throw new ServiceException( "getExtendedNodes: \"name\" property not found" );
					}
					
					result.add( new ExtendedNodeReaderReal( nodeI, setOfHostReaders ) );
					mapOfFutureNodes.remove( nodeI );
					
				} catch ( Exception e ) {
					throw new ServiceException( e.getMessage() );
				}
				
			}
			
//			try {
//				Thread.sleep( SLEEPING_TIME_MS );
//			} catch (InterruptedException e ) {}
		}
		
		neo4jWS.closeClient();
		
		return result;
	}
	
	

	// Synchronous method, for each node client waits for result
//	@Override
//	public Set<ExtendedNodeReader> getExtendedNodes( String nffgName )
//			throws UnknownNameException, NoGraphException, ServiceException {
//		
//		if ( nffgName == null )
//			throw new UnknownNameException( "getExtendedNodes: null argument" );
//		
//		if ( monitor.getNffg( nffgName ) == null )
//			throw new UnknownNameException( "getExtendedNodes: inexistent NFFG" );
//		
//		if ( !( isLoaded( nffgName ) ) )
//			throw new NoGraphException( "getExtendedNodes: NFFG is not loaded" );
//		
//		Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();
//
//		for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
//			
//			try {
//				
//				String nodeGraphNodeID = map.getGraphNodeIDFromNodeName( nodeI.getName() );
//	
//				Nodes xmlNodes = neo4jWS.getReacheableNodesFromNode( nodeGraphNodeID, 
//								                                     new HashSet<String>(), /* types */
//								                                     HOST_LABEL );
//			
//				Set<HostReader> setOfHostReaders = new HashSet<HostReader>();
//				for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {
//					
//					String nodeName = null;
//					for ( Property property : xmlNode.getProperties().getProperty() )
//						if ( property.getName().compareTo( PROPERTY_NAME_NAME ) == 0 ) {
//							nodeName = property.getValue();
//							break;
//						}
//					
//					if ( nodeName != null ) {
//						HostReader reachableHostI = monitor.getHost( nodeName );
//						setOfHostReaders.add( reachableHostI );
//					} else {
//						throw new ServiceException( "getExtendedNodes: \"name\" property not found" );
//					}
//				}
//				
//				ExtendedNodeReaderReal extendedNode = 
//						new ExtendedNodeReaderReal( nodeI, setOfHostReaders );
//				result.add( extendedNode );
//
//			} catch ( WebApplicationException | ProcessingException e ) {
//				throw new ServiceException( e.getMessage() );
//			} catch ( Exception e ) {
//				throw new ServiceException( e.getMessage() );
//			}
//			
//		}
//		
//		return result;
//	}

	
	
	@Override
	public boolean isLoaded( String nffgName ) 
			throws UnknownNameException {
		
		if ( nffgName == null )
			throw new UnknownNameException( "isLoaded: null argument" );
		
		if ( monitor.getNffg( nffgName ) == null )
			throw new UnknownNameException( "isLoaded: inexistent NFFG" );
		
		if ( loadedNFFGs.contains( nffgName ) )
			return true;
		
		return false;
	}
	
	
	
	private Node createXMLNodeFromNodeReader( NodeReader nodeInterface ) 
			throws NullPointerException, Exception {
		
		if ( nodeInterface == null )
			throw new NullPointerException( "createXMLNodeFromNodeReader: null argument" );
		
		Property nodeProperty = of.createProperty();
		nodeProperty.setName( PROPERTY_NAME_NAME );
		nodeProperty.setValue( nodeInterface.getName() );
		
		Properties nodeProperties = of.createProperties();
		nodeProperties.getProperty().add( nodeProperty );
		
		Labels nodeLabels = of.createLabels();
		nodeLabels.getLabel().add( NODE_LABEL );
		
		Node xmlNode = of.createNode();
		xmlNode.setProperties( nodeProperties );
		xmlNode.setLabels( nodeLabels );
		
		return xmlNode;
	}
	
	
	private Node createXMLNodeFromHostReader( HostReader hostInterface ) 
			throws NullPointerException, Exception {
		
		if ( hostInterface == null )
			throw new NullPointerException( "createXMLNodeFromHostReader: null argument" );
		
		Property hostProperty = of.createProperty();
		hostProperty.setName( PROPERTY_NAME_NAME );
		hostProperty.setValue( hostInterface.getName() );
		
		Properties hostProperties = of.createProperties();
		hostProperties.getProperty().add( hostProperty );
		
		Labels hostLabels = of.createLabels();
		hostLabels.getLabel().add( HOST_LABEL );
		
		Node xmlNode = of.createNode();
		xmlNode.setProperties( hostProperties );
		xmlNode.setLabels( hostLabels );
		
		return xmlNode;
	}
	
	private Relationship createXMLAllocatedOnRel( String nodeGraphNodeID, String hostGraphNodeID ) 
			throws NullPointerException {
		
		if ( ( nodeGraphNodeID == null ) || ( hostGraphNodeID == null ) )
			throw new NullPointerException( "createXMLAllocatedOnRel: null argument" );
		
		Relationship relationship = of.createRelationship();
		relationship.setSrcNode( nodeGraphNodeID );
		relationship.setDstNode( hostGraphNodeID );
		relationship.setType( RELATIONSHIP_ALLOCATEDON_TYPE );
		
		return relationship;
	}
	
	private Relationship createXMLForwardToRel( String srcGraphNodeID, String dstGraphNodeID ) 
			throws NullPointerException {
		
		if ( ( srcGraphNodeID == null ) || ( dstGraphNodeID == null ) )
			throw new NullPointerException( "createXMLForwardToRel: null argument" );
		
		Relationship relationship = of.createRelationship();
		relationship.setSrcNode( srcGraphNodeID );
		relationship.setDstNode( dstGraphNodeID );
		relationship.setType( RELATIONSHIP_FORWARDSTO_TYPE );
		
		return relationship;
	}

}
