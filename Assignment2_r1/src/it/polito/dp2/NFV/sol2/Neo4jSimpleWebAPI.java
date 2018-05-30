package it.polito.dp2.NFV.sol2;

import java.net.URI;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;


/**
 * A singleton class that provides the necessary APIs to access 
 * Neo4JSimpleXML RESTful Web Service.
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class Neo4jSimpleWebAPI {
	
	private final static String URI_NODE                        = "data/node";
	private final static String URI_NODE_id                     = "data/node/{node_id}";
	private final static String URI_NODE_id_LABELS              = "data/node/{node_id}/labels";
	private final static String URI_NODE_id_LABELS_label        = "data/node/{node_id}/labels/{label}";
	private final static String URI_NODE_id_PROPERTIES          = "data/node/{node_id}/properties";
	private final static String URI_NODE_id_PROPERTIES_property = "data/node/{node_id}/properties/{property_name}";
	private final static String URI_NODE_id_RELATIONSHIPS       = "data/node/{node_id}/relationships";
	private final static String URI_NODE_id_RELATIONSHIPS_ALL   = "data/node/{node_id}/relationships/all";
	private final static String URI_NODE_id_RELATIONSHIPS_IN    = "data/node/{node_id}/relationships/in";
	private final static String URI_NODE_id_RELATIONSHIPS_OUT   = "data/node/{node_id}/relationships/out";
//	private final static String URI_RELATIONSHIP                = "data/relationship";
	private final static String URI_RELATIONSHIP_id             = "data/relationship/{relationship_id}";
	private final static String URI_NODE_id_REACHABLENODES      = "data/node/{node_id}/reachableNodes";
	
	private final static String URI_QP_RELATIONSHIPTYPES        = "relationshipTypes";
	private final static String URI_QP_NODELABELS               = "nodeLabel";
	
	private final static String PROPERTY_URL                    = "it.polito.dp2.NFV.lab2.URL";
	
	private final UriBuilder _uriBuilder;
	private final URI        BASE_URI;
	
	private static Neo4jSimpleWebAPI instance = null;
	
	
	/**
	 * Creates a {@link Neo4jSimpleWebAPI} singleton instance.
	 * 
	 * @return a {@link Neo4jSimpleWebAPI} object
	 * @throws Exception
	 */
	protected static Neo4jSimpleWebAPI newInstance() throws Exception {
		
		if ( Neo4jSimpleWebAPI.instance != null )
			return Neo4jSimpleWebAPI.instance;
		
		Neo4jSimpleWebAPI.instance = new Neo4jSimpleWebAPI();
		
		return Neo4jSimpleWebAPI.instance;
		
	}
	
	/**
	 * Private constructor.
	 * 
	 * @throws Exception
	 */
	private Neo4jSimpleWebAPI() throws Exception {
		
		try {
			String url = System.getProperty( PROPERTY_URL );
			
			if ( url == null )
				throw new NullPointerException( "URL System variable not found" );
			
			BASE_URI = URI.create( url );
			_uriBuilder = UriBuilder.fromUri( BASE_URI );
			
			
		} catch ( Exception e ) {
			throw new Exception( e.getMessage() );
		}
		
	}
	
	
	
	
	// ======================================================================
	// NODES
	// ======================================================================
	
	
	/**
	 * Creates a new GraphNode (note: with properties only) resource.
	 * <p>
	 * POST ...{@value #URI_NODE}
	 * 
	 * @param  node the {@link Node} with the GraphNode data
	 * @return      the GraphNode ID of the resource
	 * 
	 * @throws Exception
	 */
	protected String createGraphNode( Node node ) 
			throws Exception {
		
		if ( node == null )
			throw new NullPointerException( "createGraphNode: null argument" );
		
		URI localURI = _uriBuilder.clone().path( URI_NODE ).build();
		
		Client _client = ClientBuilder.newClient();
		Node n = _client.target( localURI )
				        .request( MediaType.APPLICATION_XML )
				        .post( Entity.entity( node, MediaType.APPLICATION_XML ),
				        	   Node.class );
		_client.close();
		
		return n.getId();
	}
	
	
	/**
	 * Retrieves a GraphNode.
	 * 
	 * <p> GET ...{@value #URI_NODE_id} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @return             a {@link Node} object with the GraphNode data
	 * @throws Exception
	 */
	protected Node getNode( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "getNode: null argument" );
		
		URI localURI = _uriBuilder.clone().path( URI_NODE_id ).build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Node n = _client.target( localURI )
				        .request( MediaType.APPLICATION_XML )
				        .get( Node.class );
		_client.close();
		
		return n;
	}
	
	
	/**
	 * Deletes a GraphNode.
	 * 
	 * <p> DELETE  ...{@value #URI_NODE_id} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @throws Exception
	 */
	protected void deleteNode( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "deleteNode: null argument" );
		
		URI localURI = _uriBuilder.clone().path( URI_NODE_id ).build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
	                               .request( MediaType.APPLICATION_XML )
	                               .delete();
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "deleteNode: node delete error" );
	}
	
	
	
	
	// ======================================================================
	// PROPERTIES
	// ======================================================================

	
	/**
	 * Updates the properties of a GraphNode.
	 * 
	 * <p> PUT ...{@value #URI_NODE_id_PROPERTIES}</p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @param  properties  {@link Properties} object with updated data
	 * @throws Exception
	 */
	protected void updateNodeProperties( String graphNodeID, Properties properties) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( properties == null ) )
			throw new NullPointerException( "updateNodeProperties: null argument" );

		URI localURI = _uriBuilder.clone().path( URI_NODE_id_PROPERTIES ).build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
		                           .request( MediaType.APPLICATION_XML )
                                   .put( Entity.entity( properties, MediaType.APPLICATION_XML ) );
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "updateNodeProperties: PUT error" );
	}

	
	/**
	 * Retrieves all properties of a GraphNode.
	 * 
	 * <p> GET ...{@value #URI_NODE_id_PROPERTIES} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @return             a {@link Properties} object with GraphNode properties
	 * @throws Exception
	 */
	protected Properties getNodeProperties( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "getNodeProperties: null argument" );
		
		URI localURI = _uriBuilder.clone().path( URI_NODE_id_PROPERTIES ).build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Properties properties = _client.target( localURI )
                                       .request( MediaType.APPLICATION_XML )
                                       .get( Properties.class );
		_client.close();
		
		return properties;
	}

	
	/**
	 * Deletes GraphNode's properties.
	 * 
	 * <p> DELETE ...{@value #URI_NODE_id_PROPERTIES} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @throws Exception
	 */
	protected void deleteNodeProperties( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "deleteNodeProperties: null argument" );
		
		URI localURI = _uriBuilder.clone().path( URI_NODE_id_PROPERTIES ).build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
                                   .request( MediaType.APPLICATION_XML )
                                   .delete();
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "deleteNodeProperties: DELETE error" );
	}
	
	
	/**
	 * Updates a specific GraphNode's property.
	 * 
	 * <p> PUT ...{@value #URI_NODE_id_PROPERTIES_property} </p>
	 * 
	 * @param graphNodeID the GraphNode ID
	 * @param property    {@link Property} object with updated data
	 * @throws Exception
	 */
	protected void updateNodeProperty( String graphNodeID, Property property ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( property == null ) )
			throw new NullPointerException( "updateNodeProperty: null argument" );

		URI localURI = _uriBuilder.clone()
				                  .path( URI_NODE_id_PROPERTIES_property )
				                  .build( graphNodeID, property.getName() );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
				                   .request( MediaType.APPLICATION_XML )
				                   .put( Entity.entity( property, MediaType.APPLICATION_XML ) );
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "updateNodeProperty: PUT error" );
	}
	
	
	/**
	 * Retrieves a specific GraphNode property.
	 * 
	 * <p> GET ...{@value #URI_NODE_id_PROPERTIES_property} </p>
	 * 
	 * @param graphNodeID   GraphNode ID
	 * @param property_name the property name
	 * @return              the GraphNode's {@link Property}
	 * @throws Exception
	 */
	protected Property getNodeProperty( String graphNodeID, String property_name ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( property_name == null ) )
			throw new NullPointerException( "getNodeProperty: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_PROPERTIES_property )
                                  .build( graphNodeID, property_name );
		
		Client _client = ClientBuilder.newClient();
		Property property =  _client.target( localURI )
                                    .request( MediaType.APPLICATION_XML )
                                    .get( Property.class );
		_client.close();
		
		return property;
	}
	
	
	/**
	 * Deletes a specific GraphNode property.
	 * 
	 * <p> DELETE ...{@value #URI_NODE_id_PROPERTIES_property} </p>
	 * 
	 * @param  graphNodeID   the GraphNode ID
	 * @param  property_name the property name
	 * @throws Exception
	 */
	protected void deleteNodeProperty( String graphNodeID, String property_name ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( property_name == null ) )
			throw new NullPointerException( "deleteNodeProperty: null argument" );

		URI localURI = _uriBuilder.clone()
				                  .path( URI_NODE_id_PROPERTIES_property )
				                  .build( graphNodeID, property_name );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
                                   .request( MediaType.APPLICATION_XML )
                                   .delete();
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "deleteNodeProperty: DELETE error" );
	}
	

	
	
	// ======================================================================
	// LABELS
	// ======================================================================

	
	/**
	 * Creates a GraphNode label.
	 * 
	 * <p> POST ...{@value #URI_NODE_id_LABELS} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @param  label       a {@link Labels} object with the new label data
	 * @throws Exception
	 */
	protected void createNodeLabel( String graphNodeID, Labels label ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( label == null ) )
			throw new NullPointerException( "createNodeLabel: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_LABELS )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Response r = _client.target( localURI )
							.request( MediaType.APPLICATION_XML )
	                        .post( Entity.entity( label, MediaType.APPLICATION_XML ) );
		_client.close();
		
		if ( r.getStatus() >= 400 )
			throw new WebApplicationException( "createNodeLabel: POST error" );
	}
	
	
	/**
	 * Retrieves all GraphNode's labels.
	 * 
	 * <p> GET ...{@value #URI_NODE_id_LABELS} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @return             a {@link Labels} object with all GraphNode's labels
	 * @throws Exception
	 */
	protected Labels getNodeLabels( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "getNodeLabels: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_LABELS )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Labels labels = _client.target( localURI )
                               .request( MediaType.APPLICATION_XML )
                               .get( Labels.class );
		_client.close();
		
		return labels;
	}
	
	
	/**
	 * Updates all the GraphNode labels.
	 * 
	 * <p> PUT ...{@value #URI_NODE_id_LABELS} </p>
	 *  
	 * @param  graphNodeID the GraphNode ID
	 * @param  label       a {@link Labels} object with all the new labels
	 * @throws Exception
	 */
	protected void updateNodeLabels( String graphNodeID, Labels label ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( label == null ) )
			throw new NullPointerException( "updateNodeLabels: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_LABELS )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Response r =  _client.target( localURI )
                             .request( MediaType.APPLICATION_XML )
                             .put( Entity.entity( label, MediaType.APPLICATION_XML ) );
		_client.close();
		
		if ( r.getStatus() >= 400 )
			throw new WebApplicationException( "updateNodeLabels: POST error" );
	}
	
	
	/**
	 * Deletes a specific GraphNode label.
	 * 
	 * <p> DELETE ...{@value #URI_NODE_id_LABELS_label} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @param  label_name  the label name
	 * @throws Exception
	 */
	protected void deleteNodeLabel( String graphNodeID, String label_name ) 
			throws Exception {
	
		if ( ( graphNodeID == null ) || ( label_name == null ) )
			throw new NullPointerException( "deleteNodeLabel: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_LABELS_label )
                                  .build( graphNodeID, label_name );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
                                   .request( MediaType.APPLICATION_XML )
                                   .delete();
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "deleteNodeLabel: DELETE error" );
	}
	
	
	
	
	// ======================================================================
	// RELATIONSHIPS
	// ======================================================================
	
	
	/**
	 * Adds a relationship to a GraphNode.
	 * 
	 * <p> POST ...{@value #URI_NODE_id_RELATIONSHIPS} </p>
	 * 
	 * @param  graphNodeID  the GraphNode ID
	 * @param  relationship a {@link Relationship} object with relationship's data
	 * @return              the ID of the new GraphNode relationship
	 * @throws Exception
	 */
	protected String createNodeRelationship( String graphNodeID, Relationship relationship ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( relationship == null ) )
			throw new NullPointerException( "createNodeRelationship: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_RELATIONSHIPS )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Relationship r = _client.target( localURI )
	                            .request( MediaType.APPLICATION_XML )
	                            .post( Entity.entity( relationship, MediaType.APPLICATION_XML ), 
	                            		Relationship.class );
		_client.close();
		
		return r.getId();
	}
	
	
	/**
	 * Retrieves a specific relationship from the Web Service.
	 * 
	 * <p> GET ...{@value #URI_RELATIONSHIP_id} </p>
	 * 
	 * @param  relationshipID the relationship ID on the Web Service
	 * @return                a {@link Relationship} object with the 
	 *                        relationship's data
	 * @throws Exception
	 */
	protected Relationship getRelationship( String relationshipID ) 
			throws Exception {
		
		if ( relationshipID == null )
			throw new NullPointerException( "getRelationship: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_RELATIONSHIP_id )
                                  .build( relationshipID );
		
		Client _client = ClientBuilder.newClient();
		Relationship r = _client.target( localURI )
                                .request( MediaType.APPLICATION_XML )
                                .get( Relationship.class );
		_client.close();
		
		return r;
	}

	
	/**
	 * Removes a specific relationship from the Web Service.
	 * 
	 * <p> DELETE ...{@value #URI_RELATIONSHIP_id} </p>
	 * 
	 * @param  relationshipID the relationship ID
	 * @throws Exception
	 */
	protected void deleteRelationship( String relationshipID ) 
			throws Exception {
		
		if ( relationshipID == null )
			throw new NullPointerException( "deleteRelationship: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_RELATIONSHIP_id )
                                  .build( relationshipID );
		
		Client _client = ClientBuilder.newClient();
		Response response = _client.target( localURI )
                                   .request( MediaType.APPLICATION_XML )
                                   .delete();
		_client.close();
		
		if ( response.getStatus() >= 400 )
			throw new WebApplicationException( "deleteRelationship: DELETE error" );
	}
	
	
	/**
	 * Retrieves all relationships of a GraphNode.
	 * 
	 * <p> GET ...{@value #URI_NODE_id_RELATIONSHIPS_ALL} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @return             a {@link Relationships} object with all GraphNode's 
	 *                     relationships
	 * @throws Exception
	 */
	protected Relationships getNodeRelationships( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "getNodeRelationships: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_RELATIONSHIPS_ALL )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Relationships r = _client.target( localURI )
                                 .request( MediaType.APPLICATION_XML )
                                 .get( Relationships.class );
		_client.close();
		
		return r;
	}
	
	
	/**
	 * Retrieves all IN relationships of a GraphNode, i.e. all
	 * relationships that have this GraphNode as the destination node.
	 * 
	 * <p> GET ...{@value #URI_NODE_id_RELATIONSHIPS_IN} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @return             a {@link Relationships} object with all GraphNode's 
	 *                     relationships
	 * @throws Exception
	 */
	protected Relationships getNodeInRelationships( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "getNodeInRelationships: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_RELATIONSHIPS_IN )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Relationships r = _client.target( localURI )
                                 .request( MediaType.APPLICATION_XML )
                                 .get( Relationships.class );
		_client.close();
		
		return r;
	}
	
	
	/**
	 * Retrieves all OUT relationships of a GraphNode, i.e. all
	 * relationships that have this GraphNode as the source node.
	 * 
	 * <p> GET ...{@value #URI_NODE_id_RELATIONSHIPS_OUT} </p>
	 * 
	 * @param  graphNodeID the GraphNode ID
	 * @return             a {@link Relationships} object with all GraphNode's 
	 *                     relationships
	 * @throws Exception
	 */
	protected Relationships getNodeOutRelationships( String graphNodeID ) 
			throws Exception {
		
		if ( graphNodeID == null )
			throw new NullPointerException( "getNodeOutRelationships: null argument" );
		
		URI localURI = _uriBuilder.clone()
                                  .path( URI_NODE_id_RELATIONSHIPS_OUT )
                                  .build( graphNodeID );
		
		Client _client = ClientBuilder.newClient();
		Relationships r = _client.target( localURI )
                                 .request( MediaType.APPLICATION_XML )
                                 .get( Relationships.class );
		_client.close();
		
		return r;
	}

	
	
	
	// ======================================================================
	// REACHABLE NODES
	// ======================================================================
	
	
	/**
	 * Retrieves all GraphNodes reachable from the specified GraphNode.
	 * <p> 
	 * GET ...{@value #URI_NODE_id_REACHABLENODES}
	 * 
	 * <p> 
	 * <b>note</b>: <br> 
	 * {@code graphNodeTypes} cannot be null, if no graph node types are to be
	 * specified and empty set must be passed as parameter. <br>
	 * {@code label} must be null if no label has to be specified.
	 * 
	 * 
	 * @param  graphNodeID      the GraphNode ID
	 * @param  graphNodeTypes   the GraphNode types to be considered
	 * @param  label            the GraphNode label to be considered
	 * @return                  a {@link Nodes} objects with all the reachable 
	 *                          GraphNodes data
	 * @throws Exception
	 */
	protected 
	Nodes getReacheableNodesFromNode( String graphNodeID, Set<String> graphNodeTypes, String label ) 
			throws Exception {
		
		if ( ( graphNodeID == null ) || ( graphNodeTypes == null ) )
			throw new NullPointerException( "getReacheableNodesFromNode: null argument" );
		
		UriBuilder localBuilder = _uriBuilder.clone().path( URI_NODE_id_REACHABLENODES );
		
		if ( !( graphNodeTypes.isEmpty() ) ) {
			String relationshipTypes = new String( "" );
			
			for ( String type : graphNodeTypes )
				relationshipTypes = relationshipTypes.concat( type ).concat( "%7C" );
			
			relationshipTypes = relationshipTypes.substring( 0, ( relationshipTypes.length() - 3 ) );
			
			localBuilder = localBuilder.queryParam( URI_QP_RELATIONSHIPTYPES, relationshipTypes );
		}
		
		if ( label != null )
			localBuilder = localBuilder.queryParam( URI_QP_NODELABELS, label );
		
		URI localURI = localBuilder.build( graphNodeID );
		
//		System.err.println(localURI.toString());
		
		Client _client = ClientBuilder.newClient();
		Nodes nodes = _client.target( localURI )
                             .request( MediaType.APPLICATION_XML )
                             .get( Nodes.class );
		_client.close();
		
		return nodes;
	}
}
