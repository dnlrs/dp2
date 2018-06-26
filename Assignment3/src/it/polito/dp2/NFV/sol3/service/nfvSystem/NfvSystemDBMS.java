package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;


public class NfvSystemDBMS {

    private final static Logger logger = Logger.getLogger( NfvSystemDBMS.class.getName() );

    private final ConcurrentSkipListMap<String, RealHost>    dbHosts;
    private final ConcurrentSkipListMap<String, RealNffg>    dbNFFGs;
    private final ConcurrentSkipListMap<String, RealNode>    dbNodes;
    private final ConcurrentSkipListMap<String, RealVNFType> dbVNFs;

    private final ConcurrentSkipListMap<String, RealConnection> dbConns;
    private final ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, RealLink>> dbLinks;

    private final static NfvSystemDBMS instance;

    static {
        instance = new NfvSystemDBMS();
    }

    private NfvSystemDBMS() {
        this.dbHosts = new ConcurrentSkipListMap<String, RealHost>();
        this.dbNFFGs = new ConcurrentSkipListMap<String, RealNffg>();
        this.dbNodes = new ConcurrentSkipListMap<String, RealNode>();
        this.dbVNFs  = new ConcurrentSkipListMap<String, RealVNFType>();
        this.dbConns = new ConcurrentSkipListMap<String, RealConnection>();
        this.dbLinks = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, RealLink>>();
    }


    public static NfvSystemDBMS getInstance() {
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
    protected RealConnection getConnectionPerformance( String connectionID )
            throws NullPointerException {
            return this.dbConns.get( connectionID );
    }

    /**
     * Adds a {@link RealConnection} to the database if not already present.
     *
     * @param connectionID
     * @param connection
     * @throws NullPointerException if connection was already in the database
     */
    protected void addConnectionPerformance(
            String connectionID,
            RealConnection connection )
                    throws NullPointerException {

        RealConnection oldOne =
                this.dbConns.putIfAbsent( connectionID, connection );

        if ( oldOne != null )
            throw new NullPointerException(
                    "nfvSystem: duplicate connection detected" );
    }



    /**
     * Gives access to a {@link RealHost} given the Host name.
     *
     * @param hostName the host name
     * @return         a {@link RealHost}, null if Host doesn't exist or
     *                 the NFV System is empty
     */
    protected RealHost getHost( String hostName )
            throws NullPointerException {

        if ( !(RealNamedEntity.nameIsValid( hostName )) )
            return null;

            return this.dbHosts.get( hostName );
    }

    protected void addHost( RealHost host )
            throws NullPointerException {

        RealHost oldOne =
                this.dbHosts.putIfAbsent( host.getName() , host );

        if ( oldOne != null )
            throw new NullPointerException(
                    "nfvSystem: duplicate host detected " + host.getName() );
    }

    /**
     * Gives access to all {@link RealHost}s in the NFV System.
     *
     * @return the set of {@link RealHost}s in the NFV System,
     *         the set may be empty.
     */
    protected Set<RealHost> getHosts() {
        return new LinkedHashSet<RealHost>( this.dbHosts.values() );
    }

    protected void addHosts( Set<RealHost> hosts )
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
    protected RealNffg getNFFG( String nffgName )
            throws NullPointerException {

            return this.dbNFFGs.get( nffgName );
    }


    protected void addNffg( RealNffg nffg )
            throws NullPointerException {

        ConcurrentSkipListMap<String, RealLink> mapLinks =
                new ConcurrentSkipListMap<String, RealLink>();

        synchronized ( this ) {
            RealNffg oldNffg = this.dbNFFGs.putIfAbsent( nffg.getName(), nffg);

            if ( oldNffg != null )
                throw new NullPointerException(
                        "nfvSystem: duplicate NFFG detected" );

            ConcurrentSkipListMap<String, RealLink> oldMap =
                    this.dbLinks.putIfAbsent( nffg.getName(), mapLinks);

            if ( oldMap != null )
                throw new NullPointerException(
                        "nfvSystem: DB NFFG-links inconsistency problem" );


            for ( RealNode node : nffg.getRealNodes() ) {
                addNode( node );

                for ( RealLink link : node.getRealLinks() ) {
                    addLink( nffg.getName(), link );
                }
            }
        }
    }

    protected void removeNffg( String nffgName ) {

        this.dbLinks.remove( nffgName );
        this.dbNFFGs.remove( nffgName );
    }



    /**
     * Gives access to all {@link RealNffg}s starting from
     * {@code date}.
     *
     * @param date
     * @return     a set of {@link RealNffg}s, the set may
     *             be empty
     */
    protected Set<RealNffg> getNFFGs( Calendar date )
            throws NullPointerException {


        if ( date == null )
            return new LinkedHashSet<RealNffg>( this.dbNFFGs.values() );

        Set<RealNffg> result = new LinkedHashSet<RealNffg>();

        for ( RealNffg nffg : this.dbNFFGs.values() ) {
            if ( nffg.getDeployTime().compareTo( date ) >= 0 ) {
                result.add( nffg );
            }
        }

        return result;
    }


    protected void addNFFGs( Set<RealNffg> nffgs )
            throws NullPointerException {

        for ( RealNffg nffg : nffgs ) {
            addNffg( nffg );
        }
    }

    /**
     * Gives access to the VNF Types catalogue.
     *
     * @return a set of {@link RealVNFType}s,
     *         the set may be empty if the NFV System is empty
     */
    protected Set<RealVNFType> getVNFCatalog() {
        return new LinkedHashSet<RealVNFType>( this.dbVNFs.values() );
    }


    /**
     * Gives access to a {@link RealVNFType} given the name
     * of the VNF.
     *
     * @param vnfName
     * @return        a {@link RealVNFType}, null if if
     *                doesn't exist or the NFV System is empty
     */
    protected RealVNFType getVNF( String vnfName )
            throws NullPointerException {
            return this.dbVNFs.get( vnfName );
    }


    protected void addVNF( RealVNFType vnf )
            throws NullPointerException {

        RealVNFType oldVNF = this.dbVNFs.putIfAbsent( vnf.getName(), vnf );

        if ( oldVNF != null )
            throw new NullPointerException(
                    "nfvSystem: duplicate VNFType detected" );
    }


    protected void addCatalog( Set<RealVNFType> vnfs )
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
    protected Set<RealLink> getLinks( String nffgName, Set<String> links )
                    throws NullPointerException {

        ConcurrentSkipListMap<String, RealLink> hmLinks =
                this.dbLinks.get( nffgName );

        if ( links == null )
            return new LinkedHashSet<RealLink>( hmLinks.values() );

        Set<RealLink> result = new LinkedHashSet<RealLink>();

        for ( RealLink link : hmLinks.values() ) {
            if ( hmLinks.containsKey( link.getName() ) ) {
                result.add( link );
            }
        }

        return result;
    }


    protected RealLink getLink( String nffgName, String linkName )
                    throws NullPointerException {

        return (this.dbLinks.get( nffgName )).get( linkName );
    }


    protected void addLinks( String nffgName, Set<RealLink> links )
            throws NullPointerException {

        for ( RealLink link : links ) {
            addLink( nffgName, link );
        }
    }


    protected void addLink(
            String   nffgName,
            RealLink link      )
                    throws NullPointerException {

        (this.dbLinks.get( nffgName )).put(link.getName(), link);
//        RealLink oldLink =
//                (this.dbLinks.get( nffgName )).putIfAbsent(link.getName(), link);
//
//        if ( oldLink != null )
//            throw new NullPointerException(
//                    "nfvSystem: duplicate Link within NFFG detected" );
    }

    protected void removeLink(
            String nffgName,
            String linkName ) {
        (this.dbLinks.get( nffgName )).remove( linkName );
    }



    /**
     * Gives access to a {@link RealNode} given the Node name.
     *
     * @param nodeName
     * @return         a {@link RealNode}, null if the node
     *                 doesn't exist or the NFV System is empty
     */
    protected RealNode getNode( String nodeName )
            throws NullPointerException {
        return this.dbNodes.get( nodeName );
    }

    /**
     * Gives access to all {@link RealNode}s requested by Node
     * names.
     *
     * @param  nodes a set with node names
     * @return       a set with {@link RealNode}s requested,
     *               the set may be empty
     */
    protected Set<RealNode> getNodes( Set<String> nodes )
            throws NullPointerException {

        if ( nodes == null )
            return new LinkedHashSet<RealNode>( this.dbNodes.values() );

        Set<RealNode> result = new LinkedHashSet<RealNode>();
        for ( RealNode node : this.dbNodes.values() ) {
            if ( nodes.contains( node.getName() ) ) {
                result.add( node );
            }
        }

        return result;
    }

    protected void addNode( RealNode node )
            throws NullPointerException {

        RealNode oldNode =
                this.dbNodes.put( node.getName(), node );

        if ( oldNode != null )
            throw new NullPointerException(
                    "nfvSystem: duplicate Node detected" );
    }

    protected void addNodes( Set<RealNode> nodes )
            throws NullPointerException {

        for ( RealNode node : nodes ) {
            addNode( node );
        }
    }

    protected void removeNode( String nodeName ) {
        this.dbNodes.remove( nodeName );
    }
}
