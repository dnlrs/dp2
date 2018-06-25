package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;
import it.polito.dp2.NFV.sol3.service.UnknownNameException;


/**
 * An implementation of the {@link NfvReader} interface.
 * <p>
 * This class reads, through the {@link NfvSystemLoader}, data from an
 * XML File.
 * <p>
 * If anything goes wrong during unmarshalling or loading of data an
 * empty NFV System is returned. This is done in order to avoid
 * deploying an inconsistent NFV System.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvSystem implements NfvReader {

    private final static Logger        logger = Logger.getLogger( NfvSystem.class.getName() );
    private final static NfvSystemDBMS db     = NfvSystemDBMS.getInstance();


    static {
        try {

            logger.log( Level.INFO, "NfvDeployer initialization started.");

            NfvSystemLoader loader = new NfvSystemLoader();
            loader.loadFromGenerator( db );

            NfvSystemDeployer deployer = new NfvSystemDeployer();
            deployer.deployNFFG( "Nffg0" );

            logger.log( Level.INFO, "NfvDeployer initialized correctly." );

        } catch ( NfvReaderException e ) {
            logger.log( Level.SEVERE,
                    "Could not initialize application: " + e.getMessage() );
        } catch ( UnknownNameException e ) {
            logger.log( Level.SEVERE,
                    "Could not find \"Nffg0\": " + e.getMessage() );
        } catch ( AlreadyLoadedException e ) {
            logger.log( Level.SEVERE,
                    "\"Nffg0\" is already loaded: " + e.getMessage() );
        } catch ( ServiceException e ) {
            logger.log( Level.SEVERE,
                    "Service exception: " + e.getMessage() );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "Unknown exception: " + e.getMessage() );
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
            return null;
        } catch ( Exception e ) {
            System.err.println( "getConnectionPerformance: unexpected exception" );
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
            result = db.getHost( hostName );
        } catch ( NullPointerException e ) {
            return null;
        } catch ( Exception e ) {
            System.err.println( "getHost: unexpected exception" );
            return null;
        }

        return result;
    }


    @Override
    public Set<HostReader> getHosts() {

        Set<HostReader> result = null;
        try {
            result = new LinkedHashSet<HostReader>( db.getHosts() );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getHosts exception: " + e.getMessage() );
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
            result = db.getNFFG( nffgName );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getNffg exception: " + e.getMessage() );
            return null;
        }

        return result;
    }

    @Override
    public Set<NffgReader> getNffgs( Calendar date ) {

        Set<NffgReader> result = null;
        try {
            result = new LinkedHashSet<NffgReader>( db.getNFFGs( date ) );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getNffgs exception: " + e.getMessage() );
            return new LinkedHashSet<NffgReader>();
        }

        return result;
    }

    public void addNffg( String nffgName )
            throws ServiceException {

        RealNffg nffg = new RealNffg(
                                nffgName,
                                Calendar.getInstance(),
                                new HashSet<RealNode>() );

        try {
            db.addNffg( nffg );

            NfvSystemDeployer deployer = new NfvSystemDeployer();
            deployer.deployNFFG( nffg.getName() );

        } catch ( NullPointerException
                  | AlreadyLoadedException
                  | UnknownNameException e ) {
            throw new ServiceException();
        }
    }

    public void deleteNffg( String nffgName ) {

        RealNffg nffg = db.getNFFG( nffgName );

        if ( nffg == null )
            return; // nffg already deleted or doesn't exist


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

//    public void deployNffg( String nffgName )
//            throws ServiceException {
//
//        NfvSystemDeployer deployer = new NfvSystemDeployer();
//        try {
//            deployer.deployNFFG( "Nffg0" );
//        } catch ( UnknownNameException | AlreadyLoadedException e ) {
//            throw new ServiceException();
//        }
//    }





    //////////////////////////////////////////////////////////////////////////
    // VNFs
    //////////////////////////////////////////////////////////////////////////



    @Override
    public Set<VNFTypeReader> getVNFCatalog() {

        Set<VNFTypeReader> result = null;
        try {
            result = new LinkedHashSet<VNFTypeReader>( db.getVNFCatalog() );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getVNFCatalog exception: " + e.getMessage() );
            return new LinkedHashSet<VNFTypeReader>();
        }

        return result;
    }

    public VNFTypeReader getVNF( String vnfName ) {

        VNFTypeReader result = null;
        try {
            result = db.getVNF( vnfName );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getVNF exception: " + e.getMessage() );
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
            result = new LinkedHashSet<NodeReader>( db.getNodes( null ) );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getNodes exception: " + e.getMessage() );
            return new LinkedHashSet<NodeReader>();
        }

        return result;
    }

    public NodeReader getNode( String nodeName ) {

        NodeReader result = null;
        try {
            result = db.getNode( nodeName );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getNode exception: " + e.getMessage() );
            return null;
        }

        return result;
    }

    public void addNode(
            String nodeName,
            String hostingHost,
            String functionalType,
            String associatedNFFG )
            throws ServiceException {


        RealVNFType vnf  = db.getVNF( functionalType );
        RealNffg    nffg = db.getNFFG( associatedNFFG );

        if ( (vnf == null) || (nffg == null) )
            throw new ServiceException();

        int requiredMemory  = vnf.getRequiredMemory();
        int requiredStorage = vnf.getRequiredStorage();

        RealHost host = null;
        if ( hostingHost != null ) {

            host = db.getHost( hostingHost );
            if ( host == null ) {
                host = findAvailableHost( requiredMemory, requiredStorage );
            } else
                if ( !(host.isAvailable( requiredMemory, requiredStorage )) ) {
                    host = findAvailableHost( requiredMemory, requiredStorage );
                }


        } else {
            host = findAvailableHost( requiredMemory, requiredStorage );
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

        } catch ( NullPointerException e ) {
            throw new ServiceException();
        }
    }

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


    private RealHost findAvailableHost(
            int requiredMemory,
            int requiredStorage ) {

        for ( RealHost host : db.getHosts() ) {
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
            result = new LinkedHashSet<LinkReader>(
                                db.getLinks( nffgName, null ) );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getLinkes exception: " + e.getMessage() );
            return new LinkedHashSet<LinkReader>();
        }

        return result;
    }

    public LinkReader getLink( String nffgName, String linkName ) {

        LinkReader result = null;
        try {
            result = db.getLink( nffgName, linkName );
        } catch ( Exception e ) {
            logger.log( Level.SEVERE,
                    "getLink exception: " + e.getMessage() );
            return null;
        }

        return result;
    }

    public void addLink(
            String linkName,
            String srcNodeName,
            String dstNodeName,
            float throughput,
            int latency )
                    throws ServiceException {

        RealNode srcNode = db.getNode( srcNodeName );
        RealNode dstNode = db.getNode( dstNodeName );

        NffgReader nffg = srcNode.getNffg();

        if ( (srcNode == null)
                || (dstNode == null)
                || (nffg == null)    )
            throw new ServiceException();

        try {

            RealLink link = new RealLink(
                                    linkName, srcNode, dstNode,
                                    latency, throughput );

            db.addLink( nffg.getName(), link );
            srcNode.addLink( link );

            NfvSystemDeployer deployer = new NfvSystemDeployer();
            deployer.deployLink( link );

        } catch ( NullPointerException e ) {
            logger.log( Level.SEVERE,
                    "addLink exception: " + e.getMessage() );
            throw new ServiceException();
        }

    }

    public void deleteLink( String nffgName, String linkName ) {

        RealLink link = db.getLink( nffgName, linkName );

        if ( link == null )
            return; // link already removed or not present

        NfvSystemDeployer deployer = new NfvSystemDeployer();
        deployer.unDeployLink( link );

        RealNode srcNode = db.getNode( link.getSourceNode().getName() );

        srcNode.removeLink( link );
        db.removeLink( nffgName, linkName );
    }

}
