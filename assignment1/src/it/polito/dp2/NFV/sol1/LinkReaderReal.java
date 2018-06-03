package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.Link;

/**
 * An implementation of the {@link LinkReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class LinkReaderReal implements LinkReader {

	private Adapter adapter;
	private Link    link;


	protected LinkReaderReal( Adapter adapter, Link l )
			throws NullPointerException {

		if ( ( adapter == null ) || ( l == null ) )
			throw new NullPointerException("Null argument.");

		this.adapter = adapter;
		this.link    = l;
	}

	@Override
	public String getName() {
		return link.getName();
	}

	@Override
	public NodeReader getSourceNode() {
		return adapter.getNode( link.getSourceNode() );
	}

	@Override
	public NodeReader getDestinationNode() {
		return adapter.getNode( link.getDestinationNode() );
	}

	@Override
	public int getLatency() {
		if ( link.getMaxLatency() == null )
			return 0; // NOTE: link latency may be missing

		return link.getMaxLatency().getValue();
	}


	@Override
	public float getThroughput() {
		if ( link.getMinThroughput() == null )
			return 0; // NOTE: link throughput may be missing

		return link.getMinThroughput().getValue();
	}

}
