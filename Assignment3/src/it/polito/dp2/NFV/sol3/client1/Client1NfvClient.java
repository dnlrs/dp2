package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.NffgDescriptor;
import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.NfvClientException;
import it.polito.dp2.NFV.lab3.NodeDescriptor;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.Link;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNFFG;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.client1.model.nfvdeployer.Services;

/**
 * An implementation of the {@link NfvClient} interface.
 *
 * @author Daniel C. Rusu
 */
public class Client1NfvClient implements NfvClient {

    private static int nffgCounter = 1;

    private final static String PROPERTY_URL = "it.polito.dp2.NFV.lab3.URL";

    private static URI BASE_URI = null;


    public Client1NfvClient()
            throws NfvClientException {

        try {
            String url = System.getProperty(
                                    PROPERTY_URL,
                                    "http://localhost:8080/NfvDeployer/rest/");

            BASE_URI   = URI.create( url );
        } catch (SecurityException
                | NullPointerException
                | IllegalArgumentException e ) {
            throw new NfvClientException( e.getMessage() );
        }
    }


    @Override
    public DeployedNffg deployNffg( NffgDescriptor nffg )
            throws AllocationException, ServiceException {


        int nodeCounter = 1; /* this is relative to current NFFG being deployed */


        /* prepare NFFG xml request body */
        NfvNFFG xmlNffg = new NfvNFFG();

        xmlNffg.setName( new String( "Nffg" + nffgCounter ) );
        nffgCounter++;

        List<NfvNode> xmlNffgNodes = xmlNffg.getNfvNode();

        for ( NodeDescriptor node : nffg.getNodes() ) {
            NfvNode xmlNode = new NfvNode();
            xmlNode.setName( new String( "Node" + nodeCounter + xmlNffg.getName() ) );
            nodeCounter++;

            xmlNode.setFunctionalType( node.getFuncType().getName() );

            if ( node.getHostName() != null ) {
                xmlNode.setHostingHost( node.getHostName() );
            }

            xmlNode.setAssociatedNFFG( xmlNffg.getName() );

            xmlNffgNodes.add( xmlNode );
        }

        Client client = ClientBuilder.newClient();

        /* get service access points */
        Services services = null;
        try {
            services = client.target( BASE_URI )
                             .request( MediaType.APPLICATION_XML )
                             .get( Services.class );
        } catch ( WebApplicationException e ) {

            services = new Services();

            Link link = new Link();
            URI target_uri = UriBuilder.fromUri( BASE_URI ).path( "hosts" ).build();
            link.setHref( target_uri.toString() );
            services.setHostsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "connections" ).build();
            link.setHref( target_uri.toString() );
            services.setConnectionsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "vnfs" ).build();
            link.setHref( target_uri.toString() );
            services.setVnfsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "nffgs" ).build();
            link.setHref( target_uri.toString() );
            services.setNffgsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "nodes" ).build();
            link.setHref( target_uri.toString() );
            services.setNodesLink( link );

        } catch ( Exception e ) {
            throw new ServiceException( "Failed getting services access points" );
        }


        /* POST (deploy) NFFG */
        NfvNFFG newNffg = null;
        try {

            URI target_uri = URI.create( services.getNffgsLink().getHref() );
            newNffg = client.target( target_uri )
                            .request( MediaType.APPLICATION_XML )
                            .post( Entity.entity( xmlNffg, MediaType.APPLICATION_XML ),
                                   NfvNFFG.class );

        } catch ( WebApplicationException e ) {

            int status = e.getResponse().getStatus();

            if ( status == 403 )
                throw new AllocationException( "Resources unavailable to deploy NFFG" );
            else if ( status == 400 )
                throw new ServiceException( "Failed deploying a new NFFG: bad request" );

            throw new ServiceException( "Failed deploying a new NFFG: server error" );

        } catch ( Exception e ) {
            throw new ServiceException( "Failed deploying a new NFFG:" + e.getMessage() );
        } finally {
                client.close();
        }

        Client1DeployedNffg result = new Client1DeployedNffg( newNffg, nodeCounter, BASE_URI );

        return result;
    }



    @Override
    public DeployedNffg getDeployedNffg( String name )
            throws UnknownEntityException, ServiceException {

        Client client = ClientBuilder.newClient();

        /* get service access points */
        Services services = null;
        try {
            services = client.target( BASE_URI )
                                      .request( MediaType.APPLICATION_XML )
                                      .get( Services.class );
        } catch ( WebApplicationException e ) {

            services = new Services();

            Link link = new Link();
            URI target_uri = UriBuilder.fromUri( BASE_URI ).path( "hosts" ).build();
            link.setHref( target_uri.toString() );
            services.setHostsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "connections" ).build();
            link.setHref( target_uri.toString() );
            services.setConnectionsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "vnfs" ).build();
            link.setHref( target_uri.toString() );
            services.setVnfsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "nffgs" ).build();
            link.setHref( target_uri.toString() );
            services.setNffgsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "nodes" ).build();
            link.setHref( target_uri.toString() );
            services.setNodesLink( link );
        }

        /* GET requested NFFG (if deployed) */
        NfvNFFG xmlNffg = null;
        try {

            UriBuilder uriBuilder =
                    UriBuilder.fromPath( services.getNffgsLink().getHref() );
            URI target_uri = uriBuilder.path( "/{nffgName}" ).build( name );

            xmlNffg = client.target( target_uri )
                                    .request( MediaType.APPLICATION_XML )
                                    .get( NfvNFFG.class );

        } catch ( WebApplicationException e ) {

            if ( e.getResponse().getStatus() == 404 )
                throw new UnknownEntityException( "Nffg requested is not deployed" ); // NFFG not found

            throw new ServiceException( "getDeployedNffg: error processing request" );

        } catch ( Exception e ) {
            throw new ServiceException( e.getMessage() );
        } finally {
                client.close();
        }


        Client1DeployedNffg result =
                new Client1DeployedNffg( xmlNffg, xmlNffg.getNfvNode().size() + 1, BASE_URI );

        return result;
    }

}
