package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class NfvSystemDBMS {

    private ConcurrentHashMap<String, RealHost>    dbHosts;
    private ConcurrentHashMap<String, RealNffg>    dbNFFGs;
    private ConcurrentHashMap<String, RealNode>    dbNodes;
    private ConcurrentHashMap<String, RealVNFType> dbVNFs;

    private ConcurrentHashMap<String, RealConnection> dbConns;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, RealLink>> dbLinks;

    private static NfvSystemDBMS instance = null;

    private NfvSystemDBMS() {
        this.dbHosts = new ConcurrentHashMap<String, RealHost>();
        this.dbNFFGs = new ConcurrentHashMap<String, RealNffg>();
        this.dbNodes = new ConcurrentHashMap<String, RealNode>();
        this.dbVNFs  = new ConcurrentHashMap<String, RealVNFType>();
        this.dbConns = new ConcurrentHashMap<String, RealConnection>();
        this.dbLinks = new ConcurrentHashMap<String, ConcurrentHashMap<String, RealLink>>();
    }


    protected synchronized static NfvSystemDBMS getInstance() {

        if ( instance == null ) {
            instance = new NfvSystemDBMS();
        }

        return instance;
    }



    /* ----------------------------------------------------------------------
     *  DATA ACCESS
     ----------------------------------------------------------------------*/


    /**
     * Gives access to a {@link RealConnection} interface given
     * its ID "sourceHostNameTOdstHostName"
     *
     * @param  connectionID the connection ID
     * @return     a {@link RealConnection} interface, null if
     *             connection doesn't exists or the NFVSystem is empty
     */
    protected synchronized RealConnection getConnectionPerformance( String connectionID )
            throws NullPointerException {

        return this.dbConns.get( connectionID );
    }

    protected synchronized void addConnectionPerformance(
            String connectionID, RealConnection connection )
                    throws NullPointerException {

        this.dbConns.put( connectionID, connection );
    }



    /**
     * Gives access to a {@link RealHost} given the Host name.
     *
     * @param hostName the host name
     * @return         a {@link RealHost}, null if Host doesn't exist or
     *                 the NFV System is empty
     */
    protected synchronized RealHost getHost( String hostName )
            throws NullPointerException {

        if ( !( RealNamedEntity.nameIsValid( hostName ) ) )
            return null;

        return this.dbHosts.get( hostName );
    }

    protected synchronized void addHost( RealHost host )
            throws NullPointerException {

        this.dbHosts.put( host.getName() , host );
    }

    /**
     * Gives access to all {@link RealHost}s in the NFV System.
     *
     * @return the set of {@link RealHost}s in the NFV System,
     *         the set may be empty.
     */
    protected synchronized Set<RealHost> getHosts() {

        return new HashSet<RealHost>( this.dbHosts.values() );
    }

    protected synchronized void addHosts( Set<RealHost> hosts )
            throws NullPointerException {

        for ( RealHost host : hosts ) {
            addHost( host );
        }
    }



    /**
     * Gives access to a {@link RealNffg} given the NFFG name.
     *
     * @param nffgName the NFFG name
     * @return         a {@link RealNffg}, null if the NFFG
     *                 doesn't exist of the NFV System is empty
     */
    protected synchronized RealNffg getNFFG( String nffgName )
            throws NullPointerException {

        return this.dbNFFGs.get( nffgName );
    }


    protected synchronized void addNFFG( RealNffg nffg )
            throws NullPointerException {

        ConcurrentHashMap<String, RealLink> mapLinks =
                new ConcurrentHashMap<String, RealLink>();
        this.dbLinks.put( nffg.getName(), mapLinks);

        for ( RealNode node : nffg.getRealNodes() ) {
            addNode( node );

            for ( RealLink link : node.getRealLinks() ) {
                addLink( nffg.getName(), link );
            }
        }

        this.dbNFFGs.put( nffg.getName(), nffg);
    }



    /**
     * Gives access to all {@link RealNffg}s starting from
     * {@code date}.
     *
     * @param date
     * @return     a set of {@link RealNffg}s, the set may
     *             be empty
     */
    protected synchronized Set<RealNffg> getNFFGs( Calendar date )
            throws NullPointerException {

        if ( date == null )
            return new HashSet<RealNffg>( this.dbNFFGs.values() );

        Set<RealNffg> result = new HashSet<RealNffg>();

        for ( String nffgName : this.dbNFFGs.keySet() ) {
            RealNffg nffg = this.dbNFFGs.get( nffgName );

            if ( nffg.getDeployTime().compareTo(date) >= 0 ) {
                result.add( nffg );
            }
        }

        return result;
    }

    protected synchronized void addNFFGs( Set<RealNffg> nffgs )
            throws NullPointerException {

        for ( RealNffg nffg : nffgs ) {
            addNFFG( nffg );
        }
    }

    /**
     * Gives access to the VNF Types catalogue.
     *
     * @return a set of {@link RealVNFType}s,
     *         the set may be empty if the NFV System is empty
     */
    protected synchronized Set<RealVNFType> getVNFCatalog() {

        return new HashSet<RealVNFType>( this.dbVNFs.values() );
    }


    /**
     * Gives access to a {@link RealVNFType} given the name
     * of the VNF.
     *
     * @param vnfName
     * @return        a {@link RealVNFType}, null if if
     *                doesn't exist or the NFV System is empty
     */
    protected synchronized RealVNFType getVNF( String vnfName )
            throws NullPointerException {

        return this.dbVNFs.get( vnfName );
    }

    protected synchronized void addVNF( RealVNFType vnf )
            throws NullPointerException {

        this.dbVNFs.put(vnf.getName(), vnf);
    }

    protected synchronized void addCatalog( Set<RealVNFType> vnfs )
            throws NullPointerException {

        for ( RealVNFType vnf : vnfs ) {
            addVNF( vnf );
        }
    }

    /**
     * Gives access to all the {@link RealLink}s requested
     * by name from an NFFG.
     *
     * @param nffgName the NFFG name
     * @param links    the names of the
     * @return         a set of {@link RealLink}s,
     *                 the set may be empty
     */
    protected synchronized Set<RealLink> getLinks( String nffgName, Set<String> links )
            throws NullPointerException {

        Set<RealLink> result = new HashSet<RealLink>();

        ConcurrentHashMap<String, RealLink> hmLinks  = this.dbLinks.get( nffgName );

        for ( String linkName : links )
            if ( hmLinks.containsKey( linkName ) ) {
                result.add( hmLinks.get( linkName ) );
            }

        return result;
    }

    protected synchronized void addLink( String nffgName, RealLink link )
            throws NullPointerException {

        this.dbLinks.get( nffgName ).put(link.getName(), link);
    }

    protected synchronized void addLinks( String nffgName, Set<RealLink> links )
            throws NullPointerException {

        for ( RealLink link : links ) {
            addLink( nffgName, link );
        }
    }

    /**
     * Gives access to a {@link RealNode} given the Node name.
     *
     * @param nodeName
     * @return         a {@link RealNode}, null if the node
     *                 doesn't exist or the NFV System is empty
     */
    protected synchronized RealNode getNode( String nodeName )
            throws NullPointerException {

        return this.dbNodes.get( nodeName );
    }

    protected synchronized void addNode( RealNode node )
            throws NullPointerException {

        this.dbNodes.put( node.getName(), node );
    }

    /**
     * Gives access to all {@link RealNode}s requested by Node
     * names.
     *
     * @param  nodes a set with node names
     * @return       a set with {@link RealNode}s requested,
     *               the set may be empty
     */
    protected synchronized Set<RealNode> getNodes( Set<String> nodes )
            throws NullPointerException {

        Set<RealNode> result = new HashSet<RealNode>();

        for ( String nodeName : nodes )
            if ( this.dbNodes.containsKey( nodeName ) ) {
                result.add( this.dbNodes.get( nodeName ) );
            }

        return result;
    }

    protected synchronized void addNodes( Set<RealNode> nodes )
            throws NullPointerException {

        for ( RealNode node : nodes ) {
            addNode( node );
        }
    }

//
//    private synchronized void populateDBfromGenerator()
//            throws NfvReaderException {
//
//        NfvReader monitor = null;
//        try {
//
//            NfvReaderFactory factory = NfvReaderFactory.newInstance();
//            monitor = factory.newNfvReader();
//
//        } catch( FactoryConfigurationError | NfvReaderException e ) {
//            throw new NfvReaderException( e.getMessage() );
//        }
//
//        try {
//
//            // load hosts
//            for ( HostReader hostI : monitor.getHosts() ) {
//                this.dbHosts.put(hostI.getName(), new RealHost( hostI ) );
//            }
//
//            // load connections between hosts
//            for ( HostReader srcHostI : monitor.getHosts() ) {
//                for ( HostReader dstHostI : monitor.getHosts() ) {
//                    if ( monitor.getConnectionPerformance(srcHostI, dstHostI) != null ) {
//                        String connectionName =
//                                new String( srcHostI.getName() + "TO" + dstHostI.getName() );
//
//                        this.dbConns.put( connectionName,
//                                          new RealConnection(
//                                                  connectionName,
//                                                  monitor.getConnectionPerformance( srcHostI, dstHostI ) )
//                                          );
//                    }
//                }
//            }
//
//
//            // load VNF Catalogue
//            for ( VNFTypeReader vnfI : monitor.getVNFCatalog() ) {
//                this.dbVNFs.put( vnfI.getName(), new RealVNFType( vnfI ) );
//            }
//
//
//            // load NFFGs, Nodes and relative Links
//            for ( NffgReader nffgI : monitor.getNffgs(null) ) {
//
//                ConcurrentHashMap<String, RealLink> mapLinks =
//                        new ConcurrentHashMap<String, RealLink>();
//
//                for ( NodeReader nodeI : nffgI.getNodes() ) {
//                    this.dbNodes.put( nodeI.getName(), new RealNode( nodeI ) );
//
//                    for ( LinkReader linkI : nodeI.getLinks() ) {
//                        mapLinks.put( linkI.getName(), new RealLink( linkI) );
//                    }
//                }
//
//                this.dbNFFGs.put(nffgI.getName(), new RealNffg( nffgI ) );
//                this.dbLinks.put(nffgI.getName(), mapLinks );
//            }
//
//        } catch ( NullPointerException | IllegalArgumentException e ) {
//            throw new NfvReaderException( e.getMessage() );
//        }
//
//    }

}
