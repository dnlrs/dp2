package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;


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

    private final HashSet<RealNode> nodes;


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


        this.nodes = new HashSet<RealNode>( nodes );
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
        return Collections.unmodifiableSet( this.nodes );
    }

    // setters


    protected synchronized void addNode( RealNode node )
            throws IllegalArgumentException, AlreadyLoadedException {

        if ( node == null )
            throw new IllegalArgumentException( "addNode: null argument" );

        int requiredMemory  = node.getFuncType().getRequiredMemory();
        int requiredStorage = node.getFuncType().getRequiredStorage();


        if ( this.nodes.contains( node ) )
            throw new AlreadyLoadedException( "addNode: duplicate node" );

        for ( RealNode n : this.nodes )
            if ( n.getName().compareTo( node.getName() ) == 0 )
                throw new AlreadyLoadedException( "addNode: duplicate node" );

        if ( (this.usedMemory.get()  + requiredMemory)  > this.availableMemory.get() )
            throw new IllegalArgumentException( "addNode: no memory available" );

        if ( (this.usedStorage.get() + requiredStorage) > this.availableStorage.get() )
            throw new IllegalArgumentException( "addNode: no storage available" );

        if ( this.usedVNFs.get() == this.maxVNFs.get() )
            throw new IllegalArgumentException( "addNode: host has no VNF slots available" );

        this.nodes.add( node );

        this.usedMemory.addAndGet( requiredMemory );
        this.usedStorage.addAndGet( requiredStorage );
        this.usedVNFs.incrementAndGet();
    }

    protected synchronized void removeNode( String nodeName ) {

        for ( RealNode node : this.nodes ) {
            if ( node.getName().compareTo( nodeName ) == 0 ) {
                this.nodes.remove( node );

                this.usedMemory.addAndGet( -(node.getFuncType().getRequiredMemory()) );
                this.usedStorage.addAndGet( -(node.getFuncType().getRequiredStorage()) );
                this.usedVNFs.addAndGet( -1 );
                break;
            }
        }
    }

    /**
     * Checks if there is a VNF slot available and if there is enough memory
     * and storage as requested.
     *
     * @param requiredMemory
     * @param requiredStorage
     * @return
     */
    protected boolean isAvailable( int requiredMemory, int requiredStorage ) {

        int freeVNFs = (this.maxVNFs.get() - this.usedVNFs.get());

        if ( freeVNFs < 1 )
            return false;

        int freeMemory  = this.availableMemory.get() - this.usedMemory.get();
        int freeStorage = this.availableStorage.get() - this.usedStorage.get();

        if ( (freeMemory < requiredMemory) || (freeStorage < requiredStorage) )
            return false;

        return true;
    }
}
