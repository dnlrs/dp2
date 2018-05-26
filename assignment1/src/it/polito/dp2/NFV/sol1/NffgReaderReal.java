package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.Set;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;

/**
 * An implementation of the {@link NffgReader} interface.
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NffgReaderReal implements NffgReader {
	
	private Adapter     adapter;
	private NFFG        nffg;
	private Set<String> nodes;

	
	protected NffgReaderReal( Adapter adapter, NFFG n, Set<String> nodes ) 
			throws NullPointerException {
		
		if ( ( adapter == null ) || ( n == null ) || ( nodes == null ) )
			throw new NullPointerException("Null argument.");

		this.adapter = adapter;
		this.nffg    = n;
		this.nodes   = nodes;		
	}
	
	
	@Override
	public String getName() {
		return nffg.getName();
	}

	
	@Override
	public Calendar getDeployTime() {
		return nffg.getDeployTime().toGregorianCalendar();
	}


	@Override
	public NodeReader getNode( String nodeName ) {
		if ( ( nodeName == null ) || !( nodes.contains( nodeName ) ) )
			return null; // NOTE: null argument or node doesn't belong to this NFFG
		
		return adapter.getNode( nodeName );
	}

	@Override
	public Set<NodeReader> getNodes() {
		return adapter.getNodes( nodes ); // NOTE: set may be empty
	}

}
