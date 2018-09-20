package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import it.polito.dp2.NFV.ConnectionPerformanceReader;

/**
 * An implementation of the {@link ConnectionPerformanceReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealConnection extends RealNamedEntity implements ConnectionPerformanceReader {

    private final AtomicInteger latency;
    private final AtomicLong    throughput;


    // constructors


    protected RealConnection( String name, int latency, float throughput )
            throws IllegalArgumentException {

        super( name );

        /*
         * Checks
         */
        if ( latency < 0 )
            throw new IllegalArgumentException(
                    "new Connection: latency cannot be less than 0" );

        if ( throughput < 0F )
            throw new IllegalArgumentException(
                    "new Connection: throughput cannot be less than 0" );

        this.latency    = new AtomicInteger( latency );
        this.throughput = new AtomicLong( Double.doubleToLongBits( throughput ) );
    }



    // getters


    @Override
    public int getLatency() {
        return this.latency.intValue();
    }


    @Override
    public float getThroughput() {
        return (float) Double.longBitsToDouble( this.throughput.longValue() );
    }
}
