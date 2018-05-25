package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.sol1.jaxb.Connection;

public class ConnectionPerformanceReaderReal implements ConnectionPerformanceReader {
	
	private Connection connection;
	
	protected ConnectionPerformanceReaderReal() {}

	protected ConnectionPerformanceReaderReal( Connection c ) {
		this.connection = c;		
	}
	
	protected void setConnection( Connection c ) {
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
