package it.polito.dp2.NFV.sol1;

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

    private final RealNode sourceNode;
    private final RealNode destinationNode;

    private final AtomicInteger latency;
    private final AtomicLong    throughput;


    // constructors

    public RealLink(
            String name, RealNode sourceNode, RealNode destinationNode,
            int latency, float throughput )
                    throws NullPointerException, IllegalArgumentException {

        super( name );

        /*
         * Checks
         */
        if ( sourceNode == null )
            throw new IllegalArgumentException( "new Link: null argument" );

        if ( destinationNode == null )
            throw new IllegalArgumentException( "new Link: null argument" );

        if ( latency < 0 )
            throw new IllegalArgumentException(
                    "new Link: latency cannot be less than 0" );

        if ( throughput < 0F )
            throw new IllegalArgumentException(
                    "new Link: throughput cannot be less than 0" );

        this.sourceNode      = sourceNode;
        this.destinationNode = destinationNode;

        this.latency    = new AtomicInteger( latency );
        this.throughput = new AtomicLong( Double.doubleToLongBits( throughput ) );
    }


    // getters


    @Override
    public NodeReader getSourceNode(){
        return this.sourceNode;
    }


    @Override
    public NodeReader getDestinationNode() {
        return this.destinationNode;
    }

    @Override
    public int getLatency() {
        return this.latency.intValue();
    }


    @Override
    public float getThroughput() {
        return (float) Double.longBitsToDouble( this.throughput.longValue() );
    }
}
