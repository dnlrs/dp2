package it.polito.dp2.NFV.sol1;

import java.math.BigInteger;
import java.util.regex.PatternSyntaxException;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.sol1.jaxb.Catalogue;
import it.polito.dp2.NFV.sol1.jaxb.Connection;
import it.polito.dp2.NFV.sol1.jaxb.Host;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork;
import it.polito.dp2.NFV.sol1.jaxb.Link;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType.DeployedNFFGs;
import it.polito.dp2.NFV.sol1.jaxb.Node;
import it.polito.dp2.NFV.sol1.jaxb.NodeRef;
import it.polito.dp2.NFV.sol1.jaxb.SizeInMB;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

/**
 * Simple validator that checks for each object returned from JAXB if all
 * compulsory properties are non-null (i.e. present) and if some are
 * correctly set.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class XMLValidator {

	private final static String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*";

	protected XMLValidator() {}

	/**
	 * Checks if the {@link NFVSystemType} is not <code>null</code> and has
	 * the elements {@link InfrastructureNetwork}, {@link Catalogue} and
	 * {@link DeployedNFFGs}.
	 *
	 * @param  nfv the {@link NFVSystemType} object
	 * @return     <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidNFVSystem( NFVSystemType nfv ) {

		if ( nfv == null )
			return false; // invalid argument

		if ( nfv.getIN() == null )
			return false; // system doesn't have Infrastructure Network element

		if ( nfv.getCatalogue() == null )
			return false; // system doesn't have Catalogue element

		if ( nfv.getDeployedNFFGs() == null )
			return false; // system doesn't have deployedNFFGs element

		return true;
	}


	/**
	 * Checks if {@link InfrastructureNetwork} is not <code>null</code> and
	 * has the elements {@link InfrastructureNetwork.Hosts} and
	 * {@link InfrastructureNetwork.Connections}.
	 *
	 * @param  in the {@link InfrastructureNetwork} object
	 * @return    <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidIN( InfrastructureNetwork in ) {

		if ( in == null )
			return false; // invalid argument

		if ( in.getHosts() == null )
			return false; // in doesn't have hosts

		if ( in.getConnections() == null )
			return false; // in doesn't have connections

		return true;
	}


	/**
	 * A {@link Host} is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> name is not null
	 * <li> name is valid
	 * <li> installedMemory is not null and valid
	 * <li> installedStorage is not null and valid
	 * <li> memory value > 0
	 * <li> storage value > 0
	 * <li> storage value > 0
	 * <li> max VNFs value > 0
	 * <li> allocatedNodes object is not null
	 * </ul>
	 *
	 * @param  h the {@link Host} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidHost( Host h ) {

		if ( h == null )
			return false; // invalid argument

		if ( h.getName() == null )
			return false; // null name

		try {
			if ( !( h.getName().matches( NAME_REGEX ) ) )
				return false; // invalid name
		} catch ( PatternSyntaxException e ) {}

		SizeInMB s_am = h.getInstalledMemory();
		SizeInMB s_as = h.getInstalledStorage();

		if ( ( s_am == null ) || ( s_as == null ) )
			return false; // host doesn't have installed Memory and installed Storage

		BigInteger am = s_am.getValue();
		BigInteger as = s_as.getValue();

		if ( ( am == null ) || ( as == null ) )
			return false; // no values for available memory or storage

		if ( ( am.intValue() < 0 ) || ( as.intValue() < 0 ) )
			return false; // wrong values for available memory or storage

		if ( h.getMaxVNFs() < 0 )
			return false; // wrong value for maxVNFs

		if ( h.getAllocatedNodes() == null )
			return false; // invalid allocated nodes

		return true; // host is valid
	}


	/**
	 * A {@link NodeRef} is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> node name is not null
	 * <li> node name is valid
	 * <li> NFFG name is not null
	 * <li> NFFG name is valid
	 * </ul>
	 *
	 * @param  nr a {@link NodeRef} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidNodeRef( NodeRef nr ) {

		if ( nr == null )
			return false; // invalid argument

		if ( nr.getName() == null )
			return false; // invalid node name

		try {
			if ( !( nr.getName().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( nr.getAssociatedNFFG() == null )
			return false; // invalid associated nffg

		try {
			if ( !( nr.getAssociatedNFFG().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		return true; // NodeRef is valid

	}

	/**
	 * A {@link Connection} is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> connection ID is not null
	 * <li> source and destination host in connection ID are not null
	 * <li> source and destination host in connection ID are valid names
	 * <li> average throughput and latency are not null
	 * <li> average throughput and latency are not less than zero
	 * </ul>
	 *
	 * @param  c a {@link Connection} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidConnection( Connection c ) {

		if ( c == null )
			return false;

		if ( c.getConnectionID() == null )
			return false; // invalid connection id

		if ( c.getConnectionID().getSourceHost() == null )
			return false; // invalid source end point

		if ( c.getConnectionID().getDestinationHost() == null )
			return false; // invalid destination end point

		try {
			if ( !( c.getConnectionID().getSourceHost().matches( NAME_REGEX ) ) ||
				 !( c.getConnectionID().getDestinationHost().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( ( c.getAverageThroughput() == null ) || ( c.getLatency() == null ))
			return false; // invalid throughput or latency

		if ( ( c.getAverageThroughput().getValue() < 0.0 ) ||
			 ( c.getLatency().getValue() < 0 ) )
			return false; // invalid throughput or latency values

		return true; // Connection is valid
	}

	/**
	 * A VNF is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> VNF name is not null
	 * <li> VNF name is valid
	 * <li> functional type is not null and is valid
	 * <li> required memory and storage are not null
	 * <li> required memory and storage values are valid
	 * </ul>
	 *
	 * @param v a {@link VNF} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidVNF( VNF v ) {

		if ( v == null )
			return false; // invalid argument

		if ( v.getName() == null )
			return false; // invalid vnf name

		try {
			if ( !( v.getName().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( v.getFunctionalType() == null )
			return false;

		boolean exists = true;
		try {
			@SuppressWarnings("unused")
			FunctionalType t = FunctionalType.valueOf( v.getFunctionalType().value() );
		} catch ( IllegalArgumentException e ) {
			exists = false;
		}

		if ( !exists )
			return false;

		if ( ( v.getRequiredMemory() == null ) || ( v.getRequiredStorage() == null ) )
			return false; // missing required memory or storage

		if ( ( v.getRequiredMemory().getValue()  == null ) ||
			 ( v.getRequiredStorage().getValue() == null )	)
			return false; // invalid required memory or storage objects

		if ( ( v.getRequiredMemory().getValue().intValue() < 0 ) ||
			 ( v.getRequiredStorage().getValue().intValue() < 0 ) )
			return false; // invalid required memory or storage value

		return true; // VNF is valid

	}

	/**
	 * An NFFG is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> NFFG name is not null
	 * <li> NFFG name is valid
	 * <li> deployTime is not null
	 * <li> nodes object is not null
	 * </ul>
	 *
	 * @param  n a {@link NFFG} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidNFFG( NFFG n ) {

		if ( n == null )
			return false; // invalid argument

		if ( n.getName() == null )
			return false; // invalid nffg names

		try {
			if ( !( n.getName().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( n.getDeployTime() == null )
			return false; // invalid nffg time

		if ( n.getNodes() == null )
			return false; // invalid nffg nodes

		return true; // NFFG is valid
	}

	/**
	 * A {@link Node} is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> node name is not null
	 * <li> node name is valid
	 * <li> hostingHost is not null
	 * <li> hostingHost name is valid
	 * <li> associated NFFG is not null
	 * <li> associated NFFG is valid
	 * <li> links object is not null
	 * <li>
	 * </ul>
	 * @param  n a {@link Node} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidNode( Node n ) {

		if ( n == null )
			return false; // invalid argument

		if ( n.getName() == null )
			return false; // invalid node names

		try {
			if ( !( n.getName().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( n.getFunctionalType() == null )
			return false; // invalid node functional type

		if ( n.getHostingHost() == null )
			return false; // invalid node hosting host

		try {
			if ( !( n.getHostingHost().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( n.getAssociatedNFFG() == null )
			return false; // invalid node belonging nffg

		try {
			if ( !( n.getAssociatedNFFG().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( n.getLinks() == null )
			return false; // missing node links

		return true; // Node is valid


	}

	/**
	 * A {@link Link} is valid if:
	 * <ul>
	 * <li> object is not null
	 * <li> link name is not null
	 * <li> link name is valid
	 * <li> source node name is not null
	 * <li> source node name is valid
	 * <li> destination node name is not null
	 * <li> destination node name is valid
	 * <li> if present, throughput > 0
	 * <li> if present, latency > 0
	 * </ul>
	 *
	 * @param  l a {@link Link} object
	 * @return <code>true</code> if valid, <code>false</code> otherwise
	 */
	protected boolean isValidLink( Link l ) {

		if ( l == null )
			return false; // invalid argument

		if ( l.getName() == null )
			return false; // invalid link name

		try {
			if ( !( l.getName().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( l.getSourceNode() == null )
			return false; // invalid source link end point

		try {
			if ( !( l.getSourceNode().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( l.getDestinationNode() == null )
			return false; // invalid destination link end point

		try {
			if ( !( l.getDestinationNode().matches( NAME_REGEX ) ) )
				return false;
		} catch ( PatternSyntaxException e ) {}

		if ( l.getMinThroughput() != null )
			if ( l.getMinThroughput().getValue() < 0.0 )
				return false; // invalid throughput

		if ( l.getMaxLatency() != null )
			if ( l.getMaxLatency().getValue() < 0 )
				return false; // invalid latency

		return true; // Link is valid
	}

}
