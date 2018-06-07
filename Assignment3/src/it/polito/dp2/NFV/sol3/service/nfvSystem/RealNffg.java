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

    private GregorianCalendar deployTime;

    private CopyOnWriteArraySet<RealNode> nodes;



    // constructors

    protected RealNffg( String name, Calendar deployTime, Set<RealNode> nodes )
            throws NullPointerException, IllegalArgumentException {

        super( name );
        this.setDeployTime( deployTime );
        this.setNodes( nodes );
    }


    // getters


    @Override
    public Calendar getDeployTime() {
        synchronized ( this.deployTime ) {
            return this.deployTime;
        }
    }

    @Override
    public synchronized Set<NodeReader> getNodes() {
        return new HashSet<NodeReader> ( this.nodes );
    }

    @Override
    public NodeReader getNode( String nodeName ) {

        if ( nodeName == null )
            return null;

        if ( !( RealNamedEntity.nameIsValid( nodeName ) ) )
            return null;

        synchronized ( this.nodes ) {
            for ( NodeReader nodeI : this.nodes )
                if ( nodeI.getName().compareTo( nodeName ) == 0 )
                    return nodeI;
        }

        return null;
    }


    protected synchronized Set<RealNode> getRealNodes() {
        return new HashSet<RealNode>( this.nodes );
    }



    // setters


    protected synchronized void setNodes( Set<RealNode> nodes )
            throws NullPointerException {
        this.nodes = new CopyOnWriteArraySet<RealNode>( nodes );
    }

    protected void addNode( RealNode node )
            throws NullPointerException {

        if ( node == null )
            throw new NullPointerException( "addNode: null argument" );

        synchronized (this.nodes) {
            this.nodes.add( node );
        }

    }


    protected void setDeployTime( Calendar deployTime )
            throws IllegalArgumentException {

        if ( deployTime == null )
            throw new IllegalArgumentException( "setDeploytime: null argument" );
        synchronized ( this.deployTime ) {
            this.deployTime = new GregorianCalendar();
            this.deployTime.setTime( deployTime.getTime() );
            this.deployTime.setTimeZone( deployTime.getTimeZone() );
        }
    }


}
