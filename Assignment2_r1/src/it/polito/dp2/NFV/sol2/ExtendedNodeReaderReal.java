package it.polito.dp2.NFV.sol2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab2.ExtendedNodeReader;
import it.polito.dp2.NFV.lab2.NoGraphException;
import it.polito.dp2.NFV.lab2.ServiceException;

public class ExtendedNodeReaderReal implements ExtendedNodeReader {
	
	private Set<HostReader> reachableHosts;
	private NodeReader      node;
	
	public ExtendedNodeReaderReal(NodeReader node, Set<HostReader> reachableHosts) {
		
		this.node = node;
		this.reachableHosts = new HashSet<HostReader>(reachableHosts);
	}
	
	@Override
	public Set<HostReader> getReachableHosts() throws NoGraphException, ServiceException {
		return reachableHosts;
	}

	@Override
	public VNFTypeReader getFuncType() {
		return node.getFuncType();
	}

	@Override
	public HostReader getHost() {
		return node.getHost();
	}

	@Override
	public Set<LinkReader> getLinks() {
		return node.getLinks();
	}

	@Override
	public NffgReader getNffg() {
		return node.getNffg();
	}

	@Override
	public String getName() {
		return node.getName();
	}

}
