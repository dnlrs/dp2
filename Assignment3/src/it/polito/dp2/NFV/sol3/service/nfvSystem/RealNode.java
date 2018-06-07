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

    private RealHost    host;
    private RealNffg    nffg;
    private RealVNFType funcType;

    private CopyOnWriteArraySet<RealLink> links;


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
        synchronized ( this.funcType ) {
            return this.funcType;
        }
    }

    @Override
    public HostReader getHost() {
        synchronized ( this.host ) {
            return this.host;
        }
    }

    @Override
    public Set<LinkReader> getLinks() {
        synchronized ( this.links ) {
            return new HashSet<LinkReader>( this.links );
        }
    }

    @Override
    public NffgReader getNffg() {
        synchronized ( this.nffg ) {
            return this.nffg;
        }
    }


    protected synchronized Set<RealLink> getRealLinks() {
        return new HashSet<RealLink>( this.links );
    }



    // setters



    protected void setHost( RealHost host )
            throws IllegalArgumentException {

        if ( host == null )
            throw new IllegalArgumentException( "setHost: null argument" );

        synchronized ( this.host ) {
            this.host = host;
        }
    }


    protected void setNffg( RealNffg nffg )
            throws IllegalArgumentException {

        if ( nffg == null )
            throw new IllegalArgumentException( "setNffg: null argument" );

        synchronized ( this.nffg ) {
            this.nffg = nffg;
        }
    }


    protected void setFuncType( RealVNFType funcType )
            throws IllegalArgumentException {

        if ( funcType == null )
            throw new IllegalArgumentException( "setFuncType: null argument" );

        synchronized ( this.funcType ) {
            this.funcType = funcType;
        }
    }


    protected void setLinks( Set<RealLink> links )
            throws NullPointerException {

        if ( links.contains( null ) )
            throw new NullPointerException( "setLinks: argument Set contains null elements" );

        synchronized ( this.links ) {
            this.links = new CopyOnWriteArraySet<RealLink>( links );
        }
    }

    protected void addLink( RealLink link )
            throws NullPointerException {

        if ( link == null )
            throw new NullPointerException( "addLink: null argument" );

        synchronized ( this.links ) {
            this.links.add( link );
        }
    }


}
