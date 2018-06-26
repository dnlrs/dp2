package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNode;

/**
 * An implementation of the {@link LinkReader} interface that retrieves
 * data from the NfvDeployer Web Service.
 *
 * @author Daniel C. Rusu
 */
public class Client1Link implements LinkReader {

    private final URI BASE_URI;
    private final NfvArc link;

    public Client1Link( NfvArc xmlLink, URI base) {
        this.link = xmlLink;
        this.BASE_URI = base;
    }

    @Override
    public String getName() {
        return this.link.getName();
    }

    @Override
    public NodeReader getDestinationNode() {

        Client client = ClientBuilder.newClient();

        NfvNode response = null;
        try {

            response = client.target( this.link.getDstLink().getHref() )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvNode.class );


        } catch ( Exception e ) {
            return null;
        } finally {
            client.close();
        }

        Client1Node result = new Client1Node( response, this.BASE_URI );
        return result;
    }

    @Override
    public int getLatency() {
        // NOTE: this may be gotten from the service since link may be updated
        return this.link.getLatency();
    }

    @Override
    public NodeReader getSourceNode() {

        Client client = ClientBuilder.newClient();

        NfvNode response = null;
        try {

            response = client.target( this.link.getSrcLink().getHref() )
                             .request( MediaType.APPLICATION_XML )
                             .get( NfvNode.class );


        } catch ( Exception e ) {
            return null;
        } finally {
            client.close();
        }

        Client1Node result = new Client1Node( response, this.BASE_URI );
        return result;
    }

    @Override
    public float getThroughput() {
        // NOTE: this may have to be gotten from the service since link may be updated
        return this.link.getThroughput();
    }

}
