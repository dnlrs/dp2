package it.polito.dp2.NFV.sol3.client2;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;

/**
 * An implementation of the {@link NffgReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealNffg extends RealNamedEntity implements NffgReader {

    private final GregorianCalendar deployTime;
    private final HashSet<RealNode> nodes;


    // constructors

    protected RealNffg( String name, Calendar deployTime, Set<RealNode> nodes )
            throws NullPointerException, IllegalArgumentException {

        super( name );

        /* Checks */
        if ( deployTime == null )
            throw new IllegalArgumentException( "new NFFG: null argument" );

        if ( nodes.contains( null ) )
            throw new NullPointerException(
                    "new NFFG: trying to add null nodes" );

        /*
         * Set deploy time
         */
        this.deployTime = new GregorianCalendar();
        this.deployTime.setTime( deployTime.getTime() );
        this.deployTime.setTimeZone( deployTime.getTimeZone() );

        /*
         * Set nodes
         */
        this.nodes = new HashSet<RealNode>( nodes );
    }


    // getters


    @Override
    public Calendar getDeployTime() {
        return this.deployTime;
    }

    @Override
    public Set<NodeReader> getNodes() {
        return new HashSet<NodeReader> ( this.nodes );
    }

    @Override
    public NodeReader getNode( String nodeName ) {

        if ( nodeName == null )
            return null;

        if ( !( RealNamedEntity.nameIsValid( nodeName ) ) )
            return null;

        for ( NodeReader nodeI : this.nodes )
            if ( nodeI.getName().compareTo( nodeName ) == 0 )
                return nodeI;

        return null;
    }


    protected Set<RealNode> getRealNodes() {
        return new HashSet<RealNode>( this.nodes );
    }


    // setters

    protected void addNode( RealNode node )
            throws NullPointerException {

        if ( node == null )
            throw new NullPointerException( "addNode: null argument" );

        for ( RealNode n : this.nodes )
            if ( n.getName().compareTo( node.getName() ) == 0 )
                throw new NullPointerException( "addNode: duplicate node" );

        this.nodes.add( node );
    }

    protected void removeNode( String nodeName ) {
        for ( RealNode node : this.nodes ) {
            if ( node.getName().compareTo( nodeName ) == 0 ) {
                this.nodes.remove( node );
                break;
            }
        }
    }
}
