package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.Node;

/**
 * An implementation of the {@link NodeReader} interface.
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NodeReaderReal implements NodeReader {
	
	private Adapter     adapter;
	private Node        node;
	private Set<String> links;
	
	
	protected NodeReaderReal( Adapter adapter, Node n, Set<String> links ) 
			throws NullPointerException {
		
		if ( ( adapter == null ) || ( n == null ) || ( links == null ) )
			throw new NullPointerException("Null argument.");
		
		this.adapter = adapter;
		this.node      = n;
		this.links     = links;
	}
	
	
	@Override
	public String getName() {
		return node.getName();
	}

	@Override
	public VNFTypeReader getFuncType() {
		return adapter.getVNF( node.getFunctionalType() );
	}

	@Override
	public HostReader getHost() {
		return adapter.getHost( node.getHostingHost() );
	}
	
	@Override
	public Set<LinkReader> getLinks() {
		return adapter.getLinks( node.getAssociatedNFFG(), links ); // NOTE: set may be empty
	}

	@Override
	public NffgReader getNffg() {
		return adapter.getNFFG( node.getAssociatedNFFG() );
	}
}
