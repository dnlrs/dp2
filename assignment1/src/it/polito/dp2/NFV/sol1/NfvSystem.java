package it.polito.dp2.NFV.sol1;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.VNFTypeReader;

public class NfvSystem implements NfvReader {

    private final NfvSystemDBMS db;


    protected NfvSystem()
            throws NfvReaderException {

        this.db = NfvSystemDBMS.getInstance();

        NfvSystemLoader loader = new NfvSystemLoader();
        try {
            loader.loadFromXMLFile( this.db );
        } catch ( NfvReaderException e ) {
            System.err.println();
            System.err.println( "An ERROR occurred while loading XML file." );
            if ( e.getMessage() != null ) {
                System.err.println( e.getMessage() );
            }
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
        }

        return result;
    }

}
