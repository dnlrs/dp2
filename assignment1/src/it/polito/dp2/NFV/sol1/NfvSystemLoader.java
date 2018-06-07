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
import it.polito.dp2.NFV.sol1.jaxb.Connection;
import it.polito.dp2.NFV.sol1.jaxb.Host;
import it.polito.dp2.NFV.sol1.jaxb.InfrastructureNetwork;
import it.polito.dp2.NFV.sol1.jaxb.Link;
import it.polito.dp2.NFV.sol1.jaxb.NFFG;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType;
import it.polito.dp2.NFV.sol1.jaxb.Node;
import it.polito.dp2.NFV.sol1.jaxb.NodeRef;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

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

        NFVSystemType xmlNfvSystem = unmarshallXMLFile();


        InfrastructureNetwork in = xmlNfvSystem.getIN();
        if ( !( this.validator.isValidIN( in ) ) )
            throw new NfvReaderException(
                    "loadFromXMLFile: invalid Infrastructure Network" );

        /*
         * Retrieve Hosts from XML File
         */
        List<Host> liveListXMLHosts = in.getHosts().getHost();

        if ( liveListXMLHosts.size() == 0 )
            return; // no hosts, no connections, no NFFGs, no system

        HashMap<String, RealHost>    hosts      = initHosts( liveListXMLHosts );
        HashMap<String, Set<String>> hostsNodes = initHostsNodes( liveListXMLHosts );

        /*
         * Retrieve Connections from XML File
         */
        List<Connection> liveListXMLConnections     = in.getConnections().getConnection();
        HashMap<String, RealConnection> connections = initConnections( liveListXMLConnections, hosts );

        /*
         * Retrieve VNFs from generator
         */
        List<VNF> liveListXMLVNFs = xmlNfvSystem.getCatalogue().getVnf();
        HashMap<String, RealVNFType> vnfs =
                initVNFs(liveListXMLVNFs);

        /*
         * Retrieve NFFGs from generator along with nodes and links
         */
        List<NFFG> liveListXMLNFFGs = xmlNfvSystem.getDeployedNFFGs().getNffg();
        HashMap<String, RealNffg> nffgs = initNFFGs( liveListXMLNFFGs, hosts, vnfs, connections );


        /*
         * Check if data is consistent
         */
        for ( RealHost host : hosts.values() ) {
            Set<NodeReader> nodeInterfaces = host.getNodes();
            for ( String nodeName : hostsNodes.get( host.getName() ) ) {
                boolean found = false;
                for ( NodeReader nodeI : nodeInterfaces ) {
                    if ( nodeI.getName().compareTo(nodeName) == 0 ) {
                        found = true;
                        break;
                    }
                }

                if ( !found )
                    throw new NfvReaderException( "loadFromXMLFile: hosted nodes inconsistency" );
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
                db.addConnectionPerformance(connection.getName(), connection);
            }
            /* add NFFGs, nodes and links */
            db.addNFFGs( new HashSet<RealNffg>( nffgs.values() ) );

        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e.getMessage() );
        }
    }






    private NFVSystemType unmarshallXMLFile()
            throws NfvReaderException {

        NFVSystemType nfvSystem = null;
        try {

            JAXBContext jc  = JAXBContext.newInstance( JAXB_CLASSES_PACKAGE );
            Unmarshaller um = jc.createUnmarshaller();


            // Set up validation against a schema
            try {

                String schemaFile = ( System.getProperty( PROPERTY_USER_DIR ) == null
                                        ? new String( "" ) : System.getProperty( PROPERTY_USER_DIR ) );
                schemaFile = schemaFile.concat( SCHEMA_LOCATION );

                JAXBUtils.unmarshallerSetSchema( um, schemaFile );

            } catch ( SchemaFactoryConfigurationError sfce ) {
                System.err.println( sfce.getMessage() );
                System.err.println( "NfvReaderReal: Could not set unmarshaller validation schema." ); // no validation
            } catch ( Exception e ) {
                System.err.println( e.getMessage() );
                System.err.println( "NfvReaderReal: Could not set unmarshaller validation schema." ); // no validation
            }


            // get XML file name
            String xmlFileName = System.getProperty( PROPERTY_XML_FILE );
            if ( xmlFileName == null )
                throw new NfvReaderException( "NfvReaderReal: Could not get XML file to read." );

            // unmarshal
            Object obj = um.unmarshal( new FileInputStream( xmlFileName ) );

            @SuppressWarnings( "unchecked" )
            JAXBElement<NFVSystemType> element = (JAXBElement<NFVSystemType>) obj;

            nfvSystem = element.getValue();


        } catch ( Exception e ) {
            e.printStackTrace();
            throw new NfvReaderException( e.getMessage() );
        }

        return nfvSystem;
    }




    private HashMap<String, RealHost> initHosts( List<Host> liveListXMLHosts )
            throws NfvReaderException {

        if ( liveListXMLHosts == null )
            throw new NfvReaderException( "initHosts: null argument" );

        XMLValidator  validator = new XMLValidator();

        HashMap<String, RealHost> hosts = new HashMap<String, RealHost>();
        try {
            for ( Host xmlHost : liveListXMLHosts ) {

                if ( !( validator.isValidHost( xmlHost ) ) )
                    throw new NfvReaderException( "initHosts: found invalid Host" );

                /* This checks
                 *  - if host name is valid
                 *  - if host memory and storage and maxVNFs are valid values
                 */
                RealHost host =
                        new RealHost(
                                xmlHost.getName(),
                                xmlHost.getInstalledMemory().getValue().intValue(),
                                xmlHost.getInstalledStorage().getValue().intValue(),
                                xmlHost.getMaxVNFs(),
                                new HashSet<RealNode>() /* no nodes for now */
                                );
                /*
                 * Add host
                 */
                hosts.put( host.getName(), host );
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
//            e.printStackTrace();
            throw new NfvReaderException( e );
        }

        return hosts;
    }




    private HashMap<String, Set<String>> initHostsNodes( List<Host> liveListXMLHosts )
            throws NfvReaderException {

        HashMap<String, Set<String>> hostsNodes = new HashMap<String, Set<String>>();

        for ( Host xmlHost : liveListXMLHosts ) {

            List<NodeRef> liveListXMLNodeRefs = xmlHost.getAllocatedNodes().getNode();

            hostsNodes.put(xmlHost.getName(), new HashSet<String>() );
            if ( liveListXMLNodeRefs.size() == 0 ) {
                continue; // this host has no allocated nodes
            }


            Set<String> currentHostNodes = hostsNodes.get( xmlHost.getName() );
            for ( NodeRef xmlNodeRef : liveListXMLNodeRefs ) {

                if ( !( this.validator.isValidNodeRef( xmlNodeRef ) ) )
                    throw new NfvReaderException( "initHosts: found invalid NodeRef" );

                try {
                    currentHostNodes.add( xmlNodeRef.getName() );
                } catch ( Exception e ) {
                    throw new NfvReaderException( e.getMessage() );
                }
            }
        }

        return hostsNodes;
    }




    private HashMap<String, RealConnection> initConnections(
            List<Connection> liveListXMLConnections,
            HashMap<String, RealHost> hosts )
                    throws NfvReaderException {

        HashMap<String, RealConnection> connections = new HashMap<String, RealConnection>();
        try {
            if ( liveListXMLConnections.size() > 0 ) {

                for ( Connection xmlConnection : liveListXMLConnections ) {

                    if ( !( this.validator.isValidConnection( xmlConnection ) ) )
                        throw new NfvReaderException( "initConnections: found invalid Connection" );

                    /* This checks
                     *  - if connection name is valid
                     *  - if connection host end points exists in system
                     *  - if connection latency and throughput are valid values
                     */
                    Connection.ConnectionID connectionID = xmlConnection.getConnectionID();
                    RealHost srcHost = hosts.get( connectionID.getSourceHost() );
                    RealHost dstHost = hosts.get( connectionID.getDestinationHost() );
                    String connectionName = srcHost.getName() + "TO" + dstHost.getName();

                    RealConnection connection =
                            new RealConnection(
                                    connectionName,
                                    xmlConnection.getLatency().getValue(),
                                    xmlConnection.getAverageThroughput().getValue()
                                    );
                    /*
                     * Add connection
                     */
                    connections.put( connectionName, connection );
                }
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e.getMessage() );
        }

        return connections;
    }


    private HashMap<String, RealVNFType> initVNFs( List<VNF> liveListXMLVNFs )
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
                    vnfs.put( vnf.getName(), vnf);
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            throw new NfvReaderException( e.getMessage() );
        }

        return vnfs;
    }



    private HashMap<String, RealNffg> initNFFGs (
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
                    throw new NfvReaderException( "initNFFGs: found invalid NFFG" );

                /* This checks
                 *  - if NFFG name is valid
                 *  - if deploy time is valid
                 */
                RealNffg nffg =
                        new RealNffg(
                                xmlNFFG.getName(),
                                xmlNFFG.getDeployTime().toGregorianCalendar(),
                                new HashSet<RealNode>()
                                );

                HashMap<String, RealNode> nffgNodes = new HashMap<String, RealNode>();

                List<Node> liveListXMLNodes = xmlNFFG.getNodes().getNode();
                if ( liveListXMLNodes.size() > 0 ) {

                    for ( Node xmlNode : liveListXMLNodes ) {

                        if ( !( this.validator.isValidNode( xmlNode ) ) )
                            throw new NfvReaderException( "initNFFGs: found invalid Node" );

                        /* This checks:
                         *  - if the node really belongs to current NFFG
                         */
                        if ( xmlNode.getAssociatedNFFG().compareTo( xmlNFFG.getName()) != 0 )
                            throw new NullPointerException( "initNFFGs: nffg-node inconsistency" );

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

                        /* Add node to hosting Host
                         *
                         * This checks:
                         *  - if Host can host node (memory, storage and maxVNFs on Host)
                         */
                        hosts.get( xmlNode.getHostingHost() ).addNode( node );

                        /* Add node to NFFG
                         */
                        nffg.addNode( node );

                        /*
                         * Gather all NFFG's nodes
                         */
                        nffgNodes.put( node.getName(), node );
                    }

                    for ( Node xmlNode : liveListXMLNodes ) {

                        List<Link>  liveListXMLLinks = xmlNode.getLinks().getLink();
                        if ( liveListXMLLinks.size() > 0 ) {

                            for ( Link xmlLink : liveListXMLLinks ) {

                                if ( !( this.validator.isValidLink( xmlLink ) ) )
                                    throw new NfvReaderException( "initNFFGs: found invalid Link" );

                                /* This checks
                                 *  - if link name is valid
                                 *  - if source and destination node belong to current NFFG
                                 *  - if latency an throughput have valid values ( >0 )
                                 */

                                RealLink link =
                                        new RealLink(
                                                xmlLink.getName(),
                                                nffgNodes.get( xmlLink.getSourceNode() ),
                                                nffgNodes.get( xmlLink.getDestinationNode() ),
                                                ( xmlLink.getMaxLatency()    == null ? 0 : xmlLink.getMaxLatency().getValue() ),
                                                ( xmlLink.getMinThroughput() == null ? 0f : xmlLink.getMinThroughput().getValue() )
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

                                if ( ( connection.getLatency()    < link.getLatency() )  ||
                                     ( connection.getThroughput() < link.getThroughput() ) )
                                    throw new NullPointerException( "initNFFGs: link-connection inconsistency" );


                                /* Add link to node
                                 */
                                nffgNodes.get( xmlNode.getName() ).addLink( link );
                            }
                        }
                    }
                }

                nffgs.put( nffg.getName(), nffg );
            }
        } catch ( NullPointerException | IllegalArgumentException e ) {
            System.err.println( e.getMessage() );
            e.printStackTrace();
            throw new NfvReaderException( e.getMessage() );
        }

        return nffgs;
    }

}
