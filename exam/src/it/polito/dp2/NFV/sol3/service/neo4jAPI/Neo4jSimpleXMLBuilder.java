package it.polito.dp2.NFV.sol3.service.neo4jAPI;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Labels;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Node;
import it.polito.dp2.NFV.sol3.service.model.neo4j.ObjectFactory;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Properties;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Property;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Relationship;

/**
 * Builds XML marshallable Objects starting from NFV System objects interfaces.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class Neo4jSimpleXMLBuilder {

    public final static String PROPERTY_NAME = "name";

    public final static String LABEL_NODE = "Node";
    public final static String LABEL_HOST = "Host";

    public final static String RELATIONSHIP_FORWARDSTO  = "ForwardsTo";
    public final static String RELATIONSHIP_ALLOCATEDON = "AllocatedOn";

    private final ObjectFactory of;      // WADL/JAXB object factory

    public Neo4jSimpleXMLBuilder() {
        this.of      = new ObjectFactory();
    }


    public Node createXMLNodeFromNodeReader( NodeReader nodeInterface )
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


    public Node createXMLNodeFromHostReader( HostReader hostInterface )
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




    public Relationship createXMLAllocatedOnRel(
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



    public Relationship createXMLForwardToRel(
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
