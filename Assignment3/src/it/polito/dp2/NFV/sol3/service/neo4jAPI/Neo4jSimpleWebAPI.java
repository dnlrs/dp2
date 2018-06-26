package it.polito.dp2.NFV.sol3.service.neo4jAPI;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.sol3.model.neo4j.Labels;
import it.polito.dp2.NFV.sol3.model.neo4j.Node;
import it.polito.dp2.NFV.sol3.model.neo4j.Nodes;
import it.polito.dp2.NFV.sol3.model.neo4j.Properties;
import it.polito.dp2.NFV.sol3.model.neo4j.Property;
import it.polito.dp2.NFV.sol3.model.neo4j.Relationship;
import it.polito.dp2.NFV.sol3.model.neo4j.Relationships;


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
//    private final static String URI_RELATIONSHIP                = "data/relationship";
    private final static String URI_RELATIONSHIP_id             = "data/relationship/{relationship_id}";
    private final static String URI_NODE_id_REACHABLENODES      = "data/node/{node_id}/reachableNodes";

    private final static String URI_QP_RELATIONSHIPTYPES        = "relationshipTypes";
    private final static String URI_QP_NODELABELS               = "nodeLabel";

    private final static String PROPERTY_URL                    = "it.polito.dp2.NFV.lab3.Neo4JSimpleXMLURL";

    private static URI        BASE_URI    = null;
    private static UriBuilder _uriBuilder = null;


    public Neo4jSimpleWebAPI()
            throws Neo4jSimpleWebAPIException {

        if ( (Neo4jSimpleWebAPI.BASE_URI == null) ||
                (Neo4jSimpleWebAPI._uriBuilder == null) ) {
            try {

                String url =
                        System.getProperty(
                                PROPERTY_URL,
                                "http://localhost:8080/Neo4JSimpleXML/rest" );

                if ( url == null )
                    throw new Neo4jSimpleWebAPIException( "URL System variable not found" );

                Neo4jSimpleWebAPI.BASE_URI    = URI.create( url );
                Neo4jSimpleWebAPI._uriBuilder =
                        UriBuilder.fromUri( Neo4jSimpleWebAPI.BASE_URI );

            } catch ( SecurityException
                      | NullPointerException
                      | IllegalArgumentException e ) {
                throw new Neo4jSimpleWebAPIException( e, "URL System variable not found" );
            }
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
     */
    public String createGraphNode( Node node )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( node == null )
            throw new NullPointerException( "createGraphNode: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE )
                                            .build();


            Node n =  client.target( localURI )
                            .request( MediaType.APPLICATION_XML )
                            .post( Entity.entity( node, MediaType.APPLICATION_XML ),
                                   Node.class );
            return n.getId();

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
            client.close();
        }
    }


    /**
     * Retrieves a GraphNode.
     * <p>
     * GET ...{@value #URI_NODE_id}
     *
     * @param  graphNodeID the GraphNode ID
     * @return             a {@link Node} object with the GraphNode data
     */
    public Node getNode( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNode: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id )
                                            .build( graphNodeID );

            Node n =  client.target( localURI )
                            .request( MediaType.APPLICATION_XML )
                            .get( Node.class );
            return n;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
            client.close();
        }
    }


    /**
     * Deletes a GraphNode.
     *
     * <p> DELETE  ...{@value #URI_NODE_id} </p>
     *
     * @param  graphNodeID the GraphNode ID
     */
    public void deleteNode( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "deleteNode: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id )
                                            .build( graphNodeID );


            Response response =  client.target( localURI )
                                       .request( MediaType.APPLICATION_XML )
                                       .delete();

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException( "deleteNode: node delete error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
            client.close();
        }
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
     */
    public void updateNodeProperties( String graphNodeID, Properties properties)
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( properties == null ) )
            throw new NullPointerException( "updateNodeProperties: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_PROPERTIES )
                                            .build( graphNodeID );

            Response response =  client.target( localURI )
                                       .request( MediaType.APPLICATION_XML )
                                       .put( Entity.entity( properties, MediaType.APPLICATION_XML ) );

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException( "updateNodeProperties: PUT error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }

    }


    /**
     * Retrieves all properties of a GraphNode.
     *
     * <p> GET ...{@value #URI_NODE_id_PROPERTIES} </p>
     *
     * @param  graphNodeID the GraphNode ID
     * @return             a {@link Properties} object with GraphNode properties
     */
    public Properties getNodeProperties( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeProperties: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_PROPERTIES )
                                            .build( graphNodeID );


            Properties properties =  client.target( localURI )
                                           .request( MediaType.APPLICATION_XML )
                                           .get( Properties.class );

            return properties;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
    }


    /**
     * Deletes GraphNode's properties.
     *
     * <p> DELETE ...{@value #URI_NODE_id_PROPERTIES} </p>
     *
     * @param  graphNodeID the GraphNode ID
     */
    public void deleteNodeProperties( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "deleteNodeProperties: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_PROPERTIES )
                                            .build( graphNodeID );

            Response response =  client.target( localURI )
                                       .request( MediaType.APPLICATION_XML )
                                       .delete();

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException(
                        "deleteNodeProperties: DELETE error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
    }


    /**
     * Updates a specific GraphNode's property.
     *
     * <p> PUT ...{@value #URI_NODE_id_PROPERTIES_property} </p>
     *
     * @param graphNodeID the GraphNode ID
     * @param property    {@link Property} object with updated data
     */
    public void updateNodeProperty( String graphNodeID, Property property )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( property == null ) )
            throw new NullPointerException( "updateNodeProperty: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_PROPERTIES_property )
                                            .build( graphNodeID, property.getName() );


            Response response =  client.target( localURI )
                                       .request( MediaType.APPLICATION_XML )
                                       .put( Entity.entity(
                                                property,
                                                MediaType.APPLICATION_XML ) );

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException( "updateNodeProperty: PUT error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
    }


    /**
     * Retrieves a specific GraphNode property.
     *
     * <p> GET ...{@value #URI_NODE_id_PROPERTIES_property} </p>
     *
     * @param graphNodeID   GraphNode ID
     * @param property_name the property name
     * @return              the GraphNode's {@link Property}
     */
    public Property getNodeProperty( String graphNodeID, String property_name )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( property_name == null ) )
            throw new NullPointerException( "getNodeProperty: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_PROPERTIES_property )
                                            .build( graphNodeID, property_name );


            Property property =   client.target( localURI )
                                        .request( MediaType.APPLICATION_XML )
                                        .get( Property.class );

            return property;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
    }


    /**
     * Deletes a specific GraphNode property.
     *
     * <p> DELETE ...{@value #URI_NODE_id_PROPERTIES_property} </p>
     *
     * @param  graphNodeID   the GraphNode ID
     * @param  property_name the property name
     */
    public void deleteNodeProperty( String graphNodeID, String property_name )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( property_name == null ) )
            throw new NullPointerException( "deleteNodeProperty: null argument" );


        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_PROPERTIES_property )
                                            .build( graphNodeID, property_name );
            Response response =  client.target( localURI )
                                       .request( MediaType.APPLICATION_XML )
                                       .delete();

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException( "deleteNodeProperty: DELETE error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
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
     */
    public void createNodeLabel( String graphNodeID, Labels label )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( label == null ) )
            throw new NullPointerException( "createNodeLabel: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_LABELS )
                                            .build( graphNodeID );

            Response r =  client.target( localURI )
                                .request( MediaType.APPLICATION_XML )
                                .post( Entity.entity( label, MediaType.APPLICATION_XML ) );

            if ( r.getStatus() >= 400 )
                throw new WebApplicationException( "createNodeLabel: POST error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
    }


    /**
     * Retrieves all GraphNode's labels.
     *
     * <p> GET ...{@value #URI_NODE_id_LABELS} </p>
     *
     * @param  graphNodeID the GraphNode ID
     * @return             a {@link Labels} object with all GraphNode's labels
     */
    public Labels getNodeLabels( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeLabels: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                            .path( URI_NODE_id_LABELS )
                                            .build( graphNodeID );
            Labels labels =  client.target( localURI )
                                   .request( MediaType.APPLICATION_XML )
                                   .get( Labels.class );

            return labels;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
             client.close();
        }
    }


    /**
     * Updates all the GraphNode labels.
     *
     * <p> PUT ...{@value #URI_NODE_id_LABELS} </p>
     *
     * @param  graphNodeID the GraphNode ID
     * @param  label       a {@link Labels} object with all the new labels
     */
    public void updateNodeLabels( String graphNodeID, Labels label )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( label == null ) )
            throw new NullPointerException( "updateNodeLabels: null argument" );

        Client client = ClientBuilder.newClient();

       try {

           URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_NODE_id_LABELS )
                                           .build( graphNodeID );

           Response r =   client.target( localURI )
                                     .request( MediaType.APPLICATION_XML )
                                     .put( Entity.entity( label, MediaType.APPLICATION_XML ) );

           if ( r.getStatus() >= 400 )
               throw new WebApplicationException( "updateNodeLabels: POST error" );

       } catch ( Exception e ) {
           throw new Neo4jSimpleWebAPIException( e );
       } finally {
                client.close();
       }
    }


    /**
     * Deletes a specific GraphNode label.
     *
     * <p> DELETE ...{@value #URI_NODE_id_LABELS_label} </p>
     *
     * @param  graphNodeID the GraphNode ID
     * @param  label_name  the label name
     */
    public void deleteNodeLabel( String graphNodeID, String label_name )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( label_name == null ) )
            throw new NullPointerException( "deleteNodeLabel: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_NODE_id_LABELS_label )
                                           .build( graphNodeID, label_name );


            Response response =  client.target( localURI )
                                            .request( MediaType.APPLICATION_XML )
                                            .delete();

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException( "deleteNodeLabel: DELETE error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
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
     */
    public String createNodeRelationship(
            String graphNodeID,
            Relationship relationship )
                    throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( relationship == null ) )
            throw new NullPointerException( "createNodeRelationship: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_NODE_id_RELATIONSHIPS )
                                           .build( graphNodeID );


            Relationship r =  client.target( localURI )
                                         .request( MediaType.APPLICATION_XML )
                                         .post( Entity.entity(
                                                     relationship,
                                                     MediaType.APPLICATION_XML ),
                                                             Relationship.class );

            return r.getId();

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
    }


    /**
     * Retrieves a specific relationship from the Web Service.
     *
     * <p> GET ...{@value #URI_RELATIONSHIP_id} </p>
     *
     * @param  relationshipID the relationship ID on the Web Service
     * @return                a {@link Relationship} object with the
     *                        relationship's data
     */
    public Relationship getRelationship( String relationshipID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( relationshipID == null )
            throw new NullPointerException( "getRelationship: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_RELATIONSHIP_id )
                                           .build( relationshipID );

            Relationship r =  client.target( localURI )
                                         .request( MediaType.APPLICATION_XML )
                                         .get( Relationship.class );

            return r;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
    }


    /**
     * Removes a specific relationship from the Web Service.
     *
     * <p> DELETE ...{@value #URI_RELATIONSHIP_id} </p>
     *
     * @param  relationshipID the relationship ID
     */
    public void deleteRelationship( String relationshipID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( relationshipID == null )
            throw new NullPointerException( "deleteRelationship: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_RELATIONSHIP_id )
                                           .build( relationshipID );

            Response response =  client.target( localURI )
                                            .request( MediaType.APPLICATION_XML )
                                            .delete();

            if ( response.getStatus() >= 400 )
                throw new WebApplicationException( "deleteRelationship: DELETE error" );

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
    }


    /**
     * Retrieves all relationships of a GraphNode.
     *
     * <p> GET ...{@value #URI_NODE_id_RELATIONSHIPS_ALL} </p>
     *
     * @param  graphNodeID the GraphNode ID
     * @return             a {@link Relationships} object with all GraphNode's
     *                     relationships
     */
    public Relationships getNodeRelationships( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeRelationships: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_NODE_id_RELATIONSHIPS_ALL )
                                           .build( graphNodeID );


            Relationships r =  client.target( localURI )
                                          .request( MediaType.APPLICATION_XML )
                                          .get( Relationships.class );

            return r;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
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
     */
    public Relationships getNodeInRelationships( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeInRelationships: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_NODE_id_RELATIONSHIPS_IN )
                                           .build( graphNodeID );


            Relationships r =  client.target( localURI )
                                          .request( MediaType.APPLICATION_XML )
                                          .get( Relationships.class );

            return r;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
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
     */
    public Relationships getNodeOutRelationships( String graphNodeID )
            throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeOutRelationships: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            URI localURI = Neo4jSimpleWebAPI._uriBuilder.clone()
                                           .path( URI_NODE_id_RELATIONSHIPS_OUT )
                                           .build( graphNodeID );

            Relationships r =  client.target( localURI )
                                          .request( MediaType.APPLICATION_XML )
                                          .get( Relationships.class );

            return r;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
                 client.close();
        }
    }




    // ======================================================================
    // REACHABLE NODES
    // ======================================================================


    /**
     * Retrieves all GraphNodes reachable from the specified GraphNode
     * sequentially.
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
     */
    public
    Nodes syncGetReacheableNodesFromNode(
            String graphNodeID,
            Set<String> graphNodeTypes,
            String label )
                    throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( ( graphNodeID == null ) || ( graphNodeTypes == null ) )
            throw new NullPointerException( "getReacheableNodesFromNode: null argument" );

        Client client = ClientBuilder.newClient();

        try {

            UriBuilder localBuilder = Neo4jSimpleWebAPI._uriBuilder.clone()
                                                      .path( URI_NODE_id_REACHABLENODES );

            if ( !(graphNodeTypes.isEmpty()) ) {

                String relationshipTypes = new String( "" );

                for ( String type : graphNodeTypes ) {
                    relationshipTypes = relationshipTypes.concat( type )
                                                         .concat( "%7C" );
                }

                relationshipTypes =
                        relationshipTypes.substring( 0, (relationshipTypes.length() - 3) );

                localBuilder =
                        localBuilder.queryParam( URI_QP_RELATIONSHIPTYPES, relationshipTypes );
            }

            if ( label != null ) {
                localBuilder = localBuilder.queryParam( URI_QP_NODELABELS, label );
            }


            URI localURI = localBuilder.build( graphNodeID );

            Nodes nodes =  client.target( localURI )
                                      .request( MediaType.APPLICATION_XML )
                                      .get( Nodes.class );
            return nodes;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        } finally {
            client.close();
        }
    }

    /**
     * Retrieves all GraphNodes reachable from the specified GraphNode in an
     * asynchronous way.
     * <p>
     * GET ...{@value #URI_NODE_id_REACHABLENODES}
     * <p>
     * <b>{@link Client} must be created and closed by caller</b>
     * <p>
     * <b>note</b>:<br>
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
     */
    public
    Future<Nodes> asyncGetReacheableNodesFromNode(
            Client client,
            String graphNodeID,
            Set<String> graphNodeTypes,
            String label )
                    throws Neo4jSimpleWebAPIException, NullPointerException {

        if ( (graphNodeID == null) || (graphNodeTypes == null) )
            throw new NullPointerException(
                    "getReacheableNodesFromNode: null argument" );

        if (  client == null )
            throw new NullPointerException(
                    "getReacheableNodesFromNode: " +
                    "client must be created by the caller" );

        try {

            UriBuilder localBuilder = Neo4jSimpleWebAPI._uriBuilder.clone()
                                                      .path( URI_NODE_id_REACHABLENODES );

            if ( !(graphNodeTypes.isEmpty()) ) {

                String relationshipTypes = new String( "" );

                for ( String type : graphNodeTypes ) {
                    relationshipTypes = relationshipTypes.concat( type )
                                                         .concat( "%7C" );
                }

                relationshipTypes =
                        relationshipTypes.substring( 0, (relationshipTypes.length() - 3) );
                localBuilder      =
                        localBuilder.queryParam( URI_QP_RELATIONSHIPTYPES, relationshipTypes );
            }

            if ( label != null ) {
                localBuilder = localBuilder.queryParam( URI_QP_NODELABELS, label );
            }

            URI localURI = localBuilder.build( graphNodeID );

            Future<Nodes> futureNodes =  client.target( localURI )
                                               .request( MediaType.APPLICATION_XML )
                                               .async()
                                               .get( Nodes.class );
            return futureNodes;

        } catch ( Exception e ) {
            throw new Neo4jSimpleWebAPIException( e );
        }
    }
}
