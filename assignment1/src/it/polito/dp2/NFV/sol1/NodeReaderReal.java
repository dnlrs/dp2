package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.Node;

public class NodeReaderReal implements NodeReader {
	
	private NfvReaderReal nfvReader;

	private Node node;
	private Set<String> links;
	
	protected NodeReaderReal() {}
	
	protected NodeReaderReal( NfvReaderReal nfvReader, Node n, Set<String> links ) {
		this.nfvReader = nfvReader;
		this.links     = links;
		this.node      = n;
	}
	
	protected NodeReaderReal( NfvReaderReal nfvReader ) {
		this.nfvReader = nfvReader;
	}
	
	protected void setLinks( Set<String> links ) {
		this.links = links;
	}
	
	protected void setNode( Node n ) {
		this.node = n;
	}
	
	@Override
	public String getName() {
		return node.getName();
	}

	@Override
	public VNFTypeReader getFuncType() {
		return nfvReader.getVNF( node.getFunctionalType() );
	}

	@Override
	public HostReader getHost() {
		return nfvReader.getHost( node.getHostingHost() );
	}

	@Override
	public Set<LinkReader> getLinks() {
		return nfvReader.getLinks( node.getAssociatedNFFG(), links );
	}

	@Override
	public NffgReader getNffg() {
		return nfvReader.getNffg( node.getAssociatedNFFG() );
	}
}
