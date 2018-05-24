package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.Set;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;

public class NffgReaderImpl implements NffgReader {
	
	private NfvReaderImpl nfvReader;

	private NFFG nffg;
	private Set<String> nodes;

	protected NffgReaderImpl() {}
	
	protected NffgReaderImpl( NfvReaderImpl nfvReader, NFFG n, Set<String> nodes ) {
		this.nfvReader = nfvReader;
		this.nodes     = nodes;		
		this.nffg      = n;
	}
	
	protected NffgReaderImpl( NfvReaderImpl nfvReader ) {
		this.nfvReader = nfvReader;
	}
	
	
	protected void setNffg( NFFG n ) {
		this.nffg = n;
	}
	
	protected void setNodes( Set<String> nodes ) {
		this.nodes = nodes;
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
	public NodeReader getNode(String nodeName) {
		if ( !( nodes.contains(nodeName) ) )
			return null;
		
		return nfvReader.getNode( nodeName );
	}

	@Override
	public Set<NodeReader> getNodes() {
		return nfvReader.getNodes( nodes );
	}

}
