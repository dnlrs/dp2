package it.polito.dp2.NFV.sol2;

import java.util.HashMap;

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
	
	protected void addNode( String graphNodeID, String nodeName ) {
		
		if ( ( graphNodeID == null ) || ( nodeName == null ) )
			throw new NullPointerException("Null Argument");
		
		graphNodeIDToNodeName.put( graphNodeID, nodeName );
		nodeNameToGraphNodeID.put( nodeName, graphNodeID );
	}
	
	protected String getNodeNamefromGraphNodeID( String graphNodeID ) {
		
		if ( graphNodeID == null )
			throw new NullPointerException();
		
		return graphNodeIDToNodeName.get( graphNodeID );
	}
	
	
	protected String getGraphNodeIDFromNodeName( String nodeName ) {
		
		if ( nodeName == null )
			throw new NullPointerException();
		
		return nodeNameToGraphNodeID.get( nodeName );
	}
	
	
	
	// ======================================================================
	// HOSTS
	// ======================================================================
	
	protected void addHost( String graphNodeID, String hostName ) {
		
		if ( ( graphNodeID == null ) || ( hostName == null ) )
			throw new NullPointerException("Null Argument");
		
		graphNodeIDToHostName.put( graphNodeID, hostName );
		hostNameToGraphNodeID.put( hostName, graphNodeID );
	}

	
	protected String getHostNameFromGraphNodeID(  String graphNodeID ) {
		
		if ( graphNodeID == null )
			throw new NullPointerException();
		
		return graphNodeIDToHostName.get( graphNodeID );
		
	}
	
	
	protected String getGraphNodeIDFromHostName( String hostName ) {
		
		if ( hostName == null )
			throw new NullPointerException();
		
		return hostNameToGraphNodeID.get( hostName );
	}
	
	
	protected boolean hostNameIsPresent( String hostName ) {
		
		if ( hostName == null )
			throw new NullPointerException();
		
		if ( hostNameToGraphNodeID.containsKey( hostName ) )
			return true;
					
		return false;
	}
	
	
	
	
	// ======================================================================
	// LINKS
	// ======================================================================
	
	protected void newNffgLinkToRelMapping( String nffgName ) {
		
		if ( nffgName == null )
			throw new NullPointerException();
		
		relIDToLinkName.put( nffgName, new HashMap<String, String>() );
		linkNameToRelID.put( nffgName, new HashMap<String, String>() );
	}
	
	
	protected void addLink( String nffgName, String linkName, String relationshipID ) {
		
		if ( ( nffgName == null ) | ( linkName == null ) || ( relationshipID == null ) )
			throw new NullPointerException();
		
		relIDToLinkName.get( nffgName ).put( relationshipID, linkName );
		linkNameToRelID.get( nffgName ).put( linkName, relationshipID );
	}
	
	
	protected String getLinkNameFromRelID( String nffgName, String relationshipID ) {
		
		if ( ( nffgName == null ) || ( relationshipID == null ) )
			throw new NullPointerException();
	
		return relIDToLinkName.get( nffgName ).get( relationshipID );
	
	}
	
	
	protected String getRelIDFromLinkName( String nffgName, String linkName ) {
		
		if ( ( nffgName == null ) || ( linkName == null ) )
			throw new NullPointerException();
		
		return linkNameToRelID.get( nffgName ).get( linkName );
	}
}
