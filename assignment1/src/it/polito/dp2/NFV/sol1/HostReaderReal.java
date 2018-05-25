package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.Host;

public class HostReaderReal implements HostReader {
	
	private NfvReaderReal nfvReader;

	private Host host;
	private Set<String> nodes;
	
	protected HostReaderReal() {}
	
	protected HostReaderReal( NfvReaderReal nSys, Host h, Set<String> nodes ) {
		this.nfvReader = nSys;
		this.nodes     = nodes;
		this.host      = h;		
	}
	
	protected HostReaderReal(NfvReaderReal nSys) {
		this.nfvReader = nSys;
	}
	
	protected void setHost( Host h ) {
		this.host = h;
	}

	protected void setNodes(Set<String> nodes) {
		this.nodes = nodes;
	}
	
	@Override
	public String getName() {
		return host.getName();
	}

	@Override
	public int getAvailableMemory() {
		return host.getInstalledMemory().getValue().intValue();
	}

	@Override
	public int getAvailableStorage() {
		return host.getInstalledStorage().getValue().intValue();
	}

	@Override
	public int getMaxVNFs() {
		return host.getMaxVNFs();
	}

	@Override
	public Set<NodeReader> getNodes() {
		return nfvReader.getNodes( nodes );
	}
}
