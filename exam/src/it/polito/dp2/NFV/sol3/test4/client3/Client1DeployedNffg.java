package it.polito.dp2.NFV.sol3.test4.client3;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.AllocationException;
import it.polito.dp2.NFV.lab3.DeployedNffg;
import it.polito.dp2.NFV.lab3.LinkAlreadyPresentException;
import it.polito.dp2.NFV.lab3.NoNodeException;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.test4.client3.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.test4.client3.model.nfvdeployer.NfvNFFG;
import it.polito.dp2.NFV.sol3.test4.client3.model.nfvdeployer.NfvNode;

/**
 * An implementation of the {@link DeployedNffg} interface.
 *
 * @author Daniel C. Rusu
 */
public class Client1DeployedNffg implements DeployedNffg {

    private final URI BASE_URI;

    private final NfvNFFG deployedNffg;
    private int nodeCounter;
    private int linkCounter;

    protected Client1DeployedNffg(
                        NfvNFFG xmlNffg,
                        int nodeCounter,
                        URI baseUri ) {

        this.deployedNffg = xmlNffg;
        this.nodeCounter  = nodeCounter;
        this.linkCounter  = 1;
        this.BASE_URI     = URI.create( baseUri.toString() );
    }

    @Override
    public NodeReader addNode( VNFTypeReader type, String hostName )
            throws AllocationException, ServiceException {

        /* Node name example: Node1Nffg0 */
        String nodeName = new String(
                    "Node" + this.nodeCounter + this.deployedNffg.getName() );
        this.nodeCounter++;

        /* prepare xml request body */
        NfvNode node = new NfvNode();
        node.setName( nodeName );
        node.setFunctionalType( type.getName() );

        if ( hostName != null ) {
            node.setHostingHost( hostName );
        }
        
        /* check if there are hosts to be avoided */
        Client3Exam main = new Client3Exam();
        Set<String> avoidHosts = main.getUnwantedHosts();
        if ( avoidHosts != null ) {
        	List<String> nohost = node.getAvoidHosts();
        	for ( String host : avoidHosts ) {
        		nohost.add(host);
        	}
        }

        node.setAssociatedNFFG( this.deployedNffg.getName() );

        Client client = ClientBuilder.newClient();

        /* POST (deploy) a new node */
        NfvNode newNode = null;
        try {

            URI target_uri = UriBuilder.fromUri( this.BASE_URI )
                                       .path( "nodes" )
                                       .build();

            newNode = client.target( target_uri )
                            .request( MediaType.APPLICATION_XML )
                            .post( Entity.entity( node, MediaType.APPLICATION_XML ),
                                    NfvNode.class );

        } catch ( WebApplicationException e ) {

            if ( e.getResponse().getStatus() == 403 )
                throw new AllocationException(); // could not deploy node

            throw new ServiceException();

        } catch ( Exception e ) {
            throw new ServiceException();
        } finally {
            client.close();
        }

        Client1Node result = new Client1Node( newNode, this.BASE_URI );

        return result;
    }


    @Override
    public LinkReader addLink(
            NodeReader source,
            NodeReader dest,
            boolean overwrite )
            throws NoNodeException,
                    LinkAlreadyPresentException, ServiceException {

        /* Link name example: Link1Nffg0 */
        String linkName = new String(
                "Link" + this.linkCounter + this.deployedNffg.getName() );
        this.linkCounter++;

        /* prepare XML request body */
        NfvArc xmlLink = new NfvArc();
        xmlLink.setName( linkName );
        xmlLink.setSrc( source.getName() );
        xmlLink.setDst( dest.getName() );
        xmlLink.setThroughput( 0 );
        xmlLink.setLatency( 0 );

        Client client = ClientBuilder.newClient();

        NfvArc newLink = null;
        try {

            URI target_uri = null;
            if ( this.deployedNffg.getLinksLink() == null  ) { // HATEOAS didn't work?

                UriBuilder uriBuilder = UriBuilder.fromUri( this.BASE_URI )
                                    .path( "nffgs/{nffgName}/links" );
                if ( overwrite ) {
                    uriBuilder = uriBuilder.queryParam( "update", "1" );
                } else {
                    uriBuilder = uriBuilder.queryParam( "update", "0" );
                }
                target_uri = uriBuilder.build( this.deployedNffg.getName() );

            } else {

                UriBuilder uriBuilder =
                        UriBuilder.fromPath(
                                this.deployedNffg.getLinksLink().getHref() );
                if ( overwrite ) {
                    uriBuilder = uriBuilder.queryParam( "update", "1" );
                } else {
                    uriBuilder = uriBuilder.queryParam( "update", "0" );
                }

                target_uri = uriBuilder.build();
            }


            newLink = client.target( target_uri )
                            .request( MediaType.APPLICATION_XML )
                            .post( Entity.entity( xmlLink, MediaType.APPLICATION_XML),
                                   NfvArc.class );

        } catch ( WebApplicationException e ) {

            int status = e.getResponse().getStatus();

            if ( status == 400 )
                throw new NoNodeException( "Bad Request" );
            else if ( status == 404 )
                throw new NoNodeException( "Relative nodes or NFFG are not deployed" );
            else if ( status == 406 )
                throw new LinkAlreadyPresentException( "Link already present" );
            else
                throw new ServiceException( "Service error (status " + status + ")" );

        } catch ( Exception e ) {
            throw new ServiceException();
        } finally {
            client.close();
        }

        Client1Link result = new Client1Link( newLink, this.BASE_URI );

        return result;
    }

    @Override
    public NffgReader getReader()
            throws ServiceException {

        Client client = ClientBuilder.newClient();

        /* get service access points */
        NfvNFFG newNffg = null;
        try {

            String path = null;
            if ( this.deployedNffg.getSelf() == null ) {
                path = UriBuilder.fromUri( this.BASE_URI )
                                 .path( "nffgs/{nffgName}" )
                                 .build( this.deployedNffg.getName() )
                                 .toString();
            } else {
                path = this.deployedNffg.getSelf().getHref();
            }


            newNffg = client.target( path )
                            .request( MediaType.APPLICATION_XML )
                            .get( NfvNFFG.class );

        } catch ( WebApplicationException e ) {
            int status = e.getResponse().getStatus();

            if ( status == 400 )
                throw new ServiceException( "Bad request" );
            else if ( status == 404 )
                throw new ServiceException( "Not Found" );

            throw new ServiceException( "Service processing error" );
        } catch ( Exception e ) {
            throw new ServiceException( "Service processing error: " + e.getMessage() );
        } finally {
            if ( client != null ) {
                client.close();
                client = null;
            }
        }

        return new Client1Nffg( newNffg, this.BASE_URI );
    }

}
