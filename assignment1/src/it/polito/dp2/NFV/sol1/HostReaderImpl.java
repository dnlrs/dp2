package it.polito.dp2.NFV.sol1;

import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;

public class HostReaderImpl implements HostReader {
	
	private String name;
	private int    maxVNFs;
	private int    availableMemory;
	private int    availableStorage;

	private Set<NodeReader> nodes;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getAvailableMemory() {
		return availableMemory;
	}

	public void setAvailableMemory(int availableMemory) {
		this.availableMemory = availableMemory;
	}

	@Override
	public int getAvailableStorage() {
		return availableStorage;
	}

	public void setAvailableStorage(int availableStorage) {
		this.availableStorage = availableStorage;
	}

	@Override
	public int getMaxVNFs() {
		return maxVNFs;
	}

	public void setMaxVNFs(int maxVNFs) {
		this.maxVNFs = maxVNFs;
	}

	@Override
	public Set<NodeReader> getNodes() {
		return nodes;
	}

	public void setNodes(Set<NodeReader> nodes) {
		this.nodes = nodes;
	}

}
