package it.polito.dp2.NFV.sol3.service.neo4jAPI;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;

/**
 * Builds XML marshallable Objects starting from NFV System objects interfaces.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class Neo4jSimpleXMLBuilder {

    protected final static String PROPERTY_NAME = "name";

    protected final static String LABEL_NODE = "Node";
    protected final static String LABEL_HOST = "Host";

    protected final static String RELATIONSHIP_FORWARDSTO  = "ForwardsTo";
    protected final static String RELATIONSHIP_ALLOCATEDON = "AllocatedOn";

    private final ObjectFactory of;      // WADL/JAXB object factory



    protected Neo4jSimpleXMLBuilder() {
        this.of      = new ObjectFactory();
    }


    protected Node createXMLNodeFromNodeReader( NodeReader nodeInterface )
            throws NullPointerException, Exception {

        if ( nodeInterface == null )
            throw new NullPointerException(
                    "createXMLNodeFromNodeReader: null argument" );

        Property nodeProperty = this.of.createProperty();
        nodeProperty.setName( PROPERTY_NAME );
        nodeProperty.setValue( nodeInterface.getName() );

        Properties nodeProperties = this.of.createProperties();
        nodeProperties.getProperty().add( nodeProperty );

        Labels nodeLabels = this.of.createLabels();
        nodeLabels.getLabel().add( LABEL_NODE );

        Node xmlNode = this.of.createNode();
        xmlNode.setProperties( nodeProperties );
        xmlNode.setLabels( nodeLabels );

        return xmlNode;
    }


    protected Node createXMLNodeFromHostReader( HostReader hostInterface )
            throws NullPointerException, Exception {

        if ( hostInterface == null )
            throw new NullPointerException(
                    "createXMLNodeFromHostReader: null argument" );

        Property hostProperty = this.of.createProperty();
        hostProperty.setName( PROPERTY_NAME );
        hostProperty.setValue( hostInterface.getName() );

        Properties hostProperties = this.of.createProperties();
        hostProperties.getProperty().add( hostProperty );

        Labels hostLabels = this.of.createLabels();
        hostLabels.getLabel().add( LABEL_HOST );

        Node xmlNode = this.of.createNode();
        xmlNode.setProperties( hostProperties );
        xmlNode.setLabels( hostLabels );

        return xmlNode;
    }




    protected Relationship createXMLAllocatedOnRel(
            String nodeGraphNodeID,
            String hostGraphNodeID )
                    throws NullPointerException {

        if ( ( nodeGraphNodeID == null ) || ( hostGraphNodeID == null ) )
            throw new NullPointerException(
                    "createXMLAllocatedOnRel: null argument" );

        Relationship relationship = this.of.createRelationship();
        relationship.setSrcNode( nodeGraphNodeID );
        relationship.setDstNode( hostGraphNodeID );
        relationship.setType( RELATIONSHIP_ALLOCATEDON );

        return relationship;
    }



    protected Relationship createXMLForwardToRel(
            String srcGraphNodeID,
            String dstGraphNodeID )
                    throws NullPointerException {

        if ( ( srcGraphNodeID == null ) || ( dstGraphNodeID == null ) )
            throw new NullPointerException(
                    "createXMLForwardToRel: null argument" );

        Relationship relationship = this.of.createRelationship();
        relationship.setSrcNode( srcGraphNodeID );
        relationship.setDstNode( dstGraphNodeID );
        relationship.setType( RELATIONSHIP_FORWARDSTO );

        return relationship;
    }

}
