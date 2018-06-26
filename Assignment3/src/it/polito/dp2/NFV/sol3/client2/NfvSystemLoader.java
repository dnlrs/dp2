package it.polito.dp2.NFV.sol3.client2;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvArcs;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvHosts;
import it.polito.dp2.NFV.sol3.client2.model.nfvdeployer.NfvNFFGs;
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

            service = client.target( BASE_URI )
                            .request( MediaType.APPLICATION_XML )
                            .get( Services.class );

        } catch ( WebApplicationException e ) {
            throw new NfvReaderException( e.getMessage() );
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

            for ( NfvHosts.NfvHost hostI : xmlHosts.getNfvHost() ) {

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


            for ( NfvArcs.NfvArc connectionI : xmlconnections.getNfvArc() ) {

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

            for ( NfvVNFs.NfvVNF vnfI : xmlVNFs.getNfvVNF() ) {

                RealVNFType vnf =
                        new RealVNFType(
                                vnfI.getName(),
                                FunctionalType.fromValue( vnfI.getFunctionalType() ),
                                vnfI.getRequiredMemory(), vnfI.getRequiredStorage() );

                vnfs.put( vnf.getName(), vnf );
            }

            /*
             * Retrieve NFFGs from Web Service
             */
            NfvNFFGs xmlNffgs = client.target( service.getNffgsLink().getHref() )
                                      .request( MediaType.APPLICATION_XML )
                                      .get( NfvNFFGs.class );


            for ( NfvNFFGs.NfvNFFG nffgI : xmlNffgs.getNfvNFFG() ) {
                RealNffg nffg =
                        new RealNffg(
                                nffgI.getName(),
                                nffgI.getDeployTime().toGregorianCalendar(),
                                new HashSet<RealNode>() );

                HashMap<String, RealNode> nffgNodes = new HashMap<String, RealNode>();
                HashMap<String, RealLink> nffgLinks = new HashMap<String, RealLink>();

                for ( NfvNFFGs.NfvNFFG.NfvNode nodeI : nffgI.getNfvNode() ) {

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

                for ( NfvNFFGs.NfvNFFG.NfvNode nodeI : nffgI.getNfvNode() ) {
                    for ( NfvNFFGs.NfvNFFG.NfvNode.NfvArc linkI : nodeI.getNfvArc() ) {

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
