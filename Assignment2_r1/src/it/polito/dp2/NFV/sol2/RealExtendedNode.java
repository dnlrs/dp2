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

/**
 * An implementation of the {@link ExtendedNodeReader} interface.
 * <p>
 * This is a simplified implementation using the interfaces
 * provided by the NVF System. 
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealExtendedNode implements ExtendedNodeReader {

    private final Set<HostReader> reachableHosts;
    private final NodeReader      node;

    public RealExtendedNode( NodeReader node, Set<HostReader> reachableHosts ) {

        this.node = node;
        this.reachableHosts = new HashSet<HostReader>( reachableHosts );
    }

    @Override
    public Set<HostReader> getReachableHosts()
            throws NoGraphException, ServiceException {
        return this.reachableHosts;
    }

    @Override
    public VNFTypeReader getFuncType() {
        return this.node.getFuncType();
    }

    @Override
    public HostReader getHost() {
        return this.node.getHost();
    }

    @Override
    public Set<LinkReader> getLinks() {
        return this.node.getLinks();
    }

    @Override
    public NffgReader getNffg() {
        return this.node.getNffg();
    }

    @Override
    public String getName() {
        return this.node.getName();
    }

}
