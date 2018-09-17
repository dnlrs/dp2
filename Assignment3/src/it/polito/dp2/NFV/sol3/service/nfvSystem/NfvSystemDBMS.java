package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

import it.polito.dp2.NFV.sol3.service.AlreadyLoadedException;


public class NfvSystemDBMS {

    private final static Logger logger = Logger.getLogger( NfvSystemDBMS.class.getName() );

    private final ConcurrentSkipListMap<String, RealHost>    dbHosts;
    private final ConcurrentSkipListMap<String, RealNffg>    dbNFFGs;
    private final ConcurrentSkipListMap<String, RealNode>    dbNodes;
    private final ConcurrentSkipListMap<String, RealVNFType> dbVNFs;

    private final ConcurrentSkipListMap<String, RealConnection> dbConns;
    private final ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, RealLink>> dbLinks;

    /**
     * Since, NFFG, Nodes and links are highly inter-dependent, we need to make sure that
     * when adding an NFFG with all its nodes and links the system database remains in a
     * consistent state while updating collections.
     * Strictly related collections are:
     *  - dbNFFGs
     *  - dbNodes
     *  - dbLinks
     */
    private final Object lock = new Object();



    private final static NfvSystemDBMS instance;

    static {
        instance = new NfvSystemDBMS();
    }

    private NfvSystemDBMS() {
        logger.info( "DB initialization started.." );
        this.dbHosts = new ConcurrentSkipListMap<String, RealHost>();
        this.dbNFFGs = new ConcurrentSkipListMap<String, RealNffg>();
        this.dbNodes = new ConcurrentSkipListMap<String, RealNode>();
        this.dbVNFs  = new ConcurrentSkipListMap<String, RealVNFType>();
        this.dbConns = new ConcurrentSkipListMap<String, RealConnection>();
        this.dbLinks = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, RealLink>>();
        logger.info( "DB initilized." );
    }


    public static NfvSystemDBMS getInstance() {
        return instance;
    }

    // TODO: change nullpointer exceptions to alreadyLoadedException


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
                    throws NullPointerException, AlreadyLoadedException {

        if ( this.dbConns.putIfAbsent( connectionID, connection ) != null )
            throw new AlreadyLoadedException(
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

        return ( RealNamedEntity.nameIsValid( hostName ) ?
                        this.dbHosts.get( hostName ) : null );
    }


    /**
     * Adds a {@link RealHost} to the database if not already present.
     *
     * @param host
     * @throws NullPointerException
     */
    protected void addHost( RealHost host )
            throws NullPointerException, AlreadyLoadedException {

        if ( this.dbHosts.putIfAbsent( host.getName(), host ) != null )
            throw new AlreadyLoadedException(
                    "nfvSystem: duplicate host detected " + host.getName() );
    }

    /**
     * Gives access to all {@link RealHost}s in the NFV System.
     *
     * @return the set of {@link RealHost}s in the NFV System,
     *         the set may be empty.
     */
    protected Set<RealHost> getHosts() {
        Set<RealHost> result = new LinkedHashSet<RealHost>(this.dbHosts.values());
        return Collections.unmodifiableSet( result );
    }

    /**
     * Adds a set of {@link RealHost}s.
     *
     * @param hosts
     * @throws NullPointerException
     */
    protected void addHosts( Set<RealHost> hosts )
            throws NullPointerException, AlreadyLoadedException {

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

        synchronized ( this.lock ) {
            return ( RealNamedEntity.nameIsValid( nffgName ) ?
                            this.dbNFFGs.get( nffgName ) : null);
        }
    }


    /**
     * Adds a {@link RealNffg} with nodes and links to the NfvSystem database.
     *
     * @param nffg
     * @throws NullPointerException
     */
    protected void addNffg( RealNffg nffg )
            throws NullPointerException, AlreadyLoadedException {

        ConcurrentSkipListMap<String, RealLink> mapLinks =
                new ConcurrentSkipListMap<String, RealLink>();

        synchronized ( this.lock ) {

            if ( this.dbNFFGs.putIfAbsent( nffg.getName(), nffg) != null )
                throw new AlreadyLoadedException(
                        "nfvSystem: duplicate NFFG detected" );

            if ( this.dbLinks.putIfAbsent( nffg.getName(), mapLinks) != null )
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

    /**
     * Removes a {@link RealNffg} from the NfvSystem database.
     *
     * @param nffgName
     */
    protected void removeNffg( String nffgName ) {
        synchronized ( this.lock ) {
            this.dbLinks.remove( nffgName );
            this.dbNFFGs.remove( nffgName );
        }
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

        Set<RealNffg> result = new LinkedHashSet<RealNffg>();

        synchronized ( this.lock ) {

            if ( date == null ) {
                result.addAll( this.dbNFFGs.values() );
            } else {
                for ( RealNffg nffg : this.dbNFFGs.values() ) {
                    if ( nffg.getDeployTime().compareTo( date ) >= 0 ) {
                        result.add( nffg );
                    }
                }
            }
            return Collections.unmodifiableSet( result );
        }
    }


    /**
     * adds a set of {@link RealNffg} with associated nodes and links to the
     * NfvSystem database.
     *
     * @param nffgs
     * @throws NullPointerException
     */
    protected void addNFFGs( Set<RealNffg> nffgs )
            throws NullPointerException, AlreadyLoadedException {
        synchronized ( this.lock ) {
            for ( RealNffg nffg : nffgs ) {
                addNffg( nffg );
            }
        }
    }

    /**
     * Gives access to the VNF Types catalogue.
     *
     * @return a set of {@link RealVNFType}s,
     *         the set may be empty if the NFV System is empty
     */
    protected Set<RealVNFType> getVNFCatalog() {
        Set<RealVNFType> result = new LinkedHashSet<RealVNFType>( this.dbVNFs.values() );
        return Collections.unmodifiableSet( result );
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
            return ( RealNamedEntity.nameIsValid( vnfName ) ?
                                    this.dbVNFs.get( vnfName ) : null);
    }


    protected void addVNF( RealVNFType vnf )
            throws NullPointerException, AlreadyLoadedException {

        if ( this.dbVNFs.putIfAbsent( vnf.getName(), vnf ) != null )
            throw new AlreadyLoadedException(
                    "nfvSystem: duplicate VNFType detected" );
    }


    protected void addCatalog( Set<RealVNFType> vnfs )
            throws NullPointerException, AlreadyLoadedException {

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

        Set<RealLink> result = new LinkedHashSet<RealLink>();

        synchronized ( this.lock ) {
            ConcurrentSkipListMap<String, RealLink> hmLinks =
                    this.dbLinks.get( nffgName );

            if ( links == null ) {
                result.addAll( hmLinks.values() );
            } else {
                for ( RealLink link : hmLinks.values() ) {
                    if ( links.contains( link.getName() ) ) {
                        result.add( link );
                    }
                }
            }

            return Collections.unmodifiableSet( result );
        }
    }


    protected RealLink getLink( String nffgName, String linkName )
                    throws NullPointerException {

        if ( !RealNamedEntity.nameIsValid( nffgName ) ||
                !RealNamedEntity.nameIsValid( linkName ) )
            return null;

        synchronized ( this.lock ) {
            return (this.dbLinks.get( nffgName )).get( linkName );
        }
    }


    protected void addLinks( String nffgName, Set<RealLink> links )
            throws NullPointerException, AlreadyLoadedException {

        synchronized ( this.lock ) {
            for ( RealLink link : links ) {
                addLink( nffgName, link );
            }
        }
    }


    protected void addLink(
            String   nffgName,
            RealLink link      )
                    throws NullPointerException, AlreadyLoadedException {

        synchronized ( this.lock ) {

        if ( (this.dbLinks.get( nffgName )).putIfAbsent(link.getName(), link) != null )
            throw new AlreadyLoadedException(
                    "nfvSystem: duplicate Link within NFFG detected" );
        }
    }

//    protected void addLinkOrReplace(
//            String nffgName,
//            RealLink link           )
//                    throws NullPointerException {
//
//        synchronized ( this.lock ) {
//            (this.dbLinks.get( nffgName )).put(link.getName(), link);
//        }
//
//    }

    protected void removeLink(
            String nffgName,
            String linkName ) {
        synchronized ( this.lock ) {
            (this.dbLinks.get( nffgName )).remove( linkName );
        }
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
        synchronized ( this.lock ) {
            return this.dbNodes.get( nodeName );
        }
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

        Set<RealNode> result = new LinkedHashSet<RealNode>();

        synchronized ( this.lock ) {

            if ( nodes == null ) {
                result.addAll( this.dbNodes.values() );
            } else {
                for ( RealNode node : this.dbNodes.values() ) {
                    if ( nodes.contains( node.getName() ) ) {
                        result.add( node );
                    }
                }
            }

            return Collections.unmodifiableSet( result );
        }
    }

    protected void addNode( RealNode node )
            throws NullPointerException, AlreadyLoadedException {

        synchronized ( this.lock ) {

            if ( this.dbNodes.putIfAbsent( node.getName(), node ) != null )
                throw new AlreadyLoadedException(
                        "nfvSystem: duplicate Node detected" );
        }
    }

    protected void addNodes( Set<RealNode> nodes )
            throws NullPointerException, AlreadyLoadedException {

        synchronized ( this.lock ) {
            for ( RealNode node : nodes ) {
                addNode( node );
            }
        }
    }

    protected void removeNode( String nodeName ) {
        synchronized ( this.lock ) {
            this.dbNodes.remove( nodeName );
        }
    }
}
