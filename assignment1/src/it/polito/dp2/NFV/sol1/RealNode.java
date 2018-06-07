package it.polito.dp2.NFV.sol1;

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

    private RealHost    host;
    private RealNffg    nffg;
    private RealVNFType funcType;

    private CopyOnWriteArraySet<RealLink> links;

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
        this.setHost( host );
        this.setNffg( nffg );
        this.setFuncType( funcType );
        this.setLinks( links );
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



    protected void setHost( RealHost host )
            throws IllegalArgumentException {

        if ( host == null )
            throw new IllegalArgumentException( "setHost: null argument" );

        synchronized ( this.lockHost ) {
            this.host = host;
        }
    }


    protected void setNffg( RealNffg nffg )
            throws IllegalArgumentException {

        if ( nffg == null )
            throw new IllegalArgumentException( "setNffg: null argument" );

        synchronized ( this.lockNffg ) {
            this.nffg = nffg;
        }
    }


    protected void setFuncType( RealVNFType funcType )
            throws IllegalArgumentException {

        if ( funcType == null )
            throw new IllegalArgumentException( "setFuncType: null argument" );

        synchronized ( this.lockFuncType ) {
            this.funcType = funcType;
        }
    }


    protected void setLinks( Set<RealLink> links )
            throws NullPointerException {

        if ( links.contains( null ) )
            throw new NullPointerException( "setLinks: argument Set contains null elements" );

        synchronized ( this.lockLinks ) {
            this.links = new CopyOnWriteArraySet<RealLink>( links );
        }
    }

    protected void addLink( RealLink link )
            throws NullPointerException {

        if ( link == null )
            throw new NullPointerException( "addLink: null argument" );

        synchronized ( this.lockLinks ) {
            this.links.add( link );
        }
    }


}
