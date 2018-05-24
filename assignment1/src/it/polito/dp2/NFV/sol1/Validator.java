package it.polito.dp2.NFV.sol1;

import java.math.BigInteger;

import it.polito.dp2.NFV.sol1.jaxb.Connection;
import it.polito.dp2.NFV.sol1.jaxb.Host;
import it.polito.dp2.NFV.sol1.jaxb.Link;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;
import it.polito.dp2.NFV.sol1.jaxb.Node;
import it.polito.dp2.NFV.sol1.jaxb.NodeRef;
import it.polito.dp2.NFV.sol1.jaxb.SizeInMB;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

public class Validator {
	
	protected Validator() {}
	
	protected Boolean isValidHost( Host h ) {
		
		if ( h == null )
			return Boolean.FALSE; // invalid argument
		
		if ( h.getName() == null )
			return Boolean.FALSE; // invalid name
		
		SizeInMB s_am = h.getInstalledMemory();
		SizeInMB s_as = h.getInstalledStorage();
		
		if ( ( s_am == null ) || ( s_as == null ) )
			return Boolean.FALSE; // host doesn't have installed Memory and installed Storage
		
		BigInteger am = s_am.getValue();
		BigInteger as = s_as.getValue();
		
		if ( ( am.intValue() < 0 ) || ( as.intValue() < 0 ) )
			return Boolean.FALSE; // wrong values for available memory or storage
		
		if ( h.getMaxVNFs() < 0 )
			return Boolean.FALSE; // wrong value for maxVNFs
		
		if ( h.getAllocatedNodes() == null )
			return Boolean.FALSE; // invalid allocated nodes
		
		return Boolean.TRUE; // host is valid
	}
	
	
	protected Boolean isValidNodeRef( NodeRef nr ) {
		
		if ( nr == null )
			return Boolean.FALSE; // invalid argument
		
		if ( nr.getName() == null )
			return Boolean.FALSE; // invalid node name
		
		if ( nr.getAssociatedNFFG() == null )
			return Boolean.FALSE; // invalid associated nffg
		
		return Boolean.TRUE; // NodeRef is valid
		
	}
	
	protected Boolean isValidConnection( Connection c ) {
		
		if ( c == null )
			return Boolean.FALSE;
		
		if ( c.getConnectionID() == null )
			return Boolean.FALSE; // invalid connection id
		
		if ( c.getConnectionID().getSourceHost() == null )
			return Boolean.FALSE; // invalid source end point
		
		if ( c.getConnectionID().getDestionationHost() == null )
			return Boolean.FALSE; // invalid destination end point
		
		if ( ( c.getAverageThroughput() == null ) || ( c.getLatency() == null ))
			return Boolean.FALSE; // invalid throughput or latency
		
		if ( ( c.getAverageThroughput().getValue() < 0.0 ) ||
			 ( c.getLatency().getValue() < 0 ))
			return Boolean.FALSE; // invalid throughput or latency values
		
		
		return Boolean.TRUE; // Connection is valid
	}
	
	protected Boolean isValidVNF( VNF v ) {
		
		if ( v == null )
			return Boolean.FALSE; // invalid argument
		
		if ( v.getName() == null )
			return Boolean.FALSE; // invalid vnf name
		
		if ( ( v.getRequiredMemory() == null ) || ( v.getRequiredStorage() == null ) )
			return Boolean.FALSE; // invalid required memory or storage
		
		if ( ( v.getRequiredMemory().getValue().intValue() < 0 ) || 
			 ( v.getRequiredStorage().getValue().intValue() < 0 ) )
			return Boolean.FALSE; // invalid required memory or storage value
		
		return Boolean.TRUE; // VNF is valid
		
	}
	
	protected Boolean isValidNFFG( NFFG n ) {
		
		if ( n == null )
			return Boolean.FALSE; // invalid argument
		
		if ( n.getName() == null )
			return Boolean.FALSE; // invalid nffg names
		
		if ( n.getDeployTime() == null )
			return Boolean.FALSE; // invalid nffg time
		
		if ( n.getNodes() == null )
			return Boolean.FALSE; // invalid nffg nodes
		
		return Boolean.TRUE; // NFFG is valid
	}
	
	protected Boolean isValidNode( Node n ) {
		
		if ( n == null )
			return Boolean.FALSE; // invalid argument
		
		if ( n.getName() == null )
			return Boolean.FALSE; // invalid node names
		
		if ( n.getFunctionalType() == null )
			return Boolean.FALSE; // invalid node functional type
		
		if ( n.getHostingHost() == null )
			return Boolean.FALSE; // invalid node hosting host
		
		if ( n.getAssociatedNFFG() == null )
			return Boolean.FALSE; // invalid node belonging nffg
		
		if ( n.getLinks() == null )
			return Boolean.FALSE; // missing node links
		
		return Boolean.TRUE; // Node is valid
			
		
	}
	
	protected Boolean isValidLink( Link l ) {
		
		if ( l == null )
			return Boolean.FALSE; // invalid argument
		
		if ( l.getName() == null )
			return Boolean.FALSE; // invalid link name
		
		if ( l.getSourceNode() == null )
			return Boolean.FALSE; // invalid source link end point
		
		if ( l.getDestinationNode() == null )
			return Boolean.FALSE; // invalid destination link end point
		
		if ( ( l.getMinThroughput() == null ) || ( l.getMaxLatency() == null ) )
			return Boolean.FALSE; // missing throughput or latency
		
		if ( ( l.getMinThroughput().getValue() < 0.0 ) || ( l.getMaxLatency().getValue() < 0 ) )
			return Boolean.FALSE; // invalid throughput or latency values
		
		return Boolean.TRUE; // Link is valid
	}

}
