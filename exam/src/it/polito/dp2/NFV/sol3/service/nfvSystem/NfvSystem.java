package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;
import it.polito.dp2.NFV.sol3.service.ServiceException;
import it.polito.dp2.NFV.sol3.service.UnknownNameException;


/**
 * An implementation of the {@link NfvReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvSystem implements NfvReader {

    private final static Logger        logger = Logger.getLogger( System.class.getName() );
    private final static NfvSystemDBMS db     = NfvSystemDBMS.getInstance();

    private final static Object lock = new Object();


    static {
        try {

            logger.info( "NfvDeployer initialization started.");

            NfvSystemLoader loader = new NfvSystemLoader();
            loader.loadFromGenerator( db );

            NfvSystemDeployer deployer = new NfvSystemDeployer();
            deployer.deployNFFG( "Nffg0" );

            logger.info( "NfvDeployer initialized correctly." );

        } catch ( NfvReaderException e ) {
            logger.severe( "Could not initialize application: " + e.getMessage() );
        } catch ( UnknownNameException e ) {
            logger.severe( "Could not find \"Nffg0\": " + e.getMessage() );
        } catch ( AlreadyLoadedException e ) {
            logger.severe( "\"Nffg0\" is already loaded: " + e.getMessage() );
        } catch ( ServiceException e ) {
            logger.severe( "Service exception: " + e.getMessage() );
        } catch ( Exception e ) {
            logger.severe( "Unknown exception: " + e.getMessage() );
        }
    }

    public NfvSystem() {}


    //////////////////////////////////////////////////////////////////////////
    // CONNECTIONS
    //////////////////////////////////////////////////////////////////////////

    @Override
    public ConnectionPerformanceReader
    getConnectionPerformance( HostReader srcHostI, HostReader dstHostI ) {

        ConnectionPerformanceReader result = null;
        try {
            result = db.getConnectionPerformance( srcHostI.getName()+"TO"+dstHostI.getName() );
        } catch ( NullPointerException e ) {
            logger.severe(  "getConnectionPerformance:" + e.getMessage() );
            return null;
        } catch ( Exception e ) {
            logger.severe( "getConnectionPerformance: unexpected exception " + e.getMessage() );
            return null;
        }

        return result;
    }




    //////////////////////////////////////////////////////////////////////////
    // HOSTS
    //////////////////////////////////////////////////////////////////////////

    @Override
    public HostReader getHost( String hostName ) {


        HostReader result = null;
        try {
            synchronized ( lock ) {
                result = db.getHost( hostName );
            }
        } catch ( NullPointerException e ) {
            return null;
        } catch ( Exception e ) {
            logger.severe( "getHost: unexpected exception " + e.getMessage() );
            return null;
        }

        return result;
    }


    @Override
    public Set<HostReader> getHosts() {

        Set<HostReader> result = null;
        try {
            synchronized ( lock ) {
                result = new LinkedHashSet<HostReader>( db.getHosts() );
            }
        } catch ( Exception e ) {
            logger.severe( "getHosts: " + e.getMessage() );
            return new LinkedHashSet<HostReader>();
        }

        return result;
    }




    //////////////////////////////////////////////////////////////////////////
    // NFFGs
    //////////////////////////////////////////////////////////////////////////


    @Override
    public NffgReader getNffg( String nffgName ) {

        NffgReader result = null;
        try {
            synchronized ( lock ) {
                result = db.getNFFG( nffgName );
            }
        } catch ( Exception e ) {
            logger.severe( "getNffg: " + e.getMessage() );
            return null;
        }

        return result;
    }

    @Override
    public Set<NffgReader> getNffgs( Calendar date ) {

        Set<NffgReader> result = null;
        try {
            synchronized ( lock ) {
                result = new LinkedHashSet<NffgReader>( db.getNFFGs( date ) );
            }
        } catch ( Exception e ) {
            logger.severe( "getNffgs: " + e.getMessage() );
            return new LinkedHashSet<NffgReader>();
        }

        return result;
    }


    /**
     * Adds a NFFG to the system and deploys it into the neo4j Service.
     *
     * @param nffgName
     * @throws ServiceException
     */
    public void addNffg( String nffgName )
            throws ServiceException {

        RealNffg nffg = new RealNffg(
                                nffgName,
                                Calendar.getInstance(),
                                new HashSet<RealNode>() );

        try {
            synchronized ( lock ) {
                db.addNffg( nffg );

                NfvSystemDeployer deployer = new NfvSystemDeployer();
                deployer.deployNFFG( nffg.getName() );
            }

        } catch ( NullPointerException
                  | AlreadyLoadedException
                  | UnknownNameException e ) {
            logger.severe( "addNffg: " + e.getMessage() );
            throw new ServiceException( e.getMessage() );
        }
    }


    /**
     * Remove a NFFG from the system and undeploys it from the neo4j Service
     *
     * @param nffgName
     */
    public void deleteNffg( String nffgName ) {

        RealNffg nffg = db.getNFFG( nffgName );

        if ( nffg == null )
            return; // nffg already deleted or doesn't exist

        synchronized ( lock ) {
            NfvSystemDeployer deployer = new NfvSystemDeployer();
            deployer.unDeployNffg( nffg );


            for ( NodeReader node : nffg.getNodes() ) {
                for ( LinkReader link : node.getLinks() ) {
                    deleteLink( nffgName, link.getName() );
                }
            }

            try {
                for ( NodeReader node : nffg.getNodes() ) {
                    deleteNode( node.getName() );
                }
            } catch ( ServiceException e ) {} // should never happen

            db.removeNffg( nffgName );
        }
    }



    //////////////////////////////////////////////////////////////////////////
    // VNFs
    //////////////////////////////////////////////////////////////////////////



    @Override
    public Set<VNFTypeReader> getVNFCatalog() {

        Set<VNFTypeReader> result = null;
        try {
            result = new LinkedHashSet<VNFTypeReader>( db.getVNFCatalog() );
        } catch ( Exception e ) {
            logger.severe( "getVNFCatalog: " + e.getMessage() );
            return new LinkedHashSet<VNFTypeReader>();
        }

        return result;
    }

    public VNFTypeReader getVNF( String vnfName ) {

        VNFTypeReader result = null;
        try {
            result = db.getVNF( vnfName );
        } catch ( Exception e ) {
            logger.severe( "getVNF: " + e.getMessage() );
            return null;
        }

        return result;
    }



    //////////////////////////////////////////////////////////////////////////
    // NODEs
    //////////////////////////////////////////////////////////////////////////



    public Set<NodeReader> getNodes() {

        Set<NodeReader> result = null;
        try {
            synchronized ( lock ) {
                result = new LinkedHashSet<NodeReader>( db.getNodes( null ) );
            }
        } catch ( Exception e ) {
            logger.severe( "getNodes: " + e.getMessage() );
            return new LinkedHashSet<NodeReader>();
        }

        return result;
    }

    public NodeReader getNode( String nodeName ) {

        NodeReader result = null;
        try {
            synchronized ( lock ) {
                result = db.getNode( nodeName );
            }
        } catch ( Exception e ) {
            logger.severe( "getNode: " + e.getMessage() );
            return null;
        }

        return result;
    }

    /**
     * adds a node to the system and deploys it to the neo4j Service.
     *
     * @param nodeName
     * @param hostingHost
     * @param functionalType
     * @param associatedNFFG
     * @throws ServiceException
     */
    public void addNode(
            String nodeName,
            String hostingHost,         /* may be null if no suggested host */
            List<String> avoidHosts,
            String functionalType,
            String associatedNFFG )
            throws ServiceException {


        RealVNFType vnf  = db.getVNF( functionalType );
        RealNffg    nffg = db.getNFFG( associatedNFFG );

        if ( (vnf == null) )
            throw new ServiceException("addNode: VNF \""+ functionalType +"\" doesn't exist");

        if ( (nffg == null) )
            throw new ServiceException("addNode: NFFG doesn't exist");

        int requiredMemory  = vnf.getRequiredMemory();
        int requiredStorage = vnf.getRequiredStorage();


        synchronized ( lock ) {
            RealHost host = null;
            if ( hostingHost != null ) {

                host = db.getHost( hostingHost );
                if ( host == null ) {
                    host = findAvailableHost( requiredMemory, requiredStorage, avoidHosts );
                } else
                    if ( !(host.isAvailable( requiredMemory, requiredStorage )) ) {
                        host = findAvailableHost( requiredMemory, requiredStorage, avoidHosts );
                    }


            } else {
                host = findAvailableHost( requiredMemory, requiredStorage, avoidHosts );
            }

            if ( host == null )
                throw new ServiceException();

            RealNode node =
                    new RealNode( nodeName, host, nffg, vnf, new HashSet<RealLink>() );

            try {
                db.addNode( node );
                nffg.addNode( node );
                host.addNode( node );

                NfvSystemDeployer deployer = new NfvSystemDeployer();
                deployer.deployNode( node );

            } catch ( NullPointerException
                    | AlreadyLoadedException
                    | IllegalArgumentException e ) {
                throw new ServiceException();
            } catch ( ServiceException e ) {
            }
        }
    }

    /**
     * Removes a node from the system and also undeploys it from the
     * neo4j Service.
     *
     * @param nodeName
     * @throws ServiceException
     */
    public void deleteNode( String nodeName )
            throws ServiceException {

        RealNode node = db.getNode( nodeName );
        if ( node == null )
            return; // node already removed or doesn't exist

        if ( node.getLinks().size() > 0 )
            throw new ServiceException(); // can't remove a node with existing links

        for ( NodeReader srcNode : node.getNffg().getNodes() ) {
            if ( srcNode.getName().compareTo( node.getName() ) == 0 ) {
                continue;
            }

            for ( LinkReader link : srcNode.getLinks() ) {
                if ( link.getDestinationNode().getName().compareTo( node.getName() ) == 0 )
                    throw new ServiceException(); // can't remove a node with existing links
            }
        }

        NfvSystemDeployer deployer = new NfvSystemDeployer();
        synchronized ( lock ) {
            deployer.unDeployNode( node );

            RealHost host = db.getHost( node.getHost().getName() );

            if ( host != null ) {
                host.removeNode( node.getName() );
            }

            RealNffg nffg = db.getNFFG( node.getNffg().getName() );

            if ( nffg != null ) {
                nffg.removeNode( node.getName() );
            }

            db.removeNode( node.getName() );
        }
    }


    /**
     * Searches for a host with enough free memory and storage and at least one
     * VNF slot free.
     *
     * @param requiredMemory
     * @param requiredStorage
     * @return
     */
    private RealHost findAvailableHost(
            int requiredMemory,
            int requiredStorage,
            List<String> avoidHosts) {

        for ( RealHost host : db.getHosts() ) {
        	if ( avoidHosts != null )
	        	if ( avoidHosts.contains(host.getName() )) {
                    continue;
                }

            if ( host.isAvailable( requiredMemory, requiredStorage ) )
                return host;
        }

        return null;
    }






    //////////////////////////////////////////////////////////////////////////
    // LINKs
    //////////////////////////////////////////////////////////////////////////

    public Set<LinkReader> getLinks( String nffgName ) {

        Set<LinkReader> result = null;
        try {
            synchronized ( lock ) {
                result = new LinkedHashSet<LinkReader>(
                                db.getLinks( nffgName, null ) );
            }
        } catch ( Exception e ) {
            logger.severe( "getLinkes: " + e.getMessage() );
            return new LinkedHashSet<LinkReader>();
        }

        return result;
    }

    public LinkReader getLink( String nffgName, String linkName ) {

        LinkReader result = null;
        try {
            synchronized ( lock ) {
                result = db.getLink( nffgName, linkName );
            }
        } catch ( Exception e ) {
            logger.severe( "getLink: " + e.getMessage() );
            return null;
        }

        return result;
    }

    /**
     * Adds a link to the system and deploys it into the Neo4j Service.
     *
     * @param linkName
     * @param srcNodeName
     * @param dstNodeName
     * @param throughput
     * @param latency
     * @throws ServiceException
     */
    public void addLink(
            String linkName,
            String srcNodeName,
            String dstNodeName,
            float throughput,
            int latency )
                    throws ServiceException {

        RealNode srcNode = db.getNode( srcNodeName );
        RealNode dstNode = db.getNode( dstNodeName );

        if ( srcNode == null )
            throw new ServiceException( "addLink: source node not found" );

        if ( dstNode == null )
            throw new ServiceException( "addLink: dest node not found" );


        NffgReader nffg = srcNode.getNffg();
        if ( nffg == null )
            throw new ServiceException( "addLink: nffg not found" );

        try {

            RealLink link = new RealLink(
                                    linkName, srcNode, dstNode,
                                    latency, throughput );

            synchronized ( lock ) {
                db.addLink( nffg.getName(), link );
                srcNode.addLink( link );

                NfvSystemDeployer deployer = new NfvSystemDeployer();
                deployer.deployLink( link );
            }

        } catch ( NullPointerException
                  | AlreadyLoadedException e ) {
            logger.severe( "addLink: " + e.getMessage() );
            throw new ServiceException();
        }

    }

    /**
     * Removes a link from the system and undeploys it also from the
     * Neo4j Service.
     *
     * @param nffgName
     * @param linkName
     */
    public void deleteLink( String nffgName, String linkName ) {

        RealLink link = db.getLink( nffgName, linkName );

        if ( link == null )
            return; // link already removed or not present

        NfvSystemDeployer deployer = new NfvSystemDeployer();

        synchronized ( lock ) {
            deployer.unDeployLink( link );

            RealNode srcNode = db.getNode( link.getSourceNode().getName() );

            srcNode.removeLink( link );
            db.removeLink( nffgName, linkName );
        }
    }

}
