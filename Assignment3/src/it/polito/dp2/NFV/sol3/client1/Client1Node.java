package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

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

            response = client.target( this.node.getFunctionalTypeLink().getHref() )
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

            response = client.target( this.node.getHostingHostLink().getHref() )
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

            response = client.target( this.node.getLinksLink().getHref() )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvArcs.class );

        } catch ( Exception e ) {
            return null;
        } finally {
            client.close();
        }

        Set<Client1Link> result = new HashSet<Client1Link>();
        for ( NfvArcs.NfvArc linkI : response.getNfvArc() ) {

            NfvArc link = new NfvArc();

            link.setName( linkI.getName() );
            link.setSrc( linkI.getSrc() );
            link.setDst( linkI.getDst() );
            link.setThroughput( linkI.getThroughput() );
            link.setLatency( linkI.getLatency() );
            link.setSelf( linkI.getSelf() );
            link.setSrcLink( linkI.getSrcLink() );
            link.setDstLink( linkI.getDstLink() );

            result.add( new Client1Link( link, this.BASE_URI ) );
        }

        return new HashSet<LinkReader>( result );
    }

    @Override
    public NffgReader getNffg() {

        Client client = ClientBuilder.newClient();
        NfvNFFG response = null;
        try {

            response = client.target( this.node.getAssociatedNFFGLink().getHref() )
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
