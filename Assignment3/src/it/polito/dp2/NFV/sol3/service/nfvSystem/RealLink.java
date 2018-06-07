package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;

/**
 * An implementation of the {@link LinkReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealLink extends RealNamedEntity implements LinkReader {

    private RealNode sourceNode;
    private RealNode destinationNode;

    private AtomicInteger latency;
    private AtomicLong    throughput;


    // constructors

    public RealLink(
            String name, RealNode sourceNode, RealNode destinationNode,
            int latency, float throughput )
                    throws NullPointerException, IllegalArgumentException {

        super( name );
        setSourceNode( sourceNode );
        setDestinationNode( destinationNode );
        setLatency( latency );
        setThroughput( throughput );
    }


    // getters


    @Override
    public NodeReader getSourceNode(){
        synchronized ( this.sourceNode ) {
            return this.sourceNode;
        }
    }


    @Override
    public NodeReader getDestinationNode() {
        synchronized ( this.destinationNode ) {
            return this.destinationNode;
        }
    }

    @Override
    public int getLatency() {
        return this.latency.intValue();
    }


    @Override
    public float getThroughput() {
        return this.throughput.floatValue();
    }



    // setters


    protected void setSourceNode( RealNode sourceNode )
            throws IllegalArgumentException {

        if ( sourceNode == null )
            throw new IllegalArgumentException( "setSourceNode: null argument" );

        synchronized ( this.sourceNode ) {
            this.sourceNode = sourceNode;
        }
    }


    protected void setDestinationNode( RealNode destinationNode )
            throws IllegalArgumentException {

        if ( destinationNode == null )
            throw new IllegalArgumentException( "setDestinationnode: null argument" );

        synchronized ( this.destinationNode ) {
            this.destinationNode = destinationNode;
        }
    }


    protected void setLatency( int latency )
            throws IllegalArgumentException {

        if ( latency < 0 )
            throw new IllegalArgumentException( "setLatency: latency cannot be less than 0" );

        this.latency = new AtomicInteger( latency );
    }

    protected void setThroughput( float throughput )
            throws IllegalArgumentException {

        if ( throughput < 0f )
            throw new IllegalArgumentException( "setThroughput: throughput cannot be less than 0" );

        this.throughput = new AtomicLong( Double.doubleToLongBits( throughput ) );
    }

}