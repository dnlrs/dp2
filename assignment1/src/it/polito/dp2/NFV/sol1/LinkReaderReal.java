package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.Link;

public class LinkReaderReal implements LinkReader {

	private NfvReaderReal nfvReader;

	private Link link;
	
	protected LinkReaderReal() {}
	
	protected LinkReaderReal( NfvReaderReal nfvSystem, Link l ) {
		this.nfvReader = nfvSystem;
		this.link      = l;	
	}
	
	protected LinkReaderReal( NfvReaderReal nfvSystem ) {
		this.nfvReader = nfvSystem;
	}
	
	protected void setLink( Link l ) {
		this.link = l;
	}
	
	@Override
	public String getName() {
		return link.getName();
	}

	@Override
	public NodeReader getSourceNode() {
		return nfvReader.getNode( link.getSourceNode() );
	}

	@Override
	public NodeReader getDestinationNode() {
		return nfvReader.getNode( link.getDestinationNode() );
	}

	@Override
	public int getLatency() {
		return link.getMaxLatency().getValue();
	}


	@Override
	public float getThroughput() {
		return link.getMinThroughput().getValue();
	}

}
