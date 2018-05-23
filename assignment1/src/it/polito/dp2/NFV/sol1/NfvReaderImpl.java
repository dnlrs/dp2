package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.VNFTypeReader;

public class NfvReaderImpl implements NfvReader {

	@Override
	public ConnectionPerformanceReader getConnectionPerformance(HostReader arg0, HostReader arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HostReader getHost(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HostReader> getHosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NffgReader getNffg(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<NffgReader> getNffgs(Calendar arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

}
