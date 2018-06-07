package it.polito.dp2.NFV.sol3.service.database;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * This class keeps in memory a mapping between the graph nodes ID on the
 * Neo4J Simple WebService and the real names of Nodes, Hosts and Links
 * in the local NFV System.
 * <p>
 * For each entity the mapping is done with two HashMaps, this is because
 * if only one was used, if the two sets of entities were not disjoint,
 * i.e. graph node IDs may have been the same as Node names, Host names or
 * Link names, the mapping wouldn't have worked as expected.
 * <p>
 * Methods in this class allow to convert graph node IDs into entity name
 * and vice versa.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class IDsMappingService {

    private final ConcurrentHashMap<String, String> graphNodeIDToNodeName;
    private final ConcurrentHashMap<String, String> nodeNameToGraphNodeID;

    private final ConcurrentHashMap<String, String> graphNodeIDToHostName;
    private final ConcurrentHashMap<String, String> hostNameToGraphNodeID;

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> relIDToLinkName;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> linkNameToRelID;

    private final ConcurrentSkipListSet<String> loadedNFFGs;
    private static IDsMappingService instance = null;

    public synchronized static IDsMappingService getInstance() {

        if ( instance == null ) {
            instance = new IDsMappingService();
        }

        return instance;
    }

    protected IDsMappingService() {

        graphNodeIDToNodeName = new ConcurrentHashMap<String, String>();
        nodeNameToGraphNodeID = new ConcurrentHashMap<String, String>();

        graphNodeIDToHostName = new ConcurrentHashMap<String, String>();
        hostNameToGraphNodeID = new ConcurrentHashMap<String, String>();

        relIDToLinkName = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
        linkNameToRelID = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();

        loadedNFFGs = new ConcurrentSkipListSet<String>();
    }



    // ======================================================================
    // NODES
    // ======================================================================

    public synchronized
    void addNode( String graphNodeID, String nodeName )
            throws NullPointerException {

        if ( ( graphNodeID == null ) || ( nodeName == null ) )
            throw new NullPointerException( "addNode: null argument" );

        graphNodeIDToNodeName.put( graphNodeID, nodeName );
        nodeNameToGraphNodeID.put( nodeName, graphNodeID );
    }

    public synchronized
    String getNodeNamefromGraphNodeID( String graphNodeID )
            throws NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeNamefromGraphNodeID: null argument" );

        String result = graphNodeIDToNodeName.get( graphNodeID );
        if ( result == null )
            throw new NullPointerException( "getNodeNamefromGraphNodeID: graphNodeID not found" );

        return result;
    }


    public synchronized
    String getGraphNodeIDFromNodeName( String nodeName )
            throws NullPointerException {

        if ( nodeName == null )
            throw new NullPointerException( "getGraphNodeIDFromNodeName: null argument" );

        String result = nodeNameToGraphNodeID.get( nodeName );
        if ( result == null )
            throw new NullPointerException( "getGraphNodeIDFromNodeName: nodeName not found" );

        return result;
    }



    // ======================================================================
    // HOSTS
    // ======================================================================

    public synchronized
    void addHost( String graphNodeID, String hostName )
            throws NullPointerException {

        if ( ( graphNodeID == null ) || ( hostName == null ) )
            throw new NullPointerException( "addHost: null argument" );

        graphNodeIDToHostName.put( graphNodeID, hostName );
        hostNameToGraphNodeID.put( hostName, graphNodeID );
    }


    public synchronized
    String getHostNameFromGraphNodeID( String graphNodeID )
            throws NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getHostNameFromGraphNodeID: null argument" );

        String result = graphNodeIDToHostName.get( graphNodeID );
        if ( result == null )
            throw new NullPointerException( "getHostNameFromGraphNodeID: graphNodeID not found" );

        return result;

    }


    public synchronized
    String getGraphNodeIDFromHostName( String hostName )
            throws NullPointerException {

        if ( hostName == null )
            throw new NullPointerException( "getGraphNodeIDFromHostName: null argument" );

        String result = hostNameToGraphNodeID.get( hostName );
        if ( result == null )
            throw new NullPointerException( "getGraphNodeIDFromHostName: hostName not found" );

        return result;
    }


    public synchronized
    boolean hostNameIsPresent( String hostName )
            throws NullPointerException {

        if ( hostName == null )
            throw new NullPointerException( "hostNameIsPresent: null agument" );

        return hostNameToGraphNodeID.containsKey( hostName );
    }




    // ======================================================================
    // LINKS
    // ======================================================================

    public synchronized
    void newNffgLinkToRelMapping( String nffgName )
            throws NullPointerException {

        if ( nffgName == null )
            throw new NullPointerException( "newNffgLinkToRelMapping: null argument" );

        relIDToLinkName.put( nffgName, new ConcurrentHashMap<String, String>() );
        linkNameToRelID.put( nffgName, new ConcurrentHashMap<String, String>() );
    }


    public synchronized
    void addLink( String nffgName, String linkName, String relationshipID )
            throws NullPointerException {

        if ( ( nffgName == null ) | ( linkName == null ) || ( relationshipID == null ) )
            throw new NullPointerException( "addLink: null argument" );

        if ( ( relIDToLinkName.get( nffgName ) == null ) ||
                ( linkNameToRelID.get( nffgName ) == null ) )
            throw new NullPointerException( "addLink: mapping for nffgName not found" );

        relIDToLinkName.get( nffgName ).put( relationshipID, linkName );
        linkNameToRelID.get( nffgName ).put( linkName, relationshipID );
    }


    public synchronized
    String getLinkNameFromRelID( String nffgName, String relationshipID )
            throws NullPointerException {

        if ( ( nffgName == null ) || ( relationshipID == null ) )
            throw new NullPointerException( "getLinkNameFromRelID: null argument" );

        if ( relIDToLinkName.get( nffgName ) == null )
            throw new NullPointerException( "getLinkNameFromRelID: mapping for nffgName not found" );

        String result = relIDToLinkName.get( nffgName ).get( relationshipID );
        if ( result == null )
            throw new NullPointerException( "getLinkNameFromRelID: relationshipID not found in nffgName mapping" );

        return result;

    }


    public synchronized
    String getRelIDFromLinkName( String nffgName, String linkName )
            throws NullPointerException {

        if ( ( nffgName == null ) || ( linkName == null ) )
            throw new NullPointerException( "getRelIDFromLinkName: null argument" );

        String result = linkNameToRelID.get( nffgName ).get( linkName );
        if ( result == null )
            throw new NullPointerException( "getRelIDFromLinkName: linkName not found in nffgName mapping" );

        return result;
    }

    public synchronized
    void addLoadedNFFG( String nffgName )
            throws NullPointerException {

        if ( nffgName == null )
            throw new NullPointerException( "addLoadedNFFG: null argument" );

        loadedNFFGs.add( nffgName );
    }

    public boolean isLoadedNFFG( String nffgName )
            throws NullPointerException {

        if ( nffgName == null )
            throw new NullPointerException( "isLoadedNFFG: null argument" );

        return loadedNFFGs.contains( nffgName );
    }
}
