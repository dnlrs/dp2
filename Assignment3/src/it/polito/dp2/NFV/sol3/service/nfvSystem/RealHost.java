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

    private final AtomicInteger availableMemory;
    private final AtomicInteger availableStorage;
    private final AtomicInteger maxVNFs;

    private final AtomicInteger usedMemory;
    private final AtomicInteger usedStorage;
    private final AtomicInteger usedVNFs;

    private final CopyOnWriteArraySet<RealNode> nodes;

    private final Object lockNodes = new Object();


    // constructors


    protected RealHost(
            String name, int availableMemory, int availableStorage,
            int maxVNFs, Set<RealNode> nodes )
                    throws NullPointerException, IllegalArgumentException {

        super( name );

        /*
         * Checks
         */
        if ( availableMemory < 0 )
            throw new IllegalArgumentException(
                    "new Host: memory cannot be less than 0" );
        if ( availableStorage < 0 )
            throw new IllegalArgumentException(
                    "new Host: storage cannot be less than 0" );
        if ( maxVNFs < 0 )
            throw new IllegalArgumentException(
                    "new Host: maxVNFs cannot be less than 0" );

        /*
         * Set available memory
         */
        this.availableMemory = new AtomicInteger( availableMemory );
        this.usedMemory      = new AtomicInteger( 0 );

        /*
         * Set available storage
         */
        this.availableStorage = new AtomicInteger( availableStorage );
        this.usedStorage      = new AtomicInteger( 0 );


        /*
         * Set max VNFs
         */
        this.maxVNFs  = new AtomicInteger( maxVNFs );
        this.usedVNFs = new AtomicInteger( 0 );


        /*
         * Check Nodes
         */
        if ( nodes.contains( null ) )
            throw new NullPointerException(
                    "new Host: trying to add null nodes" );

        if ( nodes.size() > this.maxVNFs.get() )
            throw new IllegalArgumentException(
                    "new Host: too many nodes for host" );

        int requiredMemory  = 0;
        int requiredStorage = 0;
        for ( RealNode node : nodes ) {
            requiredMemory  += node.getFuncType().getRequiredMemory();
            requiredStorage += node.getFuncType().getRequiredStorage();
        }
        if ( requiredMemory > this.availableMemory.get() )
            throw new IllegalArgumentException(
                    "new Host: too much memory required" );

        if ( requiredStorage > this.availableStorage.get() )
            throw new IllegalArgumentException(
                    "new Host: too much storage required" );

        this.nodes = new CopyOnWriteArraySet<RealNode>( nodes );

        this.usedMemory.set( requiredMemory );
        this.usedStorage.set( requiredStorage );
        this.usedVNFs.set( nodes.size() );

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


    protected void addNode( RealNode node )
            throws IllegalArgumentException {

        if ( node == null )
            throw new IllegalArgumentException( "addNode: null argument" );

        int requiredMemory  = node.getFuncType().getRequiredMemory();
        int requiredStorage = node.getFuncType().getRequiredStorage();

        synchronized ( this.lockNodes ) {

            for ( RealNode n : this.nodes )
                if ( n.getName().compareTo( node.getName() ) == 0 )
                    throw new NullPointerException( "addNode: duplicate node" );

            if ( (this.usedMemory.get()  + requiredMemory)  > this.availableMemory.get() )
                throw new IllegalArgumentException(
                        "addNode: no memory available" );

            if ( (this.usedStorage.get() + requiredStorage) > this.availableStorage.get() )
                throw new IllegalArgumentException(
                        "addNode: no storage available" );

            if ( this.usedVNFs.get() == this.maxVNFs.get() )
                throw new IllegalArgumentException(
                        "addNode: host has no VNF slots available" );

            if ( this.nodes.contains( node ) )
                throw new NullPointerException( "addNode: duplicate node" );

            this.nodes.add( node );

            this.usedMemory.addAndGet( requiredMemory );
            this.usedStorage.addAndGet( requiredStorage );
            this.usedVNFs.incrementAndGet();
        }
    }
}
