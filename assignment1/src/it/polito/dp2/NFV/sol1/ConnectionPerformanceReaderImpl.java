package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.ConnectionPerformanceReader;

public class ConnectionPerformanceReaderImpl implements ConnectionPerformanceReader {
	
	private int    latency;
	private float  throughput;


	protected void setLatency(int latency) {
		this.latency = latency;
	}
	
	@Override
	public int getLatency() {
		return latency;
	}
	
	protected void setThroughput(float throughput) {
		this.throughput = throughput;
	}

	@Override
	public float getThroughput() {
		return throughput;
	}

}
