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

    private AtomicInteger latency;
    private AtomicLong    throughput;


    // constructors


    protected RealConnection( String name, int latency, float throughput )
            throws IllegalArgumentException {

        super( name );
        setLatency( latency );
        setThroughput( throughput );
    }



    // getters


    @Override
    public int getLatency() {

        return this.latency.intValue();
    }


    @Override
    public float getThroughput() {

        return this.throughput.floatValue();
    }


    // setters



    protected void setLatency( int latency ) {

        if ( latency < 0 )
            throw new IllegalArgumentException( "setLatency: latency cannot be less than 0" );

        this.latency = new AtomicInteger( latency );
    }


    protected void setThroughput( float throughput ) {
        if ( throughput < 0F )
            throw new IllegalArgumentException( "setThroughput: throughput cannot be less than 0" );

        this.throughput = new AtomicLong( Double.doubleToLongBits( throughput ) );
    }

}
