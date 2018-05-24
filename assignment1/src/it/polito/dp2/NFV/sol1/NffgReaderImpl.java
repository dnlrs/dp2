package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.Set;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;

public class NffgReaderImpl implements NffgReader {
	
	private String name;
	private Calendar deployTime;
	private Set<NodeReader> nodes;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Calendar getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(Calendar deployTime) {
		this.deployTime = deployTime;
	}

	@Override
	public NodeReader getNode(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<NodeReader> getNodes() {
		return nodes;
	}

	public void setNodes(Set<NodeReader> nodes) {
		this.nodes = nodes;
	}

}
