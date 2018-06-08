package it.polito.dp2.NFV.sol2;

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
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab2.AlreadyLoadedException;
import it.polito.dp2.NFV.lab2.ExtendedNodeReader;
import it.polito.dp2.NFV.lab2.NoGraphException;
import it.polito.dp2.NFV.lab2.ReachabilityTester;
import it.polito.dp2.NFV.lab2.ReachabilityTesterException;
import it.polito.dp2.NFV.lab2.ServiceException;
import it.polito.dp2.NFV.lab2.UnknownNameException;

/**
 * An implementation of the {@link ReachabilityTester} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealReachabilityTester implements ReachabilityTester {

    /*
     * This timer may be used to avoid "aggressive polling" in the asynchronous
     * getExtendedNodes method when interrogating {@link Future} objects.
     *
     * NOTE: the value set was experimental, yet it is not used because of
     *       tests timeout.
     */
//    private final static int    SLEEPING_TIME_MS              = 100;

    private final NfvReader             monitor; // Access to NFV System interfaces
    private final Neo4jSimpleWebAPI     neo4jWS; // (my) Neo4j WebService API
    private final Neo4jSimpleXMLBuilder builder; // builds neo4J XML objects
    private final IDsMappingService     map;     // keeps mapping with graph nodes IDs

    private final Set<String>           loadedNFFGs;   // keeps trace of loaded NFFGs


    protected RealReachabilityTester()
            throws ReachabilityTesterException {

        try {

            NfvReaderFactory factory = NfvReaderFactory.newInstance();

            this.monitor = factory.newNfvReader();
            this.neo4jWS = Neo4jSimpleWebAPI.newInstance();
            this.builder = new Neo4jSimpleXMLBuilder();

        } catch ( FactoryConfigurationError fce ) {
            throw new ReachabilityTesterException( fce.getException() );
        } catch ( NfvReaderException
                  | Neo4jSimpleWebAPIException e ) {
            throw new ReachabilityTesterException( e );
        } catch ( Exception e ) {
            System.err.println( "Unknown exception" );
            throw new ReachabilityTesterException( e );
        }

        this.map         = new IDsMappingService();
        this.loadedNFFGs = new HashSet<String>();
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
    @Override
    public void loadGraph( String nffgName )
            throws UnknownNameException,
                       AlreadyLoadedException, ServiceException {

        if ( nffgName == null )
            throw new UnknownNameException( "loadGraph: null argument" );

        if ( this.monitor.getNffg( nffgName ) == null )
            throw new UnknownNameException( "loadGraph: inexistent NFFG" );

        if ( isLoaded( nffgName ) )
            throw new AlreadyLoadedException( "loadGraph: NFFG already loaded" );

        try {

            /* init relationsihpID mappings for this NFFG */
            this.map.initLinksMappingForNFFG( nffgName ); //

            Set<LinkReader> setOFLinkInterfaces = new HashSet<LinkReader>();

            this.neo4jWS.newClient();
            NffgReader nffg = this.monitor.getNffg( nffgName );

            // 1.
            for ( NodeReader nodeI : nffg.getNodes() ) {

                // 1.a.
                Node nodeXMLNode =
                        this.builder.createXMLNodeFromNodeReader( nodeI );

                // 1.b.
                String nodeGraphNodeID =
                        this.neo4jWS.createGraphNode( nodeXMLNode );

                this.neo4jWS.createNodeLabel(
                        nodeGraphNodeID,
                        nodeXMLNode.getLabels() );
                // 1.c.
                this.map.addNode( nodeGraphNodeID, nodeI.getName() );

                // 1.2
                HostReader hostI       = nodeI.getHost();
                String hostGraphNodeID = null;

                if ( this.map.hostNameIsPresent( hostI.getName() ) ) {
                    // 2.d.
                    hostGraphNodeID  =
                            this.map.getGraphNodeIDFromHostName( hostI.getName() );

                } else {
                    // 2.a.
                    Node hostXMLNode =
                            this.builder.createXMLNodeFromHostReader( hostI );

                    // 2.b.
                    hostGraphNodeID  =
                            this.neo4jWS.createGraphNode( hostXMLNode );

                    this.neo4jWS.createNodeLabel(
                            hostGraphNodeID,
                            hostXMLNode.getLabels() );

                    // 2.c.
                    this.map.addHost( hostGraphNodeID, hostI.getName() );
                }

                // 1.3. / 1.3.a.
                Relationship allocatedOnRelationship =
                        this.builder.createXMLAllocatedOnRel(
                                nodeGraphNodeID,
                                hostGraphNodeID );
                // 1.3.b.
                String allocatedOnRelationshipID =
                        this.neo4jWS.createNodeRelationship(
                                nodeGraphNodeID,
                                allocatedOnRelationship );
                // 1.3.c.
                this.map.addLink(
                        nffgName,
                        new String( nodeI.getName() + "TO" + hostI.getName() ), /* "nodeNameTOhostName" */
                        allocatedOnRelationshipID );

                // 1.3.d.
                setOFLinkInterfaces.addAll( nodeI.getLinks() );
            }

            // 2.
            for ( LinkReader linkI : setOFLinkInterfaces ) {

                // 4.a.
                String srcNodeName    = linkI.getSourceNode().getName();
                String dstNodeName    = linkI.getDestinationNode().getName();
                String srcGraphNodeID = this.map.getGraphNodeIDFromNodeName( srcNodeName );
                String dstGraphNodeID = this.map.getGraphNodeIDFromNodeName( dstNodeName );

                // 2.b.
                Relationship forwardsToRelationship =
                        this.builder.createXMLForwardToRel(
                                srcGraphNodeID,
                                dstGraphNodeID );
                // 2.c.
                String forwardsToRelationshipID =
                        this.neo4jWS.createNodeRelationship(
                                srcGraphNodeID,
                                forwardsToRelationship);
                // 2.d.
                this.map.addLink(
                        nffgName,
                        linkI.getName(),
                        forwardsToRelationshipID );
            }

        } catch ( Neo4jSimpleWebAPIException
                  | WebApplicationException
                  | ProcessingException
                  | NullPointerException e ) {

            throw new ServiceException( e.getMessage() );

        } catch ( Exception e ) {
            System.err.println( "Unknown exception" );
            throw new ServiceException( e.getMessage() );

        } finally {
            this.neo4jWS.closeClient();
        }

        // remember loaded NFFGs
        this.loadedNFFGs.add( nffgName );
    }


    /**
     * An asynchronous implementation of the getExtendedNodes method.
     * <p>
     * It first send requests for getting reachable nodes for each node in
     * the NFFG and then performs polling on {@link Future} objects to
     * see which one "{@code isDone()}".
     * <p>
     * If no {@link Future} objects are ready a timer may be set in order
     * to avoid "aggressive polling" yet it's commented because of the
     * timeout of the tests.
     */
    @Override
    public Set<ExtendedNodeReader> getExtendedNodes( String nffgName )
            throws UnknownNameException, NoGraphException, ServiceException {


        if ( nffgName == null )
            throw new UnknownNameException( "getExtendedNodes: null argument" );

        if ( this.monitor.getNffg( nffgName ) == null )
            throw new UnknownNameException( "getExtendedNodes: inexistent NFFG" );

        if ( !( isLoaded( nffgName ) ) )
            throw new NoGraphException( "getExtendedNodes: NFFG is not loaded" );


        ConcurrentMap<NodeReader, Future<Nodes>> mapOfFutureNodes =
                new ConcurrentHashMap<NodeReader, Future<Nodes>>();

        this.neo4jWS.newClient();

        try {
            // for each node, ask for reachable hosts to the web service
            for ( NodeReader nodeI : this.monitor.getNffg( nffgName ).getNodes() ) {

                Future<Nodes> futureNodes =
                        this.neo4jWS.asyncGetReacheableNodesFromNode(
                                    this.map.getGraphNodeIDFromNodeName( nodeI.getName() ) /* graph node ID */,
                                    new HashSet<String>() /* types */,
                                    Neo4jSimpleXMLBuilder.LABEL_HOST );

                mapOfFutureNodes.put( nodeI, futureNodes );
            }

            Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();

            // get ready results for every node
            while ( !( mapOfFutureNodes.isEmpty() ) ) {

                for ( NodeReader nodeI : mapOfFutureNodes.keySet() ) {

                    Future<Nodes> futureNodes = mapOfFutureNodes.get( nodeI );

                    if ( futureNodes.isCancelled() )
                        throw new ServiceException( "getExtendedNodes: request was cancelled" );

                    if ( !(futureNodes.isDone()) ) {
                        continue; // this node is not yet completed
                    }

                    Nodes xmlNodes                   = futureNodes.get();
                    Set<HostReader> setOfHostReaders = new HashSet<HostReader>();

                    for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {

                        boolean propertyFound = false;
                        for ( Property property : xmlNode.getProperties().getProperty() )
                            if ( property.getName().compareTo( Neo4jSimpleXMLBuilder.PROPERTY_NAME ) == 0 ) {

                                setOfHostReaders.add( this.monitor.getHost( property.getValue() ) );
                                propertyFound = true;
                                break;

                            }

                        if ( !propertyFound )
                            throw new ServiceException( "getExtendedNodes: \"name\" property not found" );
                    }

                    result.add( new RealExtendedNode( nodeI, setOfHostReaders ) );
                    mapOfFutureNodes.remove( nodeI );
                }
                /*
                 * See NOTE on SLEEPING_TIME_MS and method javadoc.
                 */
//                try {
//                    Thread.sleep( SLEEPING_TIME_MS );
//                } catch (InterruptedException e ) {}
            }

            return result;

        } catch ( Exception e ) {
            throw new ServiceException( e.getMessage() );
        } finally {
            this.neo4jWS.closeClient();
        }
    }


    /*
     * The first version of the getExtendedNodes was a Synchronous one which
     * for each Node in the NFFG interrogated the REST Web Service for the
     * reachable Host nodes and waited for a response.
     *
     * It was substituted with an optimized asynchronous implementation.
     * Left commented for reference.
     */
//    @Override
//    public Set<ExtendedNodeReader> getExtendedNodes( String nffgName )
//            throws UnknownNameException, NoGraphException, ServiceException {
//
//        if ( nffgName == null )
//            throw new UnknownNameException( "getExtendedNodes: null argument" );
//
//        if ( monitor.getNffg( nffgName ) == null )
//            throw new UnknownNameException( "getExtendedNodes: inexistent NFFG" );
//
//        if ( !( isLoaded( nffgName ) ) )
//            throw new NoGraphException( "getExtendedNodes: NFFG is not loaded" );
//
//        Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();
//
//        neo4jWS.newClient();
//
//        for ( NodeReader nodeI : monitor.getNffg( nffgName ).getNodes() ) {
//
//            try {
//
//                String nodeGraphNodeID = map.getGraphNodeIDFromNodeName( nodeI.getName() );
//
//                Nodes xmlNodes =
//                        neo4jWS.syncGetReacheableNodesFromNode(
//                                nodeGraphNodeID, /* types */ new HashSet<String>(),  HOST_LABEL );
//
//                Set<HostReader> setOfHostReaders = new HashSet<HostReader>();
//                for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {
//
//                    String nodeName = null;
//                    for ( Property property : xmlNode.getProperties().getProperty() )
//                        if ( property.getName().compareTo( PROPERTY_NAME_NAME ) == 0 ) {
//                            nodeName = property.getValue();
//                            break;
//                        }
//
//                    if ( nodeName != null ) {
//                        HostReader reachableHostI = monitor.getHost( nodeName );
//                        setOfHostReaders.add( reachableHostI );
//                    } else {
//                        throw new ServiceException( "getExtendedNodes: \"name\" property not found" );
//                    }
//                }
//
//                ExtendedNodeReaderReal extendedNode =
//                        new ExtendedNodeReaderReal( nodeI, setOfHostReaders );
//                result.add( extendedNode );
//
//            } catch ( WebApplicationException | ProcessingException e ) {
//                throw new ServiceException( e.getMessage() );
//            } catch ( Exception e ) {
//                throw new ServiceException( e.getMessage() );
//            }
//
//        }
//
//        neo4jWS.closeClient();
//
//        return result;
//    }



    @Override
    public boolean isLoaded( String nffgName )
            throws UnknownNameException {

        if ( nffgName == null )
            throw new UnknownNameException( "isLoaded: null argument" );

        if ( this.monitor.getNffg( nffgName ) == null )
            throw new UnknownNameException( "isLoaded: inexistent NFFG" );

        if ( this.loadedNFFGs.contains( nffgName ) )
            return true;

        return false;
    }

}
