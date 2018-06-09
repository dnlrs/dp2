package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class NfvSystemDBMS {

    private final ConcurrentHashMap<String, RealHost>    dbHosts;
    private final ConcurrentHashMap<String, RealNffg>    dbNFFGs;
    private final ConcurrentHashMap<String, RealNode>    dbNodes;
    private final ConcurrentHashMap<String, RealVNFType> dbVNFs;

    private final ConcurrentHashMap<String, RealConnection> dbConns;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, RealLink>> dbLinks;

    private final Object lockDBHosts = new Object();
    private final Object lockDBNFFGs = new Object();
    private final Object lockDBNodes = new Object();
    private final Object lockDBConns = new Object();
    private final Object lockDBLinks = new Object();
    private final Object lockDBVNFs  = new Object();

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
    protected RealConnection getConnectionPerformance( String connectionID )
            throws NullPointerException {

        synchronized ( this.lockDBConns ) {
            return this.dbConns.get( connectionID );
        }
    }

    protected void addConnectionPerformance(
            String connectionID,
            RealConnection connection )
                    throws NullPointerException {

        synchronized ( this.lockDBConns ) {
            if ( this.dbConns.containsKey( connectionID ) )
                throw new NullPointerException(
                        "nfvSystem: duplicate connection detected" );

            this.dbConns.put( connectionID, connection );
        }
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

        synchronized ( this.lockDBHosts ) {
            return this.dbHosts.get( hostName );
        }
    }

    protected void addHost( RealHost host )
            throws NullPointerException {

        synchronized ( this.lockDBHosts ) {
            if ( this.dbHosts.containsKey( host.getName() ) )
                throw new NullPointerException(
                        "nfvSystem: duplicate host detected" );

            this.dbHosts.put( host.getName() , host );
        }
    }

    /**
     * Gives access to all {@link RealHost}s in the NFV System.
     *
     * @return the set of {@link RealHost}s in the NFV System,
     *         the set may be empty.
     */
    protected Set<RealHost> getHosts() {

        synchronized ( this.lockDBHosts ) {
            return new HashSet<RealHost>( this.dbHosts.values() );
        }
    }

    protected void addHosts( Set<RealHost> hosts )
            throws NullPointerException {

        synchronized ( this.lockDBHosts ) {
            for ( RealHost host : hosts ) {
                addHost( host );
            }
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

        synchronized ( this.lockDBNFFGs ) {
            return this.dbNFFGs.get( nffgName );
        }
    }


    protected void addNFFG( RealNffg nffg )
            throws NullPointerException {

        ConcurrentHashMap<String, RealLink> mapLinks =
                new ConcurrentHashMap<String, RealLink>();

        synchronized ( this.lockDBNFFGs ) {
            synchronized ( this.lockDBNodes ) {
                synchronized ( this.lockDBLinks ) {

                    if ( this.dbNFFGs.containsKey( nffg.getName() ) )
                        throw new NullPointerException(
                                "nfvSystem: duplicate NFFG detected" );

                    if ( this.dbLinks.containsKey( nffg.getName() ) )
                        throw new NullPointerException(
                                "nfvSystem: DB NFFG-links inconsistency problem" );

                    this.dbNFFGs.put( nffg.getName(), nffg);
                    this.dbLinks.put( nffg.getName(), mapLinks);

                    for ( RealNode node : nffg.getRealNodes() ) {
                        addNode( node );

                        for ( RealLink link : node.getRealLinks() ) {
                            addLink( nffg.getName(), link );
                        }
                    }
                }
            }
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

        synchronized ( this.lockDBNFFGs ) {

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
    }

    protected void addNFFGs( Set<RealNffg> nffgs )
            throws NullPointerException {

        synchronized ( this.lockDBNFFGs ) {
            synchronized ( this.lockDBNodes ) {
                synchronized ( this.lockDBLinks ) {

                    for ( RealNffg nffg : nffgs ) {
                        addNFFG( nffg );
                    }
                }
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

        synchronized ( this.lockDBVNFs ) {
            return new HashSet<RealVNFType>( this.dbVNFs.values() );
        }
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

        synchronized ( this.lockDBVNFs ) {
            return this.dbVNFs.get( vnfName );
        }
    }

    protected void addVNF( RealVNFType vnf )
            throws NullPointerException {

        synchronized ( this.lockDBVNFs ) {
            if ( this.dbVNFs.containsKey( vnf.getName() ) )
                throw new NullPointerException(
                        "nfvSystem: duplicate VNFType detected" );

            this.dbVNFs.put(vnf.getName(), vnf);
        }
    }

    protected void addCatalog( Set<RealVNFType> vnfs )
            throws NullPointerException {

        synchronized ( this.lockDBVNFs ) {
            for ( RealVNFType vnf : vnfs ) {
                addVNF( vnf );
            }
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
    protected Set<RealLink> getLinks(
            String      nffgName,
            Set<String> links     )
                    throws NullPointerException {

        Set<RealLink> result = new HashSet<RealLink>();

        synchronized ( this.lockDBLinks ) {
            ConcurrentHashMap<String, RealLink> hmLinks  = this.dbLinks.get( nffgName );

            for ( String linkName : links )
                if ( hmLinks.containsKey( linkName ) ) {
                    result.add( hmLinks.get( linkName ) );
                }

            return result;
        }
    }

    protected void addLink(
            String   nffgName,
            RealLink link      )
                    throws NullPointerException {

        synchronized ( this.lockDBLinks ) {
            if ( (this.dbLinks.get( nffgName )).containsKey( link.getName() ) )
                throw new NullPointerException(
                        "nfvSystem: duplicate Link within NFFG detected" );


            (this.dbLinks.get( nffgName )).put(link.getName(), link);
        }
    }

    protected void addLinks( String nffgName, Set<RealLink> links )
            throws NullPointerException {

        synchronized ( this.lockDBLinks ) {
            for ( RealLink link : links ) {
                addLink( nffgName, link );
            }
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

        synchronized ( this.lockDBNodes ) {
            return this.dbNodes.get( nodeName );
        }
    }

    protected void addNode( RealNode node )
            throws NullPointerException {

        synchronized ( this.lockDBNodes ) {
            if ( this.dbNodes.containsKey( node.getName() ) )
                throw new NullPointerException(
                        "nfvSystem: duplicate Node detected" );

            this.dbNodes.put( node.getName(), node );
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

        Set<RealNode> result = new HashSet<RealNode>();

        synchronized ( this.lockDBNodes ) {
            for ( String nodeName : nodes )
                if ( this.dbNodes.containsKey( nodeName ) ) {
                    result.add( getNode( nodeName ) );
                }

            return result;
        }
    }

    protected void addNodes( Set<RealNode> nodes )
            throws NullPointerException {

        synchronized ( this.lockDBNodes ) {
            for ( RealNode node : nodes ) {
                addNode( node );
            }
        }
    }

}
