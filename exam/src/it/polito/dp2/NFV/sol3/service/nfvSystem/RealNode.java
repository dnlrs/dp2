package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;

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

    private final HashSet<RealLink> links;

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
        this.links    = new HashSet<RealLink>( links );
    }


    // getters


    @Override
    public VNFTypeReader getFuncType() {
            return this.funcType;
    }

    @Override
    public HostReader getHost() {
            return this.host;
    }

    @Override
    public synchronized Set<LinkReader> getLinks() {
        return Collections.unmodifiableSet( new HashSet<LinkReader>( this.links ) );
    }

    @Override
    public NffgReader getNffg() {
            return this.nffg;
    }


    protected synchronized Set<RealLink> getRealLinks() {
            return Collections.unmodifiableSet( this.links );
    }



    // other

    /**
     * Adds a link to the node, if another link with the same name was present
     * then it is replaced.
     *
     * @param link
     * @throws NullPointerException
     */
    protected synchronized void addLink( RealLink link )
            throws NullPointerException, AlreadyLoadedException {

        if ( link == null )
            throw new NullPointerException( "addLink: null argument" );

        for ( RealLink l : this.links )
            if ( l.getName().compareTo( link.getName() ) == 0 )
                throw new AlreadyLoadedException( "addLink: duplicate link" );

            this.links.add( link );
    }


//    protected synchronized void addLinkOrReplace( RealLink link )
//            throws NullPointerException {
//
//        if ( link == null )
//            throw new NullPointerException( "addLink: null argument" );
//
//        for ( RealLink l : this.links )
//            if ( l.getName().compareTo( link.getName() ) == 0 ) {
//                this.links.remove( l );
//            }
//
//            this.links.add( link );
//    }

    protected void removeLink( RealLink link ) {
        for ( RealLink l : this.links )
            if ( l.getName().compareTo( link.getName() ) == 0 ) {
                this.links.remove( link );
                break;
            }
    }
}
