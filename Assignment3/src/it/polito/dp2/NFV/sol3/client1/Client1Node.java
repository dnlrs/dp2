package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvArcs;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvHost;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNFFG;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvVNF;

/**
 * An implementation of the {@link NodeReader} interface that retrieves
 * data from the NfvDeployer Web Service.
 *
 * @author Daniel C. Rusu
 */
public class Client1Node implements NodeReader {

    private final URI BASE_URI;
    private final NfvNode node;

    public Client1Node( NfvNode xmlNode, URI base ) {
        this.node = xmlNode;
        this.BASE_URI = base;
    }

    @Override
    public String getName() {
        return this.node.getName();
    }

    @Override
    public VNFTypeReader getFuncType() {

        Client client = ClientBuilder.newClient();
        NfvVNF response = null;
        try {

            String path = null;
            if ( this.node.getFunctionalTypeLink() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                                    .path( "vnfs/{vnfName}" )
                                    .build( this.node.getFunctionalType() )
                                    .toString();
            } else {
                path = this.node.getFunctionalTypeLink().getHref();
            }

            response = client.target( path )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvVNF.class );

        } catch ( Exception e ) {
            return null;
        } finally {
            client.close();
        }

        Client1VNFType result = new Client1VNFType( response, this.BASE_URI );
        return result;
    }

    @Override
    public HostReader getHost() {

        Client client = ClientBuilder.newClient();
        NfvHost response = null;
        try {

            String path = null;
            if ( this.node.getHostingHostLink() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                                    .path( "hosts/{hostName}" )
                                    .build( this.node.getHostingHost() )
                                    .toString();
            } else {
                path = this.node.getHostingHostLink().getHref();
            }

            response = client.target( path )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvHost.class );

        } catch ( Exception e ) {
            return null;
        } finally {
            client.close();
        }

        Client1Host result = new Client1Host( response, this.BASE_URI );
        return result;
    }

    @Override
    public Set<LinkReader> getLinks() {

        Client client = ClientBuilder.newClient();
        NfvArcs response = null;
        try {

            String path = null;
            if ( this.node.getLinksLink() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                                    .path( "nodes/{nodeName}/links" )
                                    .build( this.node.getName() )
                                    .toString();
            } else {
                path = this.node.getLinksLink().getHref();
            }

            response = client.target( path )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvArcs.class );

        } catch ( Exception e ) {
            return new HashSet<LinkReader>();
        } finally {
            client.close();
        }

        Set<Client1Link> result = new HashSet<Client1Link>();

        for ( NfvArc link : response.getNfvArc() ) {
            result.add( new Client1Link( link, this.BASE_URI ) );
        }

        return new HashSet<LinkReader>( result );
    }

    @Override
    public NffgReader getNffg() {

        Client client = ClientBuilder.newClient();
        NfvNFFG response = null;
        try {

            String path = null;
            if ( this.node.getAssociatedNFFGLink() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                                    .path( "nffgs/{nffgName}" )
                                    .build( this.node.getAssociatedNFFG() )
                                    .toString();
            } else {
                path = this.node.getAssociatedNFFGLink().getHref();
            }

            response = client.target( path )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvNFFG.class );

        } catch ( Exception e ) {
            return null;
        } finally {
            client.close();
        }

        Client1Nffg result = new Client1Nffg( response, this.BASE_URI );
        return result;
    }

}
