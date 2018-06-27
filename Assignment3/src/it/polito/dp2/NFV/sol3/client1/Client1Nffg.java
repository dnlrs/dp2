package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNFFG;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNodes;

/**
 * An implementation of the {@link NffgReader} interface that retrieves
 * data from the NfvDeployer Web Service.
 *
 * @author Daniel C. Rusu
 */
public class Client1Nffg implements NffgReader {

    private final URI BASE_URI;
    private final NfvNFFG nffg;


    public Client1Nffg( NfvNFFG xmlNffg, URI base ) {
        this.nffg = xmlNffg;
        this.BASE_URI = base;
    }

    @Override
    public String getName() {
        return this.nffg.getName();
    }

    @Override
    public Calendar getDeployTime() {
        return this.nffg.getDeployTime().toGregorianCalendar();
    }

    @Override
    public NodeReader getNode( String arg0 ) {

        Client client = ClientBuilder.newClient();

        NfvNode response = null;
        try {

            URI target_uri = UriBuilder.fromUri( this.BASE_URI )
                                       .path( "nodes/{nodeName}" )
                                       .build( arg0 );

            response = client.target( target_uri )
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
    public Set<NodeReader> getNodes() {

        Client client = ClientBuilder.newClient();
        NfvNodes response = null;
        try {

            String path = null;
            if ( this.nffg.getAllocatedNodesLink() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                            .path( "nffgs/{nffgName}/nodes" )
                            .build( this.nffg.getName() )
                            .toString();

            } else {
                path = this.nffg.getAllocatedNodesLink().getHref();
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
