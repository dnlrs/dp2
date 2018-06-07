package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;


/**
 * An implementation of the {@link HostReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealHost extends RealNamedEntity implements HostReader {

    private AtomicInteger availableMemory;
    private AtomicInteger availableStorage;
    private AtomicInteger maxVNFs;

    private AtomicInteger usedMemory;
    private AtomicInteger usedStorage;
    private AtomicInteger deployedVNFs;

    private CopyOnWriteArraySet<RealNode> nodes;


    // constructors


    protected RealHost(
            String name, int availableMemory, int availableStorage,
            int maxVNFs, Set<RealNode> nodes )
                    throws NullPointerException, IllegalArgumentException {

        super( name );
        this.setAvailableMemory( availableMemory );
        this.setAvailableStorage( availableStorage );
        this.setMaxVNFs( maxVNFs );
        this.setNodes( nodes );
    }


    // getters



    @Override
    public int getAvailableMemory() {

        return this.availableMemory.intValue();
    }

    @Override
    public int getAvailableStorage() {

        return this.availableStorage.intValue();
    }

    @Override
    public int getMaxVNFs() {

        return this.maxVNFs.intValue();
    }

    @Override
    public synchronized Set<NodeReader> getNodes() {
        return new HashSet<NodeReader>( this.nodes );
    }



    // setters



    protected void setNodes( Set<RealNode> nodes )
            throws NullPointerException, IllegalArgumentException {

        if ( nodes.contains( null ) )
            throw new NullPointerException( "setNodes: set contains null elements" );

        if ( nodes.size() > this.maxVNFs.get() )
            throw new IllegalArgumentException( "setNodes: too many nodes for host" );

        int requiredMemory  = 0;
        int requiredStorage = 0;
        for ( RealNode node : nodes ) {
            requiredMemory  += node.getFuncType().getRequiredMemory();
            requiredStorage += node.getFuncType().getRequiredStorage();
        }
        if ( ( requiredMemory > this.availableMemory.get() ) ||
             ( requiredStorage > this.availableStorage.get() ) )
            throw new IllegalArgumentException( "setNodes: too much memory or storage required" );

        synchronized ( this.nodes ) {
            this.nodes = new CopyOnWriteArraySet<RealNode>( nodes );
        }

        this.usedMemory.set(requiredMemory);
        this.usedStorage.set( requiredStorage );
        this.deployedVNFs.set( nodes.size() );

    }

    protected void addNode( RealNode node )
            throws IllegalArgumentException {

        if ( node == null )
            throw new IllegalArgumentException( "addNode: null argument" );

        int requiredMemory  = node.getFuncType().getRequiredMemory();
        int requiredStorage = node.getFuncType().getRequiredStorage();

        if ( ( ( this.usedMemory.get()  + requiredMemory )  > this.availableMemory.get() ) ||
             ( ( this.usedStorage.get() + requiredStorage ) > this.availableStorage.get() ) )
           throw new IllegalArgumentException( "addNode: no memory or storage available" );

        if ( this.deployedVNFs.get() == this.maxVNFs.get() )
            throw new IllegalArgumentException( "addNode: host has no VNF slots available" );

        synchronized ( this.nodes ) {
            this.nodes.add( node );
        }

        this.usedMemory.addAndGet( requiredMemory );
        this.usedStorage.addAndGet( requiredStorage );
        this.deployedVNFs.incrementAndGet();
    }

    protected void setAvailableMemory( int availableMemory )
            throws IllegalArgumentException {

        if ( availableMemory < 0 )
            throw new IllegalArgumentException( "setAvailableMemory: memory cannot be less than 0" );

        this.availableMemory = new AtomicInteger( availableMemory );
        this.usedMemory      = new AtomicInteger( 0 );
    }



    protected void setAvailableStorage( int availableStorage )
            throws IllegalArgumentException {

        if ( availableStorage < 0 )
            throw new IllegalArgumentException( "setAvailableStorage: storage cannot be less than 0" );

        this.availableStorage = new AtomicInteger( availableStorage );
        this.usedStorage      = new AtomicInteger( 0 );
    }



    protected void setMaxVNFs( int maxVNFs )
            throws IllegalArgumentException {

        if ( maxVNFs < 0 )
            throw new IllegalArgumentException( "setMaxVNFs: maxVNFs cannot be less than 0" );

        this.maxVNFs      = new AtomicInteger( maxVNFs );
        this.deployedVNFs = new AtomicInteger( 0 );
    }

}
