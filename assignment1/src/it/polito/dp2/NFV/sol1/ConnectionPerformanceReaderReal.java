package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.sol1.jaxb.Connection;

/**
 * An implementation of the {@link ConnectionPerformanceReader} interface.
 * 
 * @author   Daniel C. Rusu
 * @stuentID 234428
 */
public class ConnectionPerformanceReaderReal implements ConnectionPerformanceReader {
	
	private final Connection connection;
	

	protected ConnectionPerformanceReaderReal( Connection c ) 
			throws NullPointerException {
		
		if ( c == null )
			throw new NullPointerException( "ConnectionPerformanceReaderReal: null argument" );
		
		this.connection = c;		
	}
	
	@Override
	public int getLatency() {
		return connection.getLatency().getValue();
	}

	@Override
	public float getThroughput() {
		return connection.getAverageThroughput().getValue();
	}

}
