package it.polito.dp2.NFV.sol3.service.reachability;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.NoNodeException;
import it.polito.dp2.NFV.sol3.service.ServiceException;
import it.polito.dp2.NFV.sol3.service.UnknownNameException;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Nodes;
import it.polito.dp2.NFV.sol3.service.model.neo4j.Property;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleWebAPI;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleWebAPIException;
import it.polito.dp2.NFV.sol3.service.neo4jAPI.Neo4jSimpleXMLBuilder;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystemIDService;


/**
 * An implementation of the {@link ReachabilityTester} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class ReachabilityTester {

    private final NfvSystem          system;    // Access to NFV System interfaces
    private final Neo4jSimpleWebAPI  neo4jWS;   // (my) Neo4j WebService API
    private final NfvSystemIDService idService; // keeps mapping with graph nodes IDs


    public ReachabilityTester()
            throws ReachabilityTesterException {

        try {
            this.system = new NfvSystem();
            this.neo4jWS = new Neo4jSimpleWebAPI();
        } catch ( Exception e ) {
            throw new ReachabilityTesterException( e.getMessage() );
        }

        this.idService = NfvSystemIDService.getInstance();
    }



    /**
     * Gets the set of reachable hosts from the node.
     * <p>
     * Performs a synchronized call to Neo4J services.
     *
     * @param nodeName
     * @return
     * @throws UnknownNameException
     * @throws NoNodeException
     * @throws NoGraphException
     * @throws ServiceException
     */
    public Set<HostReader> getReachableHosts( String nodeName )
            throws UnknownNameException,
                    NoNodeException,
                    NoGraphException,
                    ServiceException {

        if ( nodeName == null )
            throw new UnknownNameException(
                    "getReachableHosts: null argument" );

        if ( this.system.getNode( nodeName ) == null )
            throw new NoNodeException(
                    "getReachableHosts: node does not exists in NFV System" );

        if ( !(this.idService.isLoadedNffg(
                this.system.getNode( nodeName ).getNffg().getName() )) )
            throw new NoGraphException(
                    "getReachableHosts: node's nffg is not loaded in Neo4j" );


        Neo4jSimpleWebAPI neo4jWS;
        try {
            neo4jWS = new Neo4jSimpleWebAPI();
        } catch ( Neo4jSimpleWebAPIException e ) {
            throw new ServiceException( e.getMessage() );
        }


        String nodeGraphNodeID = null;
        try {
            nodeGraphNodeID = this.idService.getGraphNodeIDFromNodeName( nodeName );
        } catch ( NullPointerException e ) {
            throw new NoNodeException( e.getMessage() );
        }


        /*
         * Use Neo4J services
         */
        Nodes xmlNodes = null;
        try {
            xmlNodes = neo4jWS.syncGetReacheableNodesFromNode(
                                    nodeGraphNodeID,
                                    new HashSet<String>(), /* all relationships */
                                    Neo4jSimpleXMLBuilder.LABEL_HOST );
        } catch ( Neo4jSimpleWebAPIException
                  | NullPointerException e ) {
            throw new ServiceException( e.getMessage() );
        } catch ( Exception e ) {
            throw new ServiceException( e.getMessage() );
        }

        /*
         * prepare result
         */
        Set<HostReader> result = new HashSet<HostReader>();

        for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {
            boolean propertyFound = false;

            for ( Property property : xmlNode.getProperties().getProperty() ) {
                if ( property.getName().compareTo(
                        Neo4jSimpleXMLBuilder.PROPERTY_NAME ) == 0 ) {

                    HostReader reachableHostI = this.system.getHost( property.getValue() );

                    if ( reachableHostI == null )
                        throw new UnknownNameException(
                                "getReachableHosts: invalid host name found" );

                    result.add( reachableHostI );

                    propertyFound = true;
                    break;
                }
            }

            if ( !propertyFound )
                throw new ServiceException(
                        "getReachableHosts: \"name\" property not found" );
        }


        return result;
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
    public Set<ExtendedNodeReader> getExtendedNodes( String nffgName )
            throws UnknownNameException,
                    NoGraphException,
                    ServiceException {


        if ( nffgName == null )
            throw new UnknownNameException( "getExtendedNodes: null argument" );

        if ( this.system.getNffg( nffgName ) == null )
            throw new UnknownNameException( "getExtendedNodes: inexistent NFFG" );

        if ( !(this.idService.isLoadedNffg( nffgName )) )
            throw new NoGraphException( "getExtendedNodes: NFFG is not loaded" );


        ConcurrentMap<NodeReader, Future<Nodes>> mapOfFutureNodes =
                new ConcurrentHashMap<NodeReader, Future<Nodes>>();


        Client client = ClientBuilder.newClient();

        try {
            // for each node, ask for reachable hosts to the web service
            for ( NodeReader nodeI : this.system.getNffg( nffgName ).getNodes() ) {

                Future<Nodes> futureNodes =
                        this.neo4jWS.asyncGetReacheableNodesFromNode(
                                client,
                                this.idService.getGraphNodeIDFromNodeName( nodeI.getName() ) /* graph node ID */,
                                new HashSet<String>() /* types */,
                                Neo4jSimpleXMLBuilder.LABEL_HOST );

                mapOfFutureNodes.put( nodeI, futureNodes );
            }

            Set<ExtendedNodeReader> result = new HashSet<ExtendedNodeReader>();

            // get ready results for every node
            while ( !(mapOfFutureNodes.isEmpty()) ) {

                for ( NodeReader nodeI : mapOfFutureNodes.keySet() ) {

                    Future<Nodes> futureNodes = mapOfFutureNodes.get( nodeI );

                    if ( futureNodes.isCancelled() )
                        throw new ServiceException(
                                "getExtendedNodes: request was cancelled" );

                    if ( !(futureNodes.isDone()) ) {
                        continue; // this node is not yet completed
                    }

                    Nodes xmlNodes                   = futureNodes.get();
                    Set<HostReader> setOfHostReaders = new HashSet<HostReader>();

                    for ( Nodes.Node xmlNode : xmlNodes.getNode() ) {

                        boolean propertyFound = false;

                        for ( Property property : xmlNode.getProperties().getProperty() ) {
                            if ( property.getName().compareTo(
                                    Neo4jSimpleXMLBuilder.PROPERTY_NAME ) == 0 ) {

                                HostReader reachableHost =
                                        this.system.getHost( property.getValue() );

                                if ( reachableHost == null )
                                    throw new UnknownNameException(
                                            "getExtendedNodes: invalid host name found" );

                                setOfHostReaders.add( reachableHost );
                                propertyFound = true;
                                break;

                            }
                        }

                        if ( !propertyFound )
                            throw new ServiceException(
                                    "getExtendedNodes: \"name\" property not found" );
                    }

                    result.add( new RealExtendedNode( nodeI, setOfHostReaders ) );
                    mapOfFutureNodes.remove( nodeI );
                }
            }

            return result;

        } catch ( Neo4jSimpleWebAPIException
                | NullPointerException e) {
          throw new ServiceException( e.getMessage() );
        } catch ( Exception e ) {
            throw new ServiceException( e.getMessage() );
        } finally {
            client.close();
        }
    }

}
