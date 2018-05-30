package it.polito.dp2.NFV.sol2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

public class ReachabilityTesterReal implements ReachabilityTester {
	
	private final static String PROPERTY_NAME_NAME            = "name";
	
	private final static String RELATIONSHIP_FORWARDSTO_TYPE  = "ForwardsTo";
	private final static String RELATIONSHIP_ALLOCATEDON_TYPE = "AllocatedOn";
	
	private final static String NODE_LABEL = "Node";
	private final static String HOST_LABEL = "Host";
	
	private final NfvReader         monitor; // Access to NFV System interfaces
	private final ObjectFactory     of;      // WADL/JAXB object factory
	private final Neo4jSimpleWebAPI neo4jWS; // (my) Neo4j WebService API			
	
	private final Set<String> loadedNFFGs;
	private final IDsMappingService map;
	
	
	protected ReachabilityTesterReal() 
			throws NfvReaderException, Exception {
		
		try {

			NfvReaderFactory factory = NfvReaderFactory.newInstance();
		
			monitor = factory.newNfvReader();
			of      = new ObjectFactory();
			neo4jWS = Neo4jSimpleWebAPI.newInstance();
		
		} catch ( FactoryConfigurationError fce ) {
			throw new Exception(fce.getMessage());
		} 
		
		map           = new IDsMappingService();		
		loadedNFFGs   = new HashSet<String>();
	}
	
	
	
	
	@Override
	public void loadGraph( String nffgName )
			throws UnknownNameException, AlreadyLoadedException, ServiceException {
		
		
		if ( nffgName == null )
			throw new UnknownNameException("Null argument");

		if ( monitor.getNffg(nffgName) == null )
			throw new UnknownNameException();

		if ( isLoaded( nffgName ) )
			throw new AlreadyLoadedException();
		
		map.newNffgLinkToRelMapping( nffgName ); // init relationsihpID mappings for this NFFG

		Set<LinkReader> setOFLinkInterfaces = new HashSet<LinkReader>();
		
		try { 
			
			/* for each node in the NFFG:
			 * 
			 * 1. new graph node from this NFFG node 
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
			
			// create GraphNodes from NFFG's nodes and hosting hosts
			for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
				
				// 1.a.
				Node nodeXMLNode = createXMLNodeFromNodeReader( nodeI );				
				
				// 1.b.
				String nodeGraphNodeID = neo4jWS.createGraphNode( nodeXMLNode );
				neo4jWS.createNodeLabel( nodeGraphNodeID, nodeXMLNode.getLabels() );
				
				// 1.c.
				map.addNode( nodeGraphNodeID, nodeI.getName() );
				
				
				// 2
				HostReader hostI           = nodeI.getHost();
				String     hostGraphNodeID = null; 
				
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
				
				// 3.a.
				Relationship allocatedOnRelationship = 
						createXMLAllocatedOnRel( nodeGraphNodeID, hostGraphNodeID );
				
				// 3.b.
				String allocatedOnRelationshipID = 
						neo4jWS.createNodeRelationship( nodeGraphNodeID, allocatedOnRelationship );
			
				// 3.c.
				map.addLink(nffgName, 
						    new String( nodeI.getName() + "-" + hostI.getName() ), 
						    allocatedOnRelationshipID);

				
				// 3.d. 
				setOFLinkInterfaces.addAll( nodeI.getLinks() );
			}
			
			
			 /* 
			 * 4. for each link in the NFFG create a relationship on the web
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
				String srcNodeName = linkI.getSourceNode().getName();
				String dstNodeName = linkI.getDestinationNode().getName();
				
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
			
		} catch ( WebApplicationException | ProcessingException e ) {
			throw new ServiceException();
		} catch ( Exception e ) {
			throw new ServiceException();
		}
		
		// remember loaded NFFGs
		loadedNFFGs.add( nffgName );
	}

	

	@Override
	public Set<ExtendedNodeReader> getExtendedNodes( String nffgName )
			throws UnknownNameException, NoGraphException, ServiceException {
		
		if ( nffgName == null )
			throw new UnknownNameException();
		
		if ( monitor.getNffg( nffgName ) == null )
			throw new UnknownNameException();
		
		if ( !( isLoaded( nffgName ) ) )
			throw new NoGraphException();
		
		
		Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();
		
		for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
			
			Nodes xmlNodes = null;
			try {
				
				String nodeGraphNodeID = map.getGraphNodeIDFromNodeName( nodeI.getName() );
	
				Set<String> types = new HashSet<String>();
				
				xmlNodes = neo4jWS.getReacheableNodesFromNode( nodeGraphNodeID, types, HOST_LABEL );
				
			} catch ( WebApplicationException | ProcessingException e ) {
				throw new ServiceException();
			} catch ( Exception e ) {
				throw new ServiceException();
			}
			
			
			Set<HostReader> setOfHostReaders = new HashSet<HostReader>();
			
			for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {
				
				String nodeName = null;
				for ( Property property : xmlNode.getProperties().getProperty() )
					if ( property.getName().compareTo( PROPERTY_NAME_NAME ) == 0 ) {
						nodeName = property.getValue();
						break;
					}
				
				if ( nodeName != null ) {
					HostReader reachableHostI = monitor.getHost(nodeName);
					setOfHostReaders.add( reachableHostI );
				}
			}
			
			ExtendedNodeReaderReal extendedNode = new ExtendedNodeReaderReal( nodeI, setOfHostReaders );
			result.add( extendedNode );
		}
		
		return result;
	}

	
	
	@Override
	public boolean isLoaded(String nffgName) 
			throws UnknownNameException {
		
		if ( nffgName == null )
			throw new UnknownNameException();
		
		if ( monitor.getNffg( nffgName ) == null )
			throw new UnknownNameException();
		
		if ( loadedNFFGs.contains( nffgName ) )
			return true;
		
		return false;
	}
	
	
	
	private Node createXMLNodeFromNodeReader( NodeReader nodeInterface ) {
		
		if ( nodeInterface == null )
			throw new NullPointerException();
		
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
	
	
	private Node createXMLNodeFromHostReader( HostReader hostInterface ) {
		
		if ( hostInterface == null )
			throw new NullPointerException("hostInterface");
		
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
	
	private Relationship createXMLAllocatedOnRel( String nodeGraphNodeID, String hostGraphNodeID ) {
		
		if ( ( nodeGraphNodeID == null ) || ( hostGraphNodeID == null ) )
			throw new NullPointerException();
		
		Relationship relationship = of.createRelationship();
		relationship.setSrcNode( nodeGraphNodeID );
		relationship.setDstNode( hostGraphNodeID );
		relationship.setType( RELATIONSHIP_ALLOCATEDON_TYPE );
		
		return relationship;
	}
	
	private Relationship createXMLForwardToRel( String srcGraphNodeID, String dstGraphNodeID ) {
		
		if ( ( srcGraphNodeID == null ) || ( dstGraphNodeID == null ) )
			throw new NullPointerException();
		
		Relationship relationship = of.createRelationship();
		relationship.setSrcNode( srcGraphNodeID );
		relationship.setDstNode( dstGraphNodeID );
		relationship.setType( RELATIONSHIP_FORWARDSTO_TYPE );
		
		return relationship;
		
	}

}
