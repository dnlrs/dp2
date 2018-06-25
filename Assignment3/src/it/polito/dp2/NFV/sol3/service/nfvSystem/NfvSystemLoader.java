package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NfvReaderFactory;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;

public class NfvSystemLoader {

    private final static Logger logger = Logger.getLogger( NfvSystemLoader.class.getName() );

    public void loadFromGenerator( NfvSystemDBMS db )
            throws NfvReaderException {

        NfvReader monitor = null;

        try {

            NfvReaderFactory factory = NfvReaderFactory.newInstance();
            monitor = factory.newNfvReader();

        } catch ( FactoryConfigurationError error ) {
            throw new NfvReaderException( error.getMessage() );
        }

        /*
         * Retrieve Hosts and Connections from generator
         */
        HashMap<String, RealHost> hosts = new HashMap<String, RealHost>();
        HashMap<String, RealConnection> connections =
                new HashMap<String, RealConnection>();
        try {
            for ( HostReader hostI : monitor.getHosts() ) {

                /* This checks
                 *  - if host name is valid
                 *  - if host memory and storage and maxVNFs are valid values
                 */
                RealHost host =
                        new RealHost(
                                hostI.getName(), hostI.getAvailableMemory(),
                                hostI.getAvailableStorage(), hostI.getMaxVNFs(),
                                new HashSet<RealNode>() /* no nodes for now */
                                );

                /*
                 * Add host
                 */
                hosts.put( host.getName(), host );

                for ( HostReader dstHostI : monitor.getHosts() ) {
                    ConnectionPerformanceReader connectionI =
                            monitor.getConnectionPerformance( hostI, dstHostI );

                    if ( connectionI == null ) {
                        continue;
                    }

                    /* This checks
                     *  - if connection name is valid
                     *  - if connection latency and throughput are valid values
                     */
                    RealConnection connection =
                            new RealConnection(
                                    hostI.getName()+"TO"+dstHostI.getName(),
                                    connectionI.getLatency(),
                                    connectionI.getThroughput()
                                    );

                    /*
                     * Add connection
                     */
                    connections.put( connection.getName(), connection );
                }
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            logger.log( Level.WARNING, e.getMessage() );
//            throw new NfvReaderException( e.getMessage() );
        }


        /*
         * Retrieve VNFs from generator
         */
        HashMap<String, RealVNFType> vnfs = new HashMap<String, RealVNFType>();
        try {
            for ( VNFTypeReader vnfI : monitor.getVNFCatalog() ) {
                RealVNFType vnf =
                        new RealVNFType(
                                vnfI.getName(), vnfI.getFunctionalType(),
                                vnfI.getRequiredMemory(), vnfI.getRequiredStorage()
                                );

                vnfs.put( vnf.getName(), vnf );
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e.getMessage() );
        }


        /*
         * Retrieve NFFGs from generator along with nodes and links
         */
        HashMap<String, RealNffg> nffgs = new HashMap<String, RealNffg>();
        try {

//            for ( NffgReader nffgI : monitor.getNffgs( null ) ) {
            {
                NffgReader nffgI = monitor.getNffg( "Nffg0" ); // load only "Nffg0"

                /* This checks
                 *  - if NFFG name is valid
                 *  - if deploy time is valid
                 */
                RealNffg nffg =
                        new RealNffg(
                                nffgI.getName(),
                                nffgI.getDeployTime(),
                                new HashSet<RealNode>() /* no nodes for now */
                                );

                HashMap<String, RealNode> nffgNodes = new HashMap<String, RealNode>();
                HashMap<String, RealLink> nffgLinks = new HashMap<String, RealLink>();


                for ( NodeReader nodeI : nffgI.getNodes() ) {

                    /* This checks:
                     *  - if node name is valid
                     *  - is node is hosted by an existent host
                     *  - if VNF type exists in the NFV System
                     */
                    RealNode node =
                            new RealNode(
                                    nodeI.getName(), hosts.get( nodeI.getHost().getName() ),
                                    nffg, vnfs.get( nodeI.getFuncType().getName() ),
                                    new HashSet<RealLink>() /* no links for now */
                                    );
                    /* This checks:
                     *  - if Host can host node (memory, storage and maxVNFs on Host
                     */
                    hosts.get( nodeI.getHost().getName() ).addNode( node );

                    /* This checks:
                     *  - if the node really belongs to current NFFG
                     */
                    if ( nodeI.getNffg().getName().compareTo(nffgI.getName()) != 0 )
                        throw new NullPointerException( "loadFromGenerator: nffg-node inconsistency" );

                    /*
                     * Add node to NFFG
                     */
                    nffg.addNode( node );

                    nffgNodes.put( node.getName(), node );
                }


                for ( NodeReader nodeI : nffgI.getNodes() ) {
                    for ( LinkReader linkI : nodeI.getLinks() ) {

                        /* This checks
                         *  - if link name is valid
                         *  - if source and destination node belong to current NFFG
                         *  - if latency an throughput have valid values ( >0 )
                         */
                        RealLink link =
                                new RealLink(
                                        linkI.getName(),
                                        nffgNodes.get( linkI.getSourceNode().getName() ),
                                        nffgNodes.get( linkI.getDestinationNode().getName() ),
                                        linkI.getLatency(),
                                        linkI.getThroughput()
                                        );

                        /* This checks:
                         *  - if the link follows an existent connection
                         *  - if required latency and throughput of the link are available in the connection
                         */
                        RealConnection connection =
                                connections.get(
                                        link.getSourceNode().getHost().getName() +
                                        "TO" + link.getDestinationNode().getHost().getName() );
                        if ( ( connection.getLatency()    < link.getLatency() )  ||
                             ( connection.getThroughput() < link.getThroughput() ) )
                            throw new NullPointerException( "loadFromGenerator: link-connection inconsistency" );

                        /*
                         * Add link to node
                         */
                        nffgNodes.get( nodeI.getName()).addLink( link );
                        nffgLinks.put( link.getName(), link );
                    }
                }

                nffgs.put( nffg.getName(), nffg );
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            logger.log( Level.WARNING, e.getMessage() );
//            throw new NfvReaderException( e.getMessage() );
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

        } catch ( NullPointerException | IllegalArgumentException e ) {
            logger.log( Level.WARNING, e.getMessage() );
//            throw new NfvReaderException( e.getMessage() );
        }
    }

}
