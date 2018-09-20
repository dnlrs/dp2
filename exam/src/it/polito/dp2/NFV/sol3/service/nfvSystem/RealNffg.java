package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;

/**
 * An implementation of the {@link NffgReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealNffg extends RealNamedEntity implements NffgReader {

    private final GregorianCalendar deployTime;
    private final HashSet<RealNode> nodes;


    private final Object lockNodes = new Object();

    // constructors

    protected RealNffg( String name, Calendar deployTime, Set<RealNode> nodes )
            throws NullPointerException, IllegalArgumentException {

        super( name );

        /*
         * Checks
         */
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
    public synchronized Set<NodeReader> getNodes() {
        return Collections.unmodifiableSet( new HashSet<NodeReader>(this.nodes) );
    }

    @Override
    public synchronized NodeReader getNode( String nodeName ) {

        if ( nodeName == null )
            return null;

        if ( !( RealNamedEntity.nameIsValid( nodeName ) ) )
            return null;

        for ( NodeReader nodeI : this.nodes )
            if ( nodeI.getName().compareTo( nodeName ) == 0 )
                return nodeI;

        return null;
    }


    protected synchronized Set<RealNode> getRealNodes() {
        return Collections.unmodifiableSet( this.nodes );
    }



    // setters


    protected synchronized void addNode( RealNode node )
            throws NullPointerException, AlreadyLoadedException {

        if ( node == null )
            throw new NullPointerException( "addNode: null argument" );

        for ( RealNode n : this.nodes )
            if ( n.getName().compareTo( node.getName() ) == 0 )
                throw new AlreadyLoadedException( "addNode: duplicate node" );

        this.nodes.add( node );
    }



    protected synchronized void removeNode( String nodeName ) {
        for ( RealNode node : this.nodes ) {
            if ( node.getName().compareTo( nodeName ) == 0 ) {
                this.nodes.remove( node );
                break;
            }
        }
    }
}
