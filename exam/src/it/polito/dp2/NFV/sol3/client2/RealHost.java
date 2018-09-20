package it.polito.dp2.NFV.sol3.client2;

import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;


/**
 * An implementation of the {@link HostReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealHost extends RealNamedEntity implements HostReader {

    private final Integer availableMemory;
    private final Integer availableStorage;
    private final Integer maxVNFs;

    private Integer usedMemory;
    private Integer usedStorage;
    private Integer usedVNFs;

    private final Set<RealNode> nodes;

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
         * Set available memory / storage / maxVNFs
         */
        this.availableMemory = new Integer( availableMemory );
        this.availableStorage = new Integer( availableStorage );

        this.maxVNFs  = new Integer( maxVNFs );


        /*
         * Check Nodes
         */
        if ( nodes.contains( null ) )
            throw new NullPointerException(
                    "new Host: trying to add null nodes" );

        if ( nodes.size() > this.maxVNFs.intValue() )
            throw new IllegalArgumentException(
                    "new Host: too many nodes for host" );

        int requiredMemory  = 0;
        int requiredStorage = 0;
        for ( RealNode node : nodes ) {
            requiredMemory  += node.getFuncType().getRequiredMemory();
            requiredStorage += node.getFuncType().getRequiredStorage();
        }
        if ( requiredMemory > this.availableMemory.intValue() )
            throw new IllegalArgumentException(
                    "new Host: too much memory required" );

        if ( requiredStorage > this.availableStorage.intValue() )
            throw new IllegalArgumentException(
                    "new Host: too much storage required" );

        this.nodes = new HashSet<RealNode>( nodes );

        this.usedMemory  = new Integer( requiredMemory );
        this.usedStorage = new Integer( requiredStorage );
        this.usedVNFs    = new Integer( nodes.size() );

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
    public Set<NodeReader> getNodes() {
        return new HashSet<NodeReader>( this.nodes );
    }

    // setters


    protected void addNode( RealNode node )
            throws IllegalArgumentException {

        if ( node == null )
            throw new IllegalArgumentException( "addNode: null argument" );

        int requiredMemory  = node.getFuncType().getRequiredMemory();
        int requiredStorage = node.getFuncType().getRequiredStorage();

        for ( RealNode n : this.nodes )
            if ( n.getName().compareTo( node.getName() ) == 0 )
                throw new NullPointerException( "addNode: duplicate node" );

        if ( (this.usedMemory.intValue()  + requiredMemory)  > this.availableMemory.intValue() )
            throw new IllegalArgumentException(
                    "addNode: no memory available" );

        if ( (this.usedStorage.intValue() + requiredStorage) > this.availableStorage.intValue() )
            throw new IllegalArgumentException(
                    "addNode: no storage available" );

        if ( this.usedVNFs.intValue() == this.maxVNFs.intValue() )
            throw new IllegalArgumentException(
                    "addNode: host has no VNF slots available" );

        if ( this.nodes.contains( node ) )
            throw new NullPointerException( "addNode: duplicate node" );

        this.nodes.add( node );


        this.usedMemory  = new Integer( this.usedMemory.intValue() + requiredMemory );
        this.usedStorage = new Integer( this.usedStorage.intValue() + requiredStorage );
        this.usedVNFs    = new Integer( this.usedVNFs.intValue() + 1 );
    }

    protected void removeNode( String nodeName ) {

        for ( RealNode node : this.nodes ) {
            if ( node.getName().compareTo( nodeName ) == 0 ) {
                this.nodes.remove( node );
                break;
            }
        }
    }

    protected boolean isAvailable( int requiredMemory, int requiredStorage ) {

        int freeVNFs = (this.maxVNFs.intValue() - this.usedVNFs.intValue());

        if ( freeVNFs < 1 )
            return false;

        int freeMemory  = this.availableMemory.intValue() - this.usedMemory.intValue();
        int freeStorage = this.availableStorage.intValue() - this.usedStorage.intValue();

        if ( (freeMemory < requiredMemory) || (freeStorage < requiredStorage) )
            return false;

        return true;
    }
}
