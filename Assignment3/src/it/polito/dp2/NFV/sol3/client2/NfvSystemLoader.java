package it.polito.dp2.NFV.sol3.client2;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.Link;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvArcs;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvHost;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvHosts;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvNFFG;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvNFFGs;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvVNF;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvVNFs;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.Services;

public class NfvSystemLoader {

    private final static String PROPERTY_URL = "it.polito.dp2.NFV.lab3.URL";

    private static URI BASE_URI = null;

    public NfvSystemLoader()
            throws NfvReaderException {

        try {

            String url = System.getProperty(
                                    PROPERTY_URL,
                                    "http://localhost:8080/NfvDeployer/rest/");

            BASE_URI   = URI.create( url );

        } catch (SecurityException
                | NullPointerException
                | IllegalArgumentException e ) {
            throw new NfvReaderException( e.getMessage() );
        }
    }

    public void loadFromNfvDeployer( NfvSystemDBMS db )
            throws NfvReaderException {

        Client client = ClientBuilder.newClient();

        Services service = null;
        try {

            service = client.target( UriBuilder.fromUri( BASE_URI ).path( "" ).build() )
                            .request( MediaType.APPLICATION_XML )
                            .get( Services.class );

        } catch ( WebApplicationException e ) {

            service = new Services();

            Link link = new Link();
            URI target_uri = UriBuilder.fromUri( BASE_URI ).path( "hosts" ).build();
            link.setHref( target_uri.toString() );
            service.setHostsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "connections" ).build();
            link.setHref( target_uri.toString() );
            service.setConnectionsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "vnfs" ).build();
            link.setHref( target_uri.toString() );
            service.setVnfsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "nffgs" ).build();
            link.setHref( target_uri.toString() );
            service.setNffgsLink( link );

            link = new Link();
            target_uri = UriBuilder.fromUri( BASE_URI ).path( "nodes" ).build();
            link.setHref( target_uri.toString() );
            service.setNodesLink( link );
        }

        /*
         * Retrieve hosts and connections from web service
         */
        HashMap<String, RealHost> hosts = new HashMap<String, RealHost>();
        HashMap<String, RealConnection> connections =
                new HashMap<String, RealConnection>();
        HashMap<String, RealVNFType> vnfs = new HashMap<String, RealVNFType>();
        HashMap<String, RealNffg> nffgs = new HashMap<String, RealNffg>();

        try {
            NfvHosts xmlHosts = client.target( service.getHostsLink().getHref() )
                                      .request( MediaType.APPLICATION_XML )
                                      .get( NfvHosts.class );

            int i = 0;
            for ( NfvHost hostI : xmlHosts.getNfvHost() ) {
                RealHost host =
                        new RealHost(
                                hostI.getName(), hostI.getInstalledMemory(),
                                hostI.getInstalledStorage(), hostI.getMaxVNFs(),
                                new HashSet<RealNode>() );

                hosts.put( host.getName(), host );
            }


            NfvArcs xmlconnections = client.target( service.getConnectionsLink().getHref() )
                                           .request( MediaType.APPLICATION_XML )
                                           .get( NfvArcs.class );


            for ( NfvArc connectionI : xmlconnections.getNfvArc() ) {

                RealConnection connection =
                        new RealConnection(
                                connectionI.getSrc()+"TO"+connectionI.getDst(),
                                connectionI.getLatency(),
                                connectionI.getThroughput() );

                connections.put( connection.getName(), connection );
            }



            NfvVNFs xmlVNFs = client.target( service.getVnfsLink().getHref() )
                                    .request( MediaType.APPLICATION_XML )
                                    .get( NfvVNFs.class );

            for ( NfvVNF vnfI : xmlVNFs.getNfvVNF() ) {

                RealVNFType vnf =
                        new RealVNFType(
                                vnfI.getName(),
                                FunctionalType.fromValue( vnfI.getFunctionalType().toString() ),
                                vnfI.getRequiredMemory(), vnfI.getRequiredStorage() );

                vnfs.put( vnf.getName(), vnf );
            }



            /*
             * Retrieve NFFGs from Web Service
             */
            NfvNFFGs xmlNffgs = client.target( service.getNffgsLink().getHref() )
                                      .request( MediaType.APPLICATION_XML )
                                      .get( NfvNFFGs.class );


            for ( NfvNFFG nffgI : xmlNffgs.getNfvNFFG() ) {
                RealNffg nffg =
                        new RealNffg(
                                nffgI.getName(),
                                nffgI.getDeployTime().toGregorianCalendar(),
                                new HashSet<RealNode>() );

                HashMap<String, RealNode> nffgNodes = new HashMap<String, RealNode>();
                HashMap<String, RealLink> nffgLinks = new HashMap<String, RealLink>();

                for ( NfvNode nodeI : nffgI.getNfvNode() ) {

                    RealNode node =
                            new RealNode(
                                    nodeI.getName(), hosts.get( nodeI.getHostingHost() ),
                                    nffg, vnfs.get( nodeI.getFunctionalType() ),
                                    new HashSet<RealLink>() );

                    hosts.get( nodeI.getHostingHost() ).addNode( node );

                    if ( nodeI.getAssociatedNFFG().compareTo( nffgI.getName() ) != 0 )
                        throw new NullPointerException( "nodeNffg - nffg name inconsinstency" );

                    nffg.addNode( node );
                    nffgNodes.put( node.getName(), node );
                }

                for ( NfvNode nodeI : nffgI.getNfvNode() ) {
                    for ( NfvArc linkI : nodeI.getNfvArc() ) {

                        RealLink link =
                                new RealLink(
                                        linkI.getName(),
                                        nffgNodes.get( linkI.getSrc() ),
                                        nffgNodes.get( linkI.getDst() ),
                                        linkI.getLatency(),
                                        linkI.getThroughput() );

                        nffgNodes.get( nodeI.getName() ).addLink( link );
                        nffgLinks.put( link.getName(), link );
                    }

                }
                nffgs.put( nffg.getName(), nffg );
            }
        } catch ( WebApplicationException e ) {
            throw new NfvReaderException( e.getMessage() );
        } catch ( NullPointerException
                  | IllegalArgumentException e) {
            throw new NfvReaderException( e.getMessage() );
        } finally {
            if ( client != null ) {
                client.close();
                client = null;
            }
        }

        /*
         * Time to load everything in the real NFV System
         */
        try {
            /* add hosts */
            db.addHosts( new HashSet<RealHost>( hosts.values()) );
            /* add connections */
            for ( RealConnection connection : connections.values() ) {
                db.addConnectionPerformance(connection.getName(), connection);
            }
            /* add VNFs */
            db.addCatalog( new HashSet<RealVNFType>( vnfs.values() ) );
            /* add NFFGs, nodes and links */
            db.addNFFGs( new HashSet<RealNffg>( nffgs.values() ) );
        } catch ( NullPointerException
                  | IllegalArgumentException e ) {
            throw new NfvReaderException( e.getMessage() );
      }

    }
}
