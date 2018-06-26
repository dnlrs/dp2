package it.polito.dp2.NFV.sol3.client2;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;


/**
 * An implementation of the {@link NfvReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvSystem implements NfvReader {

    private final NfvSystemDBMS db;

    public NfvSystem()
            throws NfvReaderException {

        this.db = new NfvSystemDBMS();

        try {

            NfvSystemLoader loader = new NfvSystemLoader();
            loader.loadFromNfvDeployer( this.db );

        } catch ( NfvReaderException e ) {
            throw new NfvReaderException( e.getMessage() );
        }
    }


    //////////////////////////////////////////////////////////////////////////
    // CONNECTIONS
    //////////////////////////////////////////////////////////////////////////

    @Override
    public ConnectionPerformanceReader
    getConnectionPerformance( HostReader srcHostI, HostReader dstHostI ) {

        ConnectionPerformanceReader result = null;
        try {
            result = this.db.getConnectionPerformance( srcHostI.getName()+"TO"+dstHostI.getName() );
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
            result = this.db.getHost( hostName );
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
            result = new HashSet<HostReader>( this.db.getHosts() );
        } catch ( Exception e ) {
            return new HashSet<HostReader>();
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
            result = this.db.getNFFG( nffgName );
        } catch ( Exception e ) {
            return null;
        }

        return result;
    }

    @Override
    public Set<NffgReader> getNffgs( Calendar date ) {

        Set<NffgReader> result = null;
        try {
            result = new HashSet<NffgReader>( this.db.getNFFGs( date ) );
        } catch ( Exception e ) {
            return new HashSet<NffgReader>();
        }

        return result;
    }

    //////////////////////////////////////////////////////////////////////////
    // VNFs
    //////////////////////////////////////////////////////////////////////////



    @Override
    public Set<VNFTypeReader> getVNFCatalog() {

        Set<VNFTypeReader> result = null;
        try {
            result = new HashSet<VNFTypeReader>( this.db.getVNFCatalog() );
        } catch ( Exception e ) {
            return new HashSet<VNFTypeReader>();
        }

        return result;
    }

    public VNFTypeReader getVNF( String vnfName ) {

        VNFTypeReader result = null;
        try {
            result = this.db.getVNF( vnfName );
        } catch ( Exception e ) {
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
            result = new HashSet<NodeReader>( this.db.getNodes( null ) );
        } catch ( Exception e ) {
            return new HashSet<NodeReader>();
        }

        return result;
    }

    public NodeReader getNode( String nodeName ) {

        NodeReader result = null;
        try {
            result = this.db.getNode( nodeName );
        } catch ( Exception e ) {
            return null;
        }

        return result;
    }
}
