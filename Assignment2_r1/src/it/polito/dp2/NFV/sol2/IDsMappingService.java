package it.polito.dp2.NFV.sol2;

import java.util.HashMap;

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

    private final HashMap<String, String> graphNodeIDToNodeName;
    private final HashMap<String, String> nodeNameToGraphNodeID;

    private final HashMap<String, String> graphNodeIDToHostName;
    private final HashMap<String, String> hostNameToGraphNodeID;

    private final HashMap<String, HashMap<String, String>> relIDToLinkName;
    private final HashMap<String, HashMap<String, String>> linkNameToRelID;


    protected IDsMappingService() {

        graphNodeIDToNodeName = new HashMap<String, String>();
        nodeNameToGraphNodeID = new HashMap<String, String>();

        graphNodeIDToHostName = new HashMap<String, String>();
        hostNameToGraphNodeID = new HashMap<String, String>();

        relIDToLinkName = new HashMap<String, HashMap<String, String>>();
        linkNameToRelID = new HashMap<String, HashMap<String, String>>();
    }



    // ======================================================================
    // NODES
    // ======================================================================

    protected void addNode( String graphNodeID, String nodeName )
            throws NullPointerException {

        if ( ( graphNodeID == null ) || ( nodeName == null ) )
            throw new NullPointerException( "addNode: null argument" );

        graphNodeIDToNodeName.put( graphNodeID, nodeName );
        nodeNameToGraphNodeID.put( nodeName, graphNodeID );
    }

    protected String getNodeNamefromGraphNodeID( String graphNodeID )
            throws NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getNodeNamefromGraphNodeID: null argument" );

        String result = graphNodeIDToNodeName.get( graphNodeID );
        if ( result == null )
            throw new NullPointerException( "getNodeNamefromGraphNodeID: graphNodeID not found" );

        return result;
    }


    protected String getGraphNodeIDFromNodeName( String nodeName )
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

    protected void addHost( String graphNodeID, String hostName )
            throws NullPointerException {

        if ( ( graphNodeID == null ) || ( hostName == null ) )
            throw new NullPointerException( "addHost: null argument" );

        graphNodeIDToHostName.put( graphNodeID, hostName );
        hostNameToGraphNodeID.put( hostName, graphNodeID );
    }


    protected String getHostNameFromGraphNodeID( String graphNodeID )
            throws NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException( "getHostNameFromGraphNodeID: null argument" );

        String result = graphNodeIDToHostName.get( graphNodeID );
        if ( result == null )
            throw new NullPointerException( "getHostNameFromGraphNodeID: graphNodeID not found" );

        return result;

    }


    protected String getGraphNodeIDFromHostName( String hostName )
            throws NullPointerException {

        if ( hostName == null )
            throw new NullPointerException( "getGraphNodeIDFromHostName: null argument" );

        String result = hostNameToGraphNodeID.get( hostName );
        if ( result == null )
            throw new NullPointerException( "getGraphNodeIDFromHostName: hostName not found" );

        return result;
    }


    protected boolean hostNameIsPresent( String hostName )
            throws NullPointerException {

        if ( hostName == null )
            throw new NullPointerException( "hostNameIsPresent: null agument" );

        return hostNameToGraphNodeID.containsKey( hostName );
    }




    // ======================================================================
    // LINKS
    // ======================================================================

    protected void newNffgLinkToRelMapping( String nffgName )
            throws NullPointerException {

        if ( nffgName == null )
            throw new NullPointerException( "newNffgLinkToRelMapping: null argument" );

        relIDToLinkName.put( nffgName, new HashMap<String, String>() );
        linkNameToRelID.put( nffgName, new HashMap<String, String>() );
    }


    protected void addLink( String nffgName, String linkName, String relationshipID )
            throws NullPointerException {

        if ( ( nffgName == null ) | ( linkName == null ) || ( relationshipID == null ) )
            throw new NullPointerException( "addLink: null argument" );

        if ( ( relIDToLinkName.get( nffgName ) == null ) ||
                ( linkNameToRelID.get( nffgName ) == null ) )
            throw new NullPointerException( "addLink: mapping for nffgName not found" );

        relIDToLinkName.get( nffgName ).put( relationshipID, linkName );
        linkNameToRelID.get( nffgName ).put( linkName, relationshipID );
    }


    protected String getLinkNameFromRelID( String nffgName, String relationshipID )
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


    protected String getRelIDFromLinkName( String nffgName, String linkName )
            throws NullPointerException {

        if ( ( nffgName == null ) || ( linkName == null ) )
            throw new NullPointerException( "getRelIDFromLinkName: null argument" );

        String result = linkNameToRelID.get( nffgName ).get( linkName );
        if ( result == null )
            throw new NullPointerException( "getRelIDFromLinkName: linkName not found in nffgName mapping" );

        return result;
    }
}
