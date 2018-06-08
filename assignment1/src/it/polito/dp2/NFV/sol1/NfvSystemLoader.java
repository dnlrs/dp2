package it.polito.dp2.NFV.sol1;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactoryConfigurationError;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol1.jaxb.Catalogue;
import it.polito.dp2.NFV.sol1.jaxb.Connection;
import it.polito.dp2.NFV.sol1.jaxb.Host;
import it.polito.dp2.NFV.sol1.jaxb.Host.AllocatedNodes;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork.Connections;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork.Hosts;
import it.polito.dp2.NFV.sol1.jaxb.Latency;
import it.polito.dp2.NFV.sol1.jaxb.Link;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType.DeployedNFFGs;
import it.polito.dp2.NFV.sol1.jaxb.Node;
import it.polito.dp2.NFV.sol1.jaxb.NodeRef;
import it.polito.dp2.NFV.sol1.jaxb.Throughput;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

/**
 * Loads the {@link NfvSystemDBMS} with data unmarshalled from an XML File.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvSystemLoader {

    private static final String JAXB_CLASSES_PACKAGE = "it.polito.dp2.NFV.sol1.jaxb";
    private static final String SCHEMA_LOCATION      = "/xsd/nfvInfo.xsd";

    private static final String PROPERTY_XML_FILE    = "it.polito.dp2.NFV.sol1.NfvInfo.file";
    private static final String PROPERTY_USER_DIR    = "user.dir"; // working directory

    private final XMLValidator  validator;

    protected NfvSystemLoader() {
        this.validator = new XMLValidator();
    }

    protected void loadFromXMLFile( NfvSystemDBMS db )
            throws NfvReaderException {

        /*
         * Unmarshall XML File
         */
        NFVSystemType xmlNfvSystem  = unmarshallXMLFile();

        if ( xmlNfvSystem == null )
            throw new NfvReaderException( "loadFromXMLFile: could not load file" );

        /*
         * Get and check Infrastructure Network
         */
        InfrastructureNetwork xmlIn = xmlNfvSystem.getIN();

        if ( !( this.validator.isValidIN( xmlIn ) ) )
            throw new NfvReaderException(
                    "loadFromXMLFile: invalid Infrastructure Network" );

        /*
         * Retrieve Hosts
         */
        Hosts      xmlHosts         = xmlIn.getHosts();
        List<Host> liveListXMLHosts = xmlHosts.getHost();

        if ( liveListXMLHosts.size() == 0 )
            return; // no hosts, no connections, no NFFGs, no system

//        HashMap<String, Set<String>> hostsNodes = loadHostsNodes( liveListXMLHosts );
        HashMap<String, Set<String>> hostsNodes = new HashMap<String, Set<String>>();
        HashMap<String, RealHost>    hosts      = loadHosts( liveListXMLHosts, hostsNodes );

        /*
         * Retrieve Connections
         */
        Connections      xmlConnections         = xmlIn.getConnections();
        List<Connection> liveListXMLConnections = xmlConnections.getConnection();

        HashMap<String, RealConnection> connections =
                loadConnections( liveListXMLConnections,
                                 hosts );
        /*
         * Retrieve VNs
         */
        Catalogue xmlCatalogue    = xmlNfvSystem.getCatalogue();
        List<VNF> liveListXMLVNFs = xmlCatalogue.getVnf();

        HashMap<String, RealVNFType> vnfs = loadVNFs( liveListXMLVNFs );

        /*
         * Retrieve NFFGs along with nodes and links
         */
        DeployedNFFGs xmlDeployedNFFGs = xmlNfvSystem.getDeployedNFFGs();
        List<NFFG>    liveListXMLNFFGs = xmlDeployedNFFGs.getNffg();

        HashMap<String, RealNffg> nffgs = loadNFFGs( liveListXMLNFFGs,
                                                     hosts,
                                                     vnfs,
                                                     connections );
        /*
         * Check if data is consistent
         */
        for ( RealHost host : hosts.values() ) {
            String          hostName     = host.getName();
            Set<NodeReader> nfvHostNodes = host.getNodes();
            Set<String>     xmlHostNodes = hostsNodes.get( hostName );

            if ( nfvHostNodes.size() != xmlHostNodes.size() )
                throw new NfvReaderException(
                        "loadFromXMLFile: hosted nodes inconsistency" );

            for ( String nodeName : xmlHostNodes ) {

                boolean found = false;
                for ( NodeReader nodeI : nfvHostNodes ) {
                    if ( nodeI.getName().compareTo( nodeName ) == 0 ) {
                        found = true;
                        break;
                    }
                }

                if ( !found )
                    throw new NfvReaderException(
                            "loadFromXMLFile: hosted nodes inconsistency" );
            }
        }

        /*
         * Time to load everything into the real NFV System database
         */
        try {

            /* add hosts */
            db.addHosts( new HashSet<RealHost>( hosts.values()) );

            /* add connections */
            for ( RealConnection connection : connections.values() ) {
                db.addConnectionPerformance( connection.getName(), connection );
            }

            /* add NFFGs, nodes and links */
            db.addNFFGs( new HashSet<RealNffg>( nffgs.values() ) );

        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e );
        }
    }


    private NFVSystemType unmarshallXMLFile()
            throws NfvReaderException {

        NFVSystemType nfvSystem = null;
        try {

            JAXBContext jc  = JAXBContext.newInstance( JAXB_CLASSES_PACKAGE );
            Unmarshaller um = jc.createUnmarshaller();


            /*
             *  Set up validation against a schema
             */
            try {

                String schemaFile = ( System.getProperty( PROPERTY_USER_DIR ) == null ?
                                      new String( "" ) : System.getProperty( PROPERTY_USER_DIR ) );

                schemaFile = schemaFile.concat( SCHEMA_LOCATION );

                JAXBUtils.unmarshallerSetSchema( um, schemaFile );

            } catch ( SchemaFactoryConfigurationError | Exception e ) {
                System.err.println( "\n" + e.getMessage() );
                System.err.println( "unmarshallXMLFile: Could not set unmarshaller validation schema." ); // no validation
            }

            /*
             *  Get XML file name
             */
            String xmlFileName = System.getProperty( PROPERTY_XML_FILE );
            if ( xmlFileName == null )
                throw new NfvReaderException( "unmarshallXMLFile: Could not get XML file to read." );

            /*
             *  Unmarshal
             */
            Object obj = um.unmarshal( new FileInputStream( xmlFileName ) );

            @SuppressWarnings( "unchecked" )
            JAXBElement<NFVSystemType> element = (JAXBElement<NFVSystemType>) obj;

            nfvSystem = element.getValue();

        } catch ( Exception e ) {
            throw new NfvReaderException( e );
        }

        return nfvSystem;
    }




    private
    HashMap<String, RealHost> loadHosts(
            List<Host>                   liveListXMLHosts,
            HashMap<String, Set<String>> hostsNodes        )
                    throws NfvReaderException {

        if ( liveListXMLHosts == null )
            throw new NfvReaderException( "loadHosts: null argument" );

        HashMap<String, RealHost> hosts = new HashMap<String, RealHost>();
        try {

            for ( Host xmlHost : liveListXMLHosts ) {

                if ( !(this.validator.isValidHost( xmlHost )) )
                    throw new NfvReaderException( "loadHosts: found invalid Host" );

                String xmlHostName = xmlHost.getName();
                /* This checks
                 *  - if host name is valid
                 *  - if host memory and storage and maxVNFs are valid values
                 */
                RealHost host =
                        new RealHost(
                                xmlHostName,
                                xmlHost.getInstalledMemory().getValue().intValue(),
                                xmlHost.getInstalledStorage().getValue().intValue(),
                                xmlHost.getMaxVNFs(),
                                new HashSet<RealNode>() /* no nodes for now */
                                );
                /*
                 * Add host
                 */
                if ( hosts.containsKey( host.getName() ) )
                    throw new NfvReaderException( "loadHosts: found duplicate Host" );

                hosts.put( host.getName(), host );

                /*
                 * Manage nodes references hosted on current host
                 */
                AllocatedNodes xmlAllocatedNodes  = xmlHost.getAllocatedNodes();
                List<NodeRef> liveListXMLNodeRefs = xmlAllocatedNodes.getNode();

                hostsNodes.put( xmlHostName, new HashSet<String>() );

                if ( liveListXMLNodeRefs.size() == 0 ) {
                    continue; // this host has no allocated nodes
                }

                Set<String> currentHostNodes = hostsNodes.get( xmlHostName );
                for ( NodeRef xmlNodeRef : liveListXMLNodeRefs ) {

                    if ( !(this.validator.isValidNodeRef( xmlNodeRef )) )
                        throw new NfvReaderException( "loadHosts: found invalid NodeRef" );

                    try {
                        if ( currentHostNodes.contains( xmlNodeRef.getName() ) )
                            throw new NfvReaderException( "loadHosts: found duplicate Host NodeRef " );

                        currentHostNodes.add( xmlNodeRef.getName() );
                    } catch ( Exception e ) {
                        throw new NfvReaderException( e );
                    }
                }
            }

        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e );
        }

        return hosts;
    }




//    private
//    HashMap<String, Set<String>> loadHostsNodes( List<Host> liveListXMLHosts )
//            throws NfvReaderException {
//
//        if ( liveListXMLHosts == null )
//            throw new NfvReaderException( "loadHostsNodes: null argument" );
//
//        HashMap<String, Set<String>> hostsNodes =
//                new HashMap<String, Set<String>>();
//
//        for ( Host xmlHost : liveListXMLHosts ) {
//
//            String xmlHostName = xmlHost.getName();
//
//            AllocatedNodes xmlAllocatedNodes  = xmlHost.getAllocatedNodes();
//            List<NodeRef> liveListXMLNodeRefs = xmlAllocatedNodes.getNode();
//
//            hostsNodes.put( xmlHostName, new HashSet<String>() );
//
//            if ( liveListXMLNodeRefs.size() == 0 ) {
//                continue; // this host has no allocated nodes
//            }
//
//
//            Set<String> currentHostNodes = hostsNodes.get( xmlHostName );
//            for ( NodeRef xmlNodeRef : liveListXMLNodeRefs ) {
//
//                if ( !( this.validator.isValidNodeRef( xmlNodeRef ) ) )
//                    throw new NfvReaderException( "loadHostsNodes: found invalid NodeRef" );
//
//                try {
//                    currentHostNodes.add( xmlNodeRef.getName() );
//                } catch ( Exception e ) {
//                    throw new NfvReaderException( e.getMessage() );
//                }
//            }
//        }
//
//        return hostsNodes;
//    }




    private
    HashMap<String, RealConnection> loadConnections(
            List<Connection>          liveListXMLConnections,
            HashMap<String, RealHost> hosts                   )
                    throws NfvReaderException {

        if ( (liveListXMLConnections == null) || (hosts == null) )
            throw new NfvReaderException( "loadConnections: null arguments" );

        HashMap<String, RealConnection> connections =
                new HashMap<String, RealConnection>();

        if ( liveListXMLConnections.size() == 0 )
            return connections;

        try {

            for ( Connection xmlConnection : liveListXMLConnections ) {

                if ( !(this.validator.isValidConnection( xmlConnection )) )
                    throw new NfvReaderException(
                            "loadConnections: found invalid Connection" );

                Connection.ConnectionID connectionID =
                        xmlConnection.getConnectionID();

                String connectionName = null;
                try {

                    String   srcHostName = connectionID.getSourceHost();
                    String   dstHostName = connectionID.getDestinationHost();
                    RealHost srcHost     = hosts.get( srcHostName );
                    RealHost dstHost     = hosts.get( dstHostName );

                    connectionName = srcHost.getName() + "TO" + dstHost.getName();

                } catch ( NullPointerException e ) {
                    throw new NfvReaderException(
                            "loadConnections: connection enpoint does not exist" );
                }

                /* This checks
                 *  - if connection name is valid
                 *  - if connection host end points exists in system
                 *  - if connection latency and throughput are valid values
                 */
                RealConnection connection =
                        new RealConnection(
                                connectionName,
                                xmlConnection.getLatency().getValue(),
                                xmlConnection.getAverageThroughput().getValue()
                                );
                /*
                 * Add connection
                 */
                if ( connections.containsKey( connectionName ) )
                    throw new NfvReaderException( "loadConnections: found duplicate Connection" );

                connections.put( connectionName, connection );
            }

        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e );
        }

        return connections;
    }


    private
    HashMap<String, RealVNFType> loadVNFs( List<VNF> liveListXMLVNFs )
            throws NfvReaderException {

        HashMap<String, RealVNFType> vnfs = new HashMap<String, RealVNFType>();

        if ( liveListXMLVNFs.size() == 0 )
            return vnfs; // no VNF types in the NFV System

        try {
            for ( VNF xmlVNF : liveListXMLVNFs ) {

                if ( !( this.validator.isValidVNF( xmlVNF ) ) )
                    throw new NfvReaderException( "initVNFs: found invalid VNF" );

                RealVNFType vnf =
                        new RealVNFType(
                                xmlVNF.getName(),
                                FunctionalType.fromValue( xmlVNF.getFunctionalType().value() ),
                                xmlVNF.getRequiredMemory().getValue().intValue(),
                                xmlVNF.getRequiredStorage().getValue().intValue()
                                );
                /*
                 * Add VNF
                 */
                if ( vnfs.containsKey( vnf.getName() ) )
                    throw new NfvReaderException( "loadVNFs: found duplicate VNF" );

                vnfs.put( vnf.getName(), vnf);
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e );
        }

        return vnfs;
    }



    private
    HashMap<String, RealNffg> loadNFFGs (
            List<NFFG>                      liveListXMLNFFGs,
            HashMap<String, RealHost>       hosts,
            HashMap<String, RealVNFType>    vnfs,
            HashMap<String, RealConnection> connections )
                    throws NfvReaderException {

        HashMap<String, RealNffg> nffgs = new HashMap<String, RealNffg>();

        if ( liveListXMLNFFGs.size() == 0 )
            return nffgs; // no deployed NFFGs in the NFV System


        try {

            for ( NFFG xmlNFFG : liveListXMLNFFGs ) {

                if ( !( this.validator.isValidNFFG( xmlNFFG ) ) )
                    throw new NfvReaderException( "loadNFFGs: found invalid NFFG" );

                /* This checks
                 *  - if NFFG name is valid
                 *  - if deploy time is valid
                 */
                RealNffg nffg =
                        new RealNffg(
                                xmlNFFG.getName(),
                                xmlNFFG.getDeployTime().toGregorianCalendar(),
                                new HashSet<RealNode>() /* no nodes for now */
                                );

                /*
                 * Add NFFG
                 */
                if ( nffgs.containsKey( nffg.getName() ) )
                    throw new NfvReaderException( "loadNFFGs: found duplicate NFFG" );

                nffgs.put( nffg.getName(), nffg );

                List<Node> liveListXMLNodes = xmlNFFG.getNodes().getNode();
                if ( liveListXMLNodes.size() == 0 ) {
                    continue; // this NFFG has no nodes in it
                }

                HashMap<String, RealNode> nffgNodes = new HashMap<String, RealNode>();

                for ( Node xmlNode : liveListXMLNodes ) {

                    if ( !( this.validator.isValidNode( xmlNode ) ) )
                        throw new NfvReaderException(
                                "loadNFFGs: found invalid Node" );

                    /* This checks:
                     *  - if the node really belongs to current NFFG
                     */
                    if ( xmlNode.getAssociatedNFFG().compareTo( xmlNFFG.getName()) != 0 )
                        throw new NullPointerException(
                                "loadNFFGs: nffg-node inconsistency" );

                    /* This checks:
                     *  - if node name is valid
                     *  - is node is hosted by an existent host
                     *  - if VNF type exists in the NFV System
                     */
                    RealNode node =
                            new RealNode(
                                    xmlNode.getName(),
                                    hosts.get( xmlNode.getHostingHost() ),
                                    nffg,
                                    vnfs.get( xmlNode.getFunctionalType() ),
                                    new HashSet<RealLink>() /* no links for now */
                                    );

                    /*
                     * Add node to hosting Host
                     *
                     * This also checks:
                     *  - if Host can host node (memory, storage and maxVNFs on Host)
                     */
                    hosts.get( xmlNode.getHostingHost() ).addNode( node );

                    /*
                     * Add node to NFFG
                     */
                    nffg.addNode( node );

                    /*
                     * Gather all NFFG's nodes
                     */
                    if ( nffgNodes.containsKey( node.getName() ) )
                        throw new NfvReaderException( "loadNFFGs: found duplicate Node in NFFG" );

                    nffgNodes.put( node.getName(), node );
                }

                for ( Node xmlNode : liveListXMLNodes ) {

                    List<Link>  liveListXMLLinks = xmlNode.getLinks().getLink();

                    if ( liveListXMLLinks.size() == 0 ) {
                        continue; // this node has no links
                    }

                    for ( Link xmlLink : liveListXMLLinks ) {

                        if ( !( this.validator.isValidLink( xmlLink ) ) )
                            throw new NfvReaderException(
                                    "loadNFFGs: found invalid Link" );

                        if ( xmlLink.getSourceNode().compareTo( xmlNode.getName() ) != 0 )
                            throw new NfvReaderException(
                                    "loadNFFGs: link-node inconsistency" );


                        /* This checks
                         *  - if link name is valid
                         *  - if source and destination node belong to current NFFG
                         *  - if latency an throughput have valid values ( >0 )
                         */
                        Latency    linkLatency    = xmlLink.getMaxLatency();
                        Throughput linkThroughput = xmlLink.getMinThroughput();

                        int   realLatency    = ( linkLatency    == null ? 0 : linkLatency.getValue()    );
                        float realThroughput = ( linkThroughput == null ? 0 : linkThroughput.getValue() );

                        RealLink link =
                                new RealLink(
                                        xmlLink.getName(),
                                        nffgNodes.get( xmlLink.getSourceNode() ),
                                        nffgNodes.get( xmlLink.getDestinationNode() ),
                                        realLatency,
                                        realThroughput
                                        );

                        /* This checks:
                         *  - if the link follows an existent connection
                         *  - if required latency and throughput of the link
                         *    are available in the connection
                         */
                        RealNode srcNode = nffgNodes.get( xmlLink.getSourceNode() );
                        RealNode dstNode = nffgNodes.get( xmlLink.getDestinationNode() );
                        RealHost srcHost = hosts.get( srcNode.getHost().getName() );
                        RealHost dstHost = hosts.get( dstNode.getHost().getName() );
                        String connectionName = srcHost.getName() + "TO" + dstHost.getName();

                        RealConnection connection = connections.get( connectionName );

                        if ( connection == null )
                            throw new NullPointerException(
                                    "loadNFFGs: link follows inexistent connection" );

                        if ( link.getLatency() != 0 )
                            if ( !(connection.getLatency() <= link.getLatency()) )
                                throw new NullPointerException(
                                        "loadNFFGs: link-connection latency inconsistency" );

                        if ( link.getThroughput() != 0F )
                            if ( !(connection.getThroughput() >= link.getThroughput()) )
                                throw new NullPointerException(
                                        "loadNFFGs: link-connection throughput inconsistency" );

                        /*
                         * Add link to node
                         */
                        (nffgNodes.get( xmlNode.getName() )).addLink( link );
                    }
                }
            }

        } catch ( NullPointerException | IllegalArgumentException e ) {
//            System.err.println( e.getMessage() );
//            e.printStackTrace();
            throw new NfvReaderException( e );
        }

        return nffgs;
    }

}
