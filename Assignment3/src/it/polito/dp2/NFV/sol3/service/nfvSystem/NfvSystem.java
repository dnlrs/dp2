package it.polito.dp2.NFV.sol3.service.nfvSystem;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.VNFTypeReader;


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

    private final NfvSystemDBMS db;


    protected NfvSystem()
            throws NfvReaderException {

        this.db = NfvSystemDBMS.getInstance();

        NfvSystemLoader loader = new NfvSystemLoader();
        try {

            loader.loadFromXMLFile( this.db );

        } catch ( NfvReaderException e ) {
//            e.printStackTrace();
            System.err.println( "An ERROR occurred while loading XML file." );
            if ( e.getMessage() != null ) {
                System.err.println( e.getMessage() );
            }
        } catch ( Exception e ) {
            e.printStackTrace( System.err ); // should never get here
            System.err.println(
                    "NfvSystem: unexpected exception." + "\n" +
                    "An unknown ERROR occurred while loading XML file." );
        }
    }


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
        } catch ( NullPointerException e ) {
            return new HashSet<HostReader>();
        } catch ( Exception e ) {
            System.err.println( "getHosts: unexpected exception" );
            return new HashSet<HostReader>();
        }
        return result;
    }

    @Override
    public NffgReader getNffg( String nffgName ) {

        NffgReader result = null;
        try {
            result = this.db.getNFFG( nffgName );
        } catch ( NullPointerException e ) {
            return null;
        } catch ( Exception e ) {
            System.err.println( "getNffg: unexpected exception" );
            return null;
        }

        return result;
    }

    @Override
    public Set<NffgReader> getNffgs( Calendar date ) {

        Set<NffgReader> result = null;
        try {
            result = new HashSet<NffgReader>( this.db.getNFFGs( date ) );
        } catch ( NullPointerException e ) {
            return new HashSet<NffgReader>();
        } catch ( Exception e ) {
            System.err.println( "getNffgs: unexpected exception" );
            return new HashSet<NffgReader>();
        }

        return result;
    }

    @Override
    public Set<VNFTypeReader> getVNFCatalog() {

        Set<VNFTypeReader> result = null;
        try {
            result = new HashSet<VNFTypeReader>( this.db.getVNFCatalog() );
        } catch ( NullPointerException e ) {
            return new HashSet<VNFTypeReader>();
        } catch ( Exception e ) {
            System.err.println( "getVNFCatalog: unexpected exception" );
            return new HashSet<VNFTypeReader>();
        }

        return result;
    }

}
