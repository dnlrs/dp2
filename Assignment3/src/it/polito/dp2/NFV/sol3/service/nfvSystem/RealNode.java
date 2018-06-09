package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;

/**
 * An implementation of the {@link NodeReader} interface.
 *
 * @author   Daniel C. Rusu
 * @studenID 234428
 */
public class RealNode extends RealNamedEntity implements NodeReader {

    private final RealHost    host;
    private final RealNffg    nffg;
    private final RealVNFType funcType;

    private final CopyOnWriteArraySet<RealLink> links;

    private final Object lockHost     = new Object();
    private final Object lockNffg     = new Object();
    private final Object lockFuncType = new Object();
    private final Object lockLinks    = new Object();


    // constructors

    public RealNode(
            String name, RealHost host, RealNffg nffg,
            RealVNFType funcType, Set<RealLink> links )
                    throws NullPointerException, IllegalArgumentException {

        super( name );

        /*
         * Checks
         */
        if ( host == null )
            throw new IllegalArgumentException( "new Node: null argument" );

        if ( nffg == null )
            throw new IllegalArgumentException( "new Node: null argument" );

        if ( funcType == null )
            throw new IllegalArgumentException( "new Node: null argument" );

        if ( links.contains( null ) )
            throw new NullPointerException(
                    "new Node: argument set contains null elements" );

        this.host     = host;
        this.nffg     = nffg;
        this.funcType = funcType;
        this.links    = new CopyOnWriteArraySet<RealLink>( links );
    }


    // getters


    @Override
    public VNFTypeReader getFuncType() {
        synchronized ( this.lockFuncType ) {
            return this.funcType;
        }
    }

    @Override
    public HostReader getHost() {
        synchronized ( this.lockHost ) {
            return this.host;
        }
    }

    @Override
    public Set<LinkReader> getLinks() {
        synchronized ( this.lockLinks ) {
            return new HashSet<LinkReader>( this.links );
        }
    }

    @Override
    public NffgReader getNffg() {
        synchronized ( this.lockNffg ) {
            return this.nffg;
        }
    }


    protected Set<RealLink> getRealLinks() {
        synchronized ( this.lockLinks ) {
            return new HashSet<RealLink>( this.links );
        }
    }



    // setters

    protected void addLink( RealLink link )
            throws NullPointerException {

        if ( link == null )
            throw new NullPointerException( "addLink: null argument" );

        synchronized ( this.lockLinks ) {

            for ( RealLink l : this.links )
                if ( l.getName().compareTo( link.getName() ) == 0 )
                    throw new NullPointerException( "addLink: duplicate link" );

            this.links.add( link );
        }
    }
}
