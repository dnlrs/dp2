package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvHost;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNodes;

/**
 * An implementation of the {@link HostReader} interface that retrieves
 * data from the NfvDeployer Web Service.
 *
 * @author Daniel C. Rusu
 */
public class Client1Host implements HostReader {

    private final URI BASE_URI;
    private final NfvHost host;

    public Client1Host( NfvHost xmlHost, URI base ) {
        this.host = xmlHost;
        this.BASE_URI = base;
    }

    @Override
    public String getName() {
        return this.host.getName();
    }

    @Override
    public int getAvailableMemory() {
        return this.host.getInstalledMemory();
    }

    @Override
    public int getAvailableStorage() {
        return this.host.getInstalledStorage();
    }

    @Override
    public int getMaxVNFs() {
        return this.host.getMaxVNFs();
    }

    @Override
    public Set<NodeReader> getNodes() {

        Client client = ClientBuilder.newClient();
        NfvNodes response = null;
        try {

            String path = null;
            if ( this.host.getHostedNodesLink() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                                .path( "hosts/{hostName}/nodes" )
                                .build( this.host.getName() )
                                .toString();
            } else {
                path = this.host.getHostedNodesLink().getHref();
            }


            response = client.target( path )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvNodes.class );

        } catch ( Exception e ) {
            return new HashSet<NodeReader>();
        } finally {
            client.close();
        }

        Set<Client1Node> result = new HashSet<Client1Node>();

        for ( NfvNode node : response.getNfvNode() ) {

            result.add( new Client1Node( node, this.BASE_URI ) );
        }

        return new HashSet<NodeReader>( result );
    }

}
