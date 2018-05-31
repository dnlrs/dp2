package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.Host;


/**
 * An implementation of the {@link HostReader} interface.
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class HostReaderReal implements HostReader {
	
	private final Adapter adapter;
	private final Host    host;
	
	private final Set<String> nodes;
	

	protected HostReaderReal( Adapter adapter, Host h, Set<String> nodes ) 
			throws NullPointerException {
		
		if ( ( adapter == null ) || ( h == null ) || ( nodes == null ) )
			throw new NullPointerException( "HostReaderReal: null argument" );
		
		this.adapter = adapter;
		this.nodes   = nodes;
		this.host    = h;		
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
		return adapter.getNodes( nodes ); // NOTE: set may be empty
	}
}
