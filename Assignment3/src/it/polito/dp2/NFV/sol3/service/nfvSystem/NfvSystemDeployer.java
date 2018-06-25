package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;
import it.polito.dp2.NFV.sol3.service.UnknownNameException;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Node;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Relationship;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleWebAPI;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleWebAPIException;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleXMLBuilder;

public class NfvSystemDeployer {


    NfvSystem          system;
    NfvSystemIDService IDService;
    Neo4jSimpleXMLBuilder builder;

    public NfvSystemDeployer() {
        this.system    = new NfvSystem();
        this.IDService = NfvSystemIDService.getInstance();
        this.builder   = new Neo4jSimpleXMLBuilder();
    }





    /**
     * Strategy:
     * <ul>
     * <li> 1. for each node in the NFFG:
     * <ul>
     *   <li> a. Prepare an XML Node with data taken from the node interface
     *   <li> b. Ask the web service to create a new graph node according to
     *           the XML Node
     *   <li> c. Save the returned graph node id
     * </ul>
     *
     * <li> 1.2. new graph node from current NFFG node's hosting host
     * <ul>
     *   <li> a. Prepare an XML Node with data taken from the host hosting
     *           the current nodeI
     *   <li> b. Ask the web service to create a new graph node according to
     *           the XML Node ( unless 1.2.d. )
     *   <li> c. Save the returned graph node id
     *   <li> d. If there already was a graph node for this host, simply
     *           retrieve its id
     * </ul>
     * <li> 1.3. create relationship between node and host
     * <ul>
     *   <li> a. Prepare an XML Relationship with necessary data
     *   <li> b. Ask web service to create a new relationship between the
     *           graph nodes
     *   <li> c. Save the returned relationship id
     *   <li> d. Save all links found for current nodeI
     * </ul>
     * <li> 2. for each link in the NFFG create a relationship on the web
     *         service
     * <ul>
     *   <li> a. Retrieve the source graph node and the destination graph
     *           node of each link according to its source NFFG node and
     *           destination NFFG node
     *   <li> b. Create an XML Relationship with necessary data
     *   <li> c. Ask web service to create a new relationship between
     *           the graph nodes
     *   <li> d. Save the relationship id
     * </ul>
     * </ul>
     */
    public void deployNFFG( String nffgName )
            throws UnknownNameException,
                       AlreadyLoadedException,
                       ServiceException {

//        Neo4jSimpleXMLBuilder builder = new Neo4jSimpleXMLBuilder();

        if ( nffgName == null )
            throw new UnknownNameException( "loadGraph: null argument" );

        if ( this.system.getNffg( nffgName ) == null )
            throw new UnknownNameException( "loadGraph: inexistent NFFG" );

        if ( this.IDService.isLoadedNffg( nffgName ) )
            throw new AlreadyLoadedException( "loadGraph: NFFG already loaded" );


        Neo4jSimpleWebAPI neo4jWS;
        try {
            neo4jWS = new Neo4jSimpleWebAPI();
        } catch ( Neo4jSimpleWebAPIException e ) {
            throw new ServiceException( e );
        }

        try {

            /* init relationsihpID mappings for this NFFG */
            this.IDService.initLinksMappingForNFFG( nffgName ); //

            Set<LinkReader> setOFLinkInterfaces = new HashSet<LinkReader>();

            NffgReader nffg = this.system.getNffg( nffgName );

            for ( NodeReader nodeI : nffg.getNodes() ) {

                deployNode( nodeI );

//                Node nodeXMLNode =
//                        builder.createXMLNodeFromNodeReader( nodeI );
//
//                String nodeGraphNodeID =
//                        neo4jWS.createGraphNode( nodeXMLNode );
//
//                neo4jWS.createNodeLabel(
//                        nodeGraphNodeID,
//                        nodeXMLNode.getLabels() );
//                this.IDService.addNode( nodeGraphNodeID, nodeI.getName() );
//
//                HostReader hostI       = nodeI.getHost();
//                String hostGraphNodeID = null;
//
//                if ( this.IDService.hostNameIsPresent( hostI.getName() ) ) {
//                    hostGraphNodeID  =
//                            this.IDService.getGraphNodeIDFromHostName( hostI.getName() );
//
//                } else {
//                    Node hostXMLNode =
//                            builder.createXMLNodeFromHostReader( hostI );
//
//                    hostGraphNodeID  =
//                            neo4jWS.createGraphNode( hostXMLNode );
//
//                    neo4jWS.createNodeLabel(
//                            hostGraphNodeID,
//                            hostXMLNode.getLabels() );
//
//                    this.IDService.addHost( hostGraphNodeID, hostI.getName() );
//                }
//
//                Relationship allocatedOnRelationship =
//                        builder.createXMLAllocatedOnRel(
//                                nodeGraphNodeID,
//                                hostGraphNodeID );
//                String allocatedOnRelationshipID =
//                        neo4jWS.createNodeRelationship(
//                                nodeGraphNodeID,
//                                allocatedOnRelationship );
//                this.IDService.addLink(
//                        nffgName,
//                        new String( nodeI.getName() + "TO" + hostI.getName() ), /* "nodeNameTOhostName" */
//                        allocatedOnRelationshipID );

                setOFLinkInterfaces.addAll( nodeI.getLinks() );
            }

            for ( LinkReader linkI : setOFLinkInterfaces ) {

                deployLink( linkI );
//                String srcNodeName    = linkI.getSourceNode().getName();
//                String dstNodeName    = linkI.getDestinationNode().getName();
//                String srcGraphNodeID = this.IDService.getGraphNodeIDFromNodeName( srcNodeName );
//                String dstGraphNodeID = this.IDService.getGraphNodeIDFromNodeName( dstNodeName );
//
//                Relationship forwardsToRelationship =
//                        this.builder.createXMLForwardToRel(
//                                srcGraphNodeID,
//                                dstGraphNodeID );
//                String forwardsToRelationshipID =
//                        neo4jWS.createNodeRelationship(
//                                srcGraphNodeID,
//                                forwardsToRelationship);
//                this.IDService.addLink(
//                        nffgName,
//                        linkI.getName(),
//                        forwardsToRelationshipID );
            }

        } catch ( WebApplicationException
                  | ProcessingException
                  | NullPointerException e ) {

            throw new ServiceException( e.getMessage() );

        } catch ( Exception e ) {
            System.err.println( "Unknown exception" );
            throw new ServiceException( e.getMessage() );

        }

        // remember loaded NFFGs
        this.IDService.addLoadedNffg( nffgName );
    }





    public void deployNode( NodeReader nodeI )
            throws ServiceException {

//        Neo4jSimpleXMLBuilder builder = new Neo4jSimpleXMLBuilder();

        Neo4jSimpleWebAPI neo4jWS;
        try {
            neo4jWS = new Neo4jSimpleWebAPI();
        } catch ( Neo4jSimpleWebAPIException e ) {
            throw new ServiceException( e );
        }

        try {
            Node nodeXMLNode =
                    this.builder.createXMLNodeFromNodeReader( nodeI );

            String nodeGraphNodeID =
                    neo4jWS.createGraphNode( nodeXMLNode );

            neo4jWS.createNodeLabel(
                    nodeGraphNodeID,
                    nodeXMLNode.getLabels() );
            this.IDService.addNode( nodeGraphNodeID, nodeI.getName() );

            HostReader hostI       = nodeI.getHost();
            String hostGraphNodeID = null;

            if ( this.IDService.hostNameIsPresent( hostI.getName() ) ) {
                hostGraphNodeID  =
                        this.IDService.getGraphNodeIDFromHostName( hostI.getName() );

            } else {
                Node hostXMLNode =
                        this.builder.createXMLNodeFromHostReader( hostI );

                hostGraphNodeID  =
                        neo4jWS.createGraphNode( hostXMLNode );

                neo4jWS.createNodeLabel(
                        hostGraphNodeID,
                        hostXMLNode.getLabels() );

                this.IDService.addHost( hostGraphNodeID, hostI.getName() );
            }

            Relationship allocatedOnRelationship =
                    this.builder.createXMLAllocatedOnRel(
                            nodeGraphNodeID,
                            hostGraphNodeID );
            String allocatedOnRelationshipID =
                    neo4jWS.createNodeRelationship(
                            nodeGraphNodeID,
                            allocatedOnRelationship );
            this.IDService.addLink(
                    nodeI.getNffg().getName(),
                    new String( nodeI.getName() + "TO" + hostI.getName() ), /* "nodeNameTOhostName" */
                    allocatedOnRelationshipID );

        } catch ( Exception e ) {
            throw new ServiceException( e.getMessage() );
        }
    }




    public void deployLink( LinkReader linkI )
            throws ServiceException {

        Neo4jSimpleWebAPI neo4jWS;
        try {
            neo4jWS = new Neo4jSimpleWebAPI();
        } catch ( Neo4jSimpleWebAPIException e ) {
            throw new ServiceException( e );
        }

        try {
            String srcNodeName    = linkI.getSourceNode().getName();
            String dstNodeName    = linkI.getDestinationNode().getName();
            String srcGraphNodeID = this.IDService.getGraphNodeIDFromNodeName( srcNodeName );
            String dstGraphNodeID = this.IDService.getGraphNodeIDFromNodeName( dstNodeName );

            Relationship forwardsToRelationship =
                    this.builder.createXMLForwardToRel(
                            srcGraphNodeID,
                            dstGraphNodeID );
            String forwardsToRelationshipID =
                    neo4jWS.createNodeRelationship(
                            srcGraphNodeID,
                            forwardsToRelationship);
            this.IDService.addLink(
                    linkI.getSourceNode().getNffg().getName(),
                    linkI.getName(),
                    forwardsToRelationshipID );
        } catch ( Exception e ) {
            throw new ServiceException();
        }
    }


    public void unDeployNffg( NffgReader nffg ) {

        if ( nffg == null )
            return;

        Neo4jSimpleWebAPI neo4jWS;
        try {
            neo4jWS = new Neo4jSimpleWebAPI();

            for ( NodeReader nodeI : nffg.getNodes() ) {
                for ( LinkReader linkI : nodeI.getLinks() ) {
                    unDeployLink( linkI );
                }
            }

            for ( NodeReader node : nffg.getNodes() ) {
                unDeployNode( node );
            }
        } catch ( Exception e ) {}


        this.IDService.removeNffg( nffg.getName() );
    }


    public void unDeployLink( LinkReader link ) {

        String relationshipID =
                this.IDService.getRelIDFromLinkName(
                        link.getSourceNode().getNffg().getName(),
                        link.getName() );

        if ( relationshipID != null ) {
            try {

                Neo4jSimpleWebAPI neo4jWS = new Neo4jSimpleWebAPI();
                neo4jWS.deleteRelationship( relationshipID );
                this.IDService.removeRelationship(
                        link.getSourceNode().getNffg().getName(),
                        relationshipID );

            } catch ( Exception e ) {}
        }
    }

    public void unDeployNode( NodeReader node ) {

        String nodeID = this.IDService.getGraphNodeIDFromNodeName( node.getName() );

        if ( nodeID != null ) {
            try {

                Neo4jSimpleWebAPI neo4jWS = new Neo4jSimpleWebAPI();

                String hostRelID =
                        this.IDService.getRelIDFromLinkName(
                                            node.getNffg().getName(),
                                            node.getName() + "TO" + node.getHost().getName() );
                if ( hostRelID != null ) {
                    neo4jWS.deleteRelationship( hostRelID );
                }

                neo4jWS.deleteNode( nodeID );
                this.IDService.removeNode( nodeID );

            } catch ( Exception e ) {}
        }
    }
}
