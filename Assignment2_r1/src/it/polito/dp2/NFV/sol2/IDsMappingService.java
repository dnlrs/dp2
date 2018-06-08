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

    private final Object lockNodes = new Object();
    private final Object lockHosts = new Object();
    private final Object lockLinks = new Object();

    protected IDsMappingService() {

        this.graphNodeIDToNodeName = new HashMap<String, String>();
        this.nodeNameToGraphNodeID = new HashMap<String, String>();

        this.graphNodeIDToHostName = new HashMap<String, String>();
        this.hostNameToGraphNodeID = new HashMap<String, String>();

        this.relIDToLinkName = new HashMap<String, HashMap<String, String>>();
        this.linkNameToRelID = new HashMap<String, HashMap<String, String>>();
    }



    // ======================================================================
    // NODES
    // ======================================================================

    protected void addNode( String graphNodeID, String nodeName )
            throws NullPointerException {

        if ( ( graphNodeID == null ) || ( nodeName == null ) )
            throw new NullPointerException( "addNode: null argument" );

        synchronized ( this.lockNodes ) {
            this.graphNodeIDToNodeName.put( graphNodeID, nodeName );
            this.nodeNameToGraphNodeID.put( nodeName, graphNodeID );
        }
    }

    protected String getNodeNamefromGraphNodeID( String graphNodeID )
            throws NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException(
                    "getNodeNamefromGraphNodeID: null argument" );

        synchronized ( this.lockNodes ) {
            String result = this.graphNodeIDToNodeName.get( graphNodeID );
            if ( result == null )
                throw new NullPointerException(
                        "getNodeNamefromGraphNodeID: graphNodeID not found" );

            return new String( result );
        }
    }


    protected String getGraphNodeIDFromNodeName( String nodeName )
            throws NullPointerException {

        if ( nodeName == null )
            throw new NullPointerException(
                    "getGraphNodeIDFromNodeName: null argument" );

        synchronized ( this.lockNodes ) {
            String result = this.nodeNameToGraphNodeID.get( nodeName );
            if ( result == null )
                throw new NullPointerException(
                        "getGraphNodeIDFromNodeName: nodeName not found" );

            return new String( result );
        }
    }

    protected boolean nodeNameIsPresent( String nodeName ) {

        if ( nodeName == null )
            throw new NullPointerException( "nodeNameIsPresent: null agument" );

        synchronized ( this.lockNodes ) {
            return this.nodeNameToGraphNodeID.containsKey( nodeName );
        }


    }



    // ======================================================================
    // HOSTS
    // ======================================================================

    protected void addHost( String graphNodeID, String hostName )
            throws NullPointerException {

        if ( ( graphNodeID == null ) || ( hostName == null ) )
            throw new NullPointerException( "addHost: null argument" );

        synchronized ( this.lockHosts ) {
            this.graphNodeIDToHostName.put( graphNodeID, hostName );
            this.hostNameToGraphNodeID.put( hostName, graphNodeID );
        }
    }


    protected String getHostNameFromGraphNodeID( String graphNodeID )
            throws NullPointerException {

        if ( graphNodeID == null )
            throw new NullPointerException(
                    "getHostNameFromGraphNodeID: null argument" );

        synchronized ( this.lockHosts ) {
            String result = this.graphNodeIDToHostName.get( graphNodeID );
            if ( result == null )
                throw new NullPointerException(
                        "getHostNameFromGraphNodeID: graphNodeID not found" );

            return new String( result );
        }

    }


    protected String getGraphNodeIDFromHostName( String hostName )
            throws NullPointerException {

        if ( hostName == null )
            throw new NullPointerException(
                    "getGraphNodeIDFromHostName: null argument" );

        synchronized ( this.lockHosts ) {
            String result = this.hostNameToGraphNodeID.get( hostName );
            if ( result == null )
                throw new NullPointerException(
                        "getGraphNodeIDFromHostName: hostName not found" );

            return new String( result );
        }
    }


    protected boolean hostNameIsPresent( String hostName )
            throws NullPointerException {

        if ( hostName == null )
            throw new NullPointerException( "hostNameIsPresent: null agument" );

        synchronized ( this.lockHosts ) {
            return this.hostNameToGraphNodeID.containsKey( hostName );
        }
    }




    // ======================================================================
    // LINKS
    // ======================================================================

    protected void initLinksMappingForNFFG( String nffgName )
            throws NullPointerException {

        if ( nffgName == null )
            throw new NullPointerException(
                    "newNffgLinkToRelMapping: null argument" );

        synchronized ( this.lockLinks ) {
            this.relIDToLinkName.put( nffgName, new HashMap<String, String>() );
            this.linkNameToRelID.put( nffgName, new HashMap<String, String>() );
        }
    }


    protected void addLink(
            String nffgName,
            String linkName,
            String relationshipID )
            throws NullPointerException {

        if ( ( nffgName == null ) ||
                ( linkName == null ) ||
                ( relationshipID == null ) )
            throw new NullPointerException( "addLink: null argument" );

        synchronized ( this.lockLinks ) {

            if ( ( this.relIDToLinkName.get( nffgName ) == null ) ||
                    ( this.linkNameToRelID.get( nffgName ) == null ) )
                throw new NullPointerException(
                        "addLink: mapping for nffgName not found" );

            (this.relIDToLinkName.get( nffgName )).put( relationshipID, linkName );
            (this.linkNameToRelID.get( nffgName )).put( linkName, relationshipID );

        }
    }


    protected String getLinkNameFromRelID(
            String nffgName,
            String relationshipID )
                    throws NullPointerException {

        if ( ( nffgName == null ) || ( relationshipID == null ) )
            throw new NullPointerException(
                    "getLinkNameFromRelID: null argument" );

        synchronized ( this.lockLinks ) {

            if ( this.relIDToLinkName.get( nffgName ) == null )
                throw new NullPointerException(
                        "getLinkNameFromRelID: mapping for nffgName not found" );

            String result = (this.relIDToLinkName.get( nffgName )).get( relationshipID );
            if ( result == null )
                throw new NullPointerException(
                        "getLinkNameFromRelID: relationshipID not found" );

            return new String( result );

        }
    }


    protected String getRelIDFromLinkName( String nffgName, String linkName )
            throws NullPointerException {

        if ( ( nffgName == null ) || ( linkName == null ) )
            throw new NullPointerException(
                    "getRelIDFromLinkName: null argument" );

        synchronized ( this.lockLinks ) {

            if ( this.linkNameToRelID.get( nffgName ) == null )
                throw new NullPointerException(
                        "getRelIDFromLinkName: mapping for nffgName not found" );

            String result = this.linkNameToRelID.get( nffgName ).get( linkName );
            if ( result == null )
                throw new NullPointerException(
                        "getRelIDFromLinkName: linkName not found" );

            return new String( result );

        }
    }
}
