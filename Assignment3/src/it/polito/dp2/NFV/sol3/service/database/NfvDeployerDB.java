package it.polito.dp2.NFV.sol3.service.database;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import it.polito.dp2.NFV.lab3.ServiceException;

public class NfvDeployerDB {

    private ConcurrentHashMap<String, HostReader> dbHosts;
    private ConcurrentHashMap<String, NffgReader> dbNFFGs;
    private ConcurrentHashMap<String, NodeReader> dbNodes;
    private ConcurrentHashMap<String, VNFTypeReader> dbVNFs;

    private ConcurrentHashMap<String, ConnectionPerformanceReader> dbConns;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, LinkReader>> dbLinks;

    private static NfvDeployerDB instance = null;


    private NfvDeployerDB() {
        dbHosts = new ConcurrentHashMap<String, HostReader>();
        dbNFFGs = new ConcurrentHashMap<String, NffgReader>();
        dbNodes = new ConcurrentHashMap<String, NodeReader>();
        dbVNFs  = new ConcurrentHashMap<String, VNFTypeReader>();
        dbConns = new ConcurrentHashMap<String, ConnectionPerformanceReader>();
        dbLinks = new ConcurrentHashMap<String, ConcurrentHashMap<String, LinkReader>>();
    }

    public synchronized static NfvDeployerDB getInstance() {

        if ( instance == null ) {
            instance = new NfvDeployerDB();

            try {
                instance.initDB();
            } catch ( ServiceException e ) {
                instance = new NfvDeployerDB();
            }
        }

        return instance;
    }

    /* ----------------------------------------------------------------------
     *  DATA ACCESS
     ----------------------------------------------------------------------*/
    /**
     * Gives access to a {@link ConnectionPerformanceReader} interface given
     * its ID "sourceHostName-dstHostName"
     *
     * @param  cID the connection ID
     * @return     a {@link ConnectionPerformanceReader} interface, null if
     *             connection doesn't exists or the NFVSystem is empty
     */
    public synchronized
    ConnectionPerformanceReader getConnectionPerformance( String cID ) {
        if ( cID == null )
            return null;

        return dbConns.get( cID );
    }

    /**
     * Gives access to a {@link HostReader} interface given the Host name.
     *
     * @param hostName the host name
     * @return         a {@link HostReader}, null if Host doesn't exist or
     *                 the NFV System is empty
     */
    public synchronized
    HostReader getHost( String hostName ) {
        if ( hostName == null )
            return null;

        return dbHosts.get( hostName );
    }

    /**
     * Gives access to all {@link HostReader} interfaces in the NFV System.
     *
     * @return the set of {@link HostReader} interfaces in the NFV System,
     *         the set may be empty.
     */
    public synchronized
    Set<HostReader> getHosts() {
        return new HashSet<HostReader>( dbHosts.values() );
    }

    /**
     * Gives access to a {@link NffgReader} interface given the NFFG name.
     *
     * @param nffgName the NFFG name
     * @return         a {@link NffgReader} interface, null if the NFFG
     *                 doesn't exist of the NFV System is empty
     */
    public synchronized
    NffgReader getNFFG( String nffgName ) {
        if ( nffgName == null )
            return null;

        return  dbNFFGs.get( nffgName );
    }

    /**
     * Gives access to all {@link NffgReader} interfaces starting from
     * {@code date}.
     *
     * @param date
     * @return     a set of {@link NffgReader} interfaces, the set may
     *             be empty
     */
    public synchronized
    Set<NffgReader> getNFFGs( Calendar date ) {
        if ( date == null )
            return new HashSet<NffgReader>( dbNFFGs.values() );

        Set<NffgReader> setNFFGs = new HashSet<NffgReader>();
        for ( String key : dbNFFGs.keySet() ) {
            NffgReader nffg = dbNFFGs.get(key);

            if ( nffg.getDeployTime().compareTo(date) >= 0 ) {
                setNFFGs.add(nffg);
            }
        }

        return setNFFGs;
    }

    /**
     * Gives access to the VNF Types catalogue.
     *
     * @return a set of {@link VNFTypeReader} interfaces,
     *         the set may be empty if the NFV System is empty
     */
    public synchronized
    Set<VNFTypeReader> getVNFCatalog() {
        return new HashSet<VNFTypeReader>( dbVNFs.values() );
    }

    /**
     * Gives access to a {@link VNFTypeReader} interface given the name
     * of the VNF.
     *
     * @param vnfName
     * @return        a {@link VNFTypeReader} interface, null if if
     *                doesn't exist or the NFV System is empty
     */
    public synchronized
    VNFTypeReader getVNF( String vnfName ) {
        if ( vnfName == null )
            return null;

        return dbVNFs.get( vnfName );
    }

    /**
     * Gives access to all the {@link LinkReader} interfaces requested
     * by name from an NFFG.
     *
     * @param nffgName the NFFG name
     * @param links    the names of the
     * @return         a set of {@link LinkReader} interfaces,
     *                 the set may be empty
     */
    public synchronized
    Set<LinkReader> getLinks( String nffgName, Set<String> links ) {
        if ( ( nffgName == null ) || ( links == null ) )
            return new HashSet<LinkReader>();

        Set<LinkReader>             setLinks = new HashSet<LinkReader>();
        ConcurrentHashMap<String, LinkReader> hmLinks  = dbLinks.get( nffgName );

        for ( String linkName : links )
            if ( hmLinks.containsKey( linkName ) ) {
                setLinks.add( hmLinks.get( linkName ) );
            }

        return setLinks;
    }

    /**
     * Gives access to a {@link NodeReader} interface given the Node name.
     *
     * @param nodeName
     * @return         a {@link NodeReader} interface, null if the node
     *                 doesn't exist or the NFV System is empty
     */
    public synchronized
    NodeReader getNode( String nodeName ) {
        if ( nodeName == null )
            return null;

        return dbNodes.get(nodeName);
    }

    /**
     * Gives access to all {@link NodeReader} interfaces requested by Node
     * names.
     *
     * @param  nodes a set with node names
     * @return       a set with {@link NodeReader} interfaces requested,
     *               the set may be empty
     */
    public synchronized
    Set<NodeReader> getNodes( Set<String> nodes ) {
        if ( nodes == null )
            return new HashSet<NodeReader>();

        Set<NodeReader> setNodes = new HashSet<NodeReader>();

        for ( String nodeName : nodes )
            if ( dbNodes.containsKey( nodeName ) ) {
                setNodes.add( dbNodes.get( nodeName ) );
            }

        return setNodes;
    }


    private synchronized void initDB()
            throws ServiceException {

        NfvReader monitor = null;
        try {
            NfvReaderFactory factory = NfvReaderFactory.newInstance();
            monitor = factory.newNfvReader();
        } catch( FactoryConfigurationError | NfvReaderException e ) {
            throw new ServiceException();
        }

        // hosts
        for ( HostReader hostI : monitor.getHosts() ) {
            dbHosts.put(hostI.getName(), hostI);
        }

        // connections
        for ( HostReader srcHostI : monitor.getHosts() ) {
            for ( HostReader dstHostI : monitor.getHosts() )
                if ( monitor.getConnectionPerformance(srcHostI, dstHostI) != null ) {
                    dbConns.put(
                            new String( srcHostI.getName() + "-" + dstHostI.getName() ),
                            monitor.getConnectionPerformance(srcHostI, dstHostI)
                            );
                }
        }

        // VNFs
        for ( VNFTypeReader vnfI : monitor.getVNFCatalog() ) {
            dbVNFs.put(vnfI.getName(), vnfI);
        }

        // NFFGs
        for ( NffgReader nffgI : monitor.getNffgs(null) ) {

            ConcurrentHashMap<String, LinkReader> mapLinks =
                    new ConcurrentHashMap<String, LinkReader>();

            for ( NodeReader nodeI : nffgI.getNodes() ) {
                dbNodes.put(nodeI.getName(), nodeI);

                for ( LinkReader linkI : nodeI.getLinks() ) {
                    mapLinks.put(linkI.getName(), linkI);
                }
            }

            dbNFFGs.put(nffgI.getName(), nffgI);
        }
    }



}
