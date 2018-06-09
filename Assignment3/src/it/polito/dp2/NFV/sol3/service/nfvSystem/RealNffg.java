package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;

/**
 * An implementation of the {@link NffgReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealNffg extends RealNamedEntity implements NffgReader {

    private final GregorianCalendar             deployTime;
    private final CopyOnWriteArraySet<RealNode> nodes;

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
        this.nodes = new CopyOnWriteArraySet<RealNode>( nodes );
    }


    // getters


    @Override
    public Calendar getDeployTime() {
        return this.deployTime;
    }

    @Override
    public Set<NodeReader> getNodes() {
        synchronized ( this.lockNodes ) {
            return new HashSet<NodeReader> ( this.nodes );
        }
    }

    @Override
    public NodeReader getNode( String nodeName ) {

        if ( nodeName == null )
            return null;

        if ( !( RealNamedEntity.nameIsValid( nodeName ) ) )
            return null;

        synchronized ( this.lockNodes ) {
            for ( NodeReader nodeI : this.nodes )
                if ( nodeI.getName().compareTo( nodeName ) == 0 )
                    return nodeI;
        }

        return null;
    }


    protected Set<RealNode> getRealNodes() {
        synchronized ( this.lockNodes ) {
            return new HashSet<RealNode>( this.nodes );
        }
    }



    // setters
    protected void addNode( RealNode node )
            throws NullPointerException {

        if ( node == null )
            throw new NullPointerException( "addNode: null argument" );

        synchronized ( this.lockNodes ) {

            for ( RealNode n : this.nodes )
                if ( n.getName().compareTo( node.getName() ) == 0 )
                    throw new NullPointerException( "addNode: duplicate node" );

            this.nodes.add( node );
        }
    }
}
