package it.polito.dp2.NFV.sol3.service;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import it.polito.dp2.NFV.sol3.service.database.IDsMappingService;
import it.polito.dp2.NFV.sol3.service.database.NfvDeployerDB;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleWebAPI;

public class NfvDeployerConfig implements ApplicationEventListener {

    private NfvDeployerDB     dbms   = null;
    private IDsMappingService mapper = null;
    private Neo4jSimpleWebAPI neo4j  = null;

    @Override
    public void onEvent( ApplicationEvent event ) {

        switch ( event.getType() ) {
            case INITIALIZATION_START:
            case RELOAD_FINISHED:

                dbms   = NfvDeployerDB.getInstance();
                mapper = IDsMappingService.getInstance();
                neo4j  = Neo4jSimpleWebAPI.getInstance();

                if ( neo4j == null )
                    return;







                break;

            case DESTROY_FINISHED:

                if ( neo4j != null ) {
                    neo4j.closeClient();
                }

                break;

            default:
                break;
        }






        }

    @Override
    public RequestEventListener onRequest(RequestEvent arg0) {

        return null;
    }
//
//    public void loadGraph( String nffgName )
//            throws UnknownEntityException, AlreadyLoadedException, ServiceException {
//
//
//        if ( nffgName == null )
//            throw new UnknownNameException( "loadGraph: null argument" );
//
//        if ( monitor.getNffg( nffgName ) == null )
//            throw new UnknownNameException( "loadGraph: inexistent NFFG" );
//
//        if ( isLoaded( nffgName ) )
//            throw new AlreadyLoadedException( "loadGraph: NFFG already loaded" );
//
//        try {
//
//            map.newNffgLinkToRelMapping( nffgName ); // init relationsihpID mappings for this NFFG
//
//            Set<LinkReader> setOFLinkInterfaces = new HashSet<LinkReader>();
//
//            neo4jWS.newClient();
//            // 1.
//            for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
//                // 1.a.
//                Node nodeXMLNode       = createXMLNodeFromNodeReader( nodeI );
//                // 1.b.
//                String nodeGraphNodeID = neo4jWS.createGraphNode( nodeXMLNode );
//                neo4jWS.createNodeLabel( nodeGraphNodeID, nodeXMLNode.getLabels() );
//                // 1.c.
//                map.addNode( nodeGraphNodeID, nodeI.getName() );
//
//                // 2
//                HostReader hostI       = nodeI.getHost();
//                String hostGraphNodeID = null;
//                if ( map.hostNameIsPresent( hostI.getName() ) ) {
//                    // 2.d.
//                    hostGraphNodeID  = map.getGraphNodeIDFromHostName( hostI.getName() );
//                } else {
//                    // 2.a.
//                    Node hostXMLNode = createXMLNodeFromHostReader( hostI );
//                    // 2.b.
//                    hostGraphNodeID  = neo4jWS.createGraphNode( hostXMLNode );
//                    neo4jWS.createNodeLabel( hostGraphNodeID, hostXMLNode.getLabels() );
//                    // 2.c.
//                    map.addHost( hostGraphNodeID, hostI.getName() );
//                }
//
//                // 3. / 3.a.
//                Relationship allocatedOnRelationship =
//                        createXMLAllocatedOnRel( nodeGraphNodeID, hostGraphNodeID );
//                // 3.b.
//                String allocatedOnRelationshipID =
//                        neo4jWS.createNodeRelationship( nodeGraphNodeID, allocatedOnRelationship );
//                // 3.c.
//                map.addLink( nffgName,
//                             new String( nodeI.getName() + "-" + hostI.getName() ), // relationship local name "nodeName-hostName"
//                             allocatedOnRelationshipID );
//                // 3.d.
//                setOFLinkInterfaces.addAll( nodeI.getLinks() );
//            }
//
//            // 4.
//            for ( LinkReader linkI : setOFLinkInterfaces ) {
//                // 4.a.
//                String srcNodeName    = linkI.getSourceNode().getName();
//                String dstNodeName    = linkI.getDestinationNode().getName();
//                String srcGraphNodeID = map.getGraphNodeIDFromNodeName( srcNodeName );
//                String dstGraphNodeID = map.getGraphNodeIDFromNodeName( dstNodeName );
//                // 4.b.
//                Relationship forwardsToRelationship =
//                        createXMLForwardToRel( srcGraphNodeID, dstGraphNodeID );
//                // 4.c.
//                String forwardsToRelationshipID =
//                        neo4jWS.createNodeRelationship( srcGraphNodeID, forwardsToRelationship);
//                // 4.d.
//                map.addLink( nffgName, linkI.getName(), forwardsToRelationshipID );
//            }
//
//            neo4jWS.closeClient();
//
//        } catch ( WebApplicationException | ProcessingException e ) {
//            neo4jWS.closeClient();
//            throw new ServiceException( e.getMessage() );
//        } catch ( Exception e ) {
//            neo4jWS.closeClient();
//            throw new ServiceException( e.getMessage() );
//        }
//
//        // remember loaded NFFGs
//        loadedNFFGs.add( nffgName );
//    }
//

}
