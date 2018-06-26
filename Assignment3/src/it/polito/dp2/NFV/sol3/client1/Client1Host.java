package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvHost;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNodes;

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

            response = client.target( this.host.getHostedNodesLink().getHref() )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvNodes.class );

        } catch ( Exception e ) {
            return new HashSet<NodeReader>();
        } finally {
            client.close();
        }

        Set<Client1Node> result = new HashSet<Client1Node>();

        for ( NfvNode node : response.getNfvNode() ) {

//        for ( NfvNodes.NfvNode nodeI : response.getNfvNode() ) {
//            NfvNode node = new NfvNode();
//
//            node.setName( nodeI.getName() );
//            node.setFunctionalType( nodeI.getFunctionalType() );
//            node.setHostingHost( nodeI.getHostingHost() );
//            node.setAssociatedNFFG( nodeI.getAssociatedNFFG() );
//            node.setSelf( nodeI.getSelf() );
//            node.setFunctionalTypeLink( nodeI.getFunctionalTypeLink() );
//            node.setHostingHostLink( nodeI.getHostingHostLink() );
//            node.setAssociatedNFFGLink( nodeI.getAssociatedNFFGLink() );
//            node.setLinksLink( nodeI.getLinksLink() );
//            node.setReachableHostsLink( nodeI.getReachableHostsLink() );

            result.add( new Client1Node( node, this.BASE_URI ) );
        }

        return new HashSet<NodeReader>( result );
    }

}
