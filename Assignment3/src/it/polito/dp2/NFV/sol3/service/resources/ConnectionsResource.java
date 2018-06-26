package it.polito.dp2.NFV.sol3.service.resources;

import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvArcs;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;

@Path( "/connections" )
public class ConnectionsResource {

    private final static NfvSystem system = new NfvSystem();

    @Context
    UriInfo uriInfo;

    @DefaultValue("0") @QueryParam("page")
    private int page;

    @DefaultValue("20") @QueryParam("itemsPerPage")
    private int itemsPerPage;

    @QueryParam( "sourceHost" )
    String srcHost;
    @QueryParam( "destinationHost" )
    String dstHost;

    public ConnectionsResource() {}

    @GET
    @Produces( MediaType.APPLICATION_XML )
    public NfvArcs getConnections() {

        Set<HostReader> hosts = system.getHosts();

        NfvArcs      result   = new NfvArcs();
        List<NfvArc> liveList = result.getNfvArc();

        /*
         * All connections
         */
        if ( (this.srcHost == null) && (this.dstHost == null) ) {
            for ( HostReader srcHostI : hosts ) {
                for ( HostReader dstHostI : hosts ) {

                    if ( system.getConnectionPerformance( srcHostI, dstHostI ) == null ) {
                        continue;
                    }

                    liveList.add( buildNfvConnection(
                            system.getConnectionPerformance( srcHostI, dstHostI ),
                            srcHostI,
                            dstHostI,
                            this.uriInfo,
                            true ) );
                }
            }
        }

        /*
         * All connections starting from srcHost
         */
        if ( (this.srcHost != null) && (this.dstHost == null) ) {
            HostReader srcHostI = system.getHost( this.srcHost );

            if ( srcHostI == null )
                throw new WebApplicationException(
                        Response.Status.NOT_FOUND ); // 404

            for ( HostReader dstHostI : hosts ) {

                if ( system.getConnectionPerformance( srcHostI, dstHostI ) == null ) {
                    continue;
                }

                liveList.add( buildNfvConnection(
                        system.getConnectionPerformance( srcHostI, dstHostI ),
                        srcHostI,
                        dstHostI,
                        this.uriInfo,
                        true ) );
            }
        }

        /*
         * All connections arriving to dstHost
         */
        if ( (this.srcHost == null) && (this.dstHost != null) ) {
            HostReader dstHostI = system.getHost( this.dstHost );

            if ( dstHostI == null )
                throw new WebApplicationException(
                        Response.Status.NOT_FOUND ); // 404

            for ( HostReader srcHostI : hosts ) {

                if ( system.getConnectionPerformance( srcHostI, dstHostI ) == null ) {
                    continue;
                }

                liveList.add( buildNfvConnection(
                        system.getConnectionPerformance( srcHostI, dstHostI ),
                        srcHostI,
                        dstHostI,
                        this.uriInfo,
                        true ) );
            }

        }

        /*
         * The connection between srcHost and dstHost
         */
        if ( (this.srcHost != null) && (this.dstHost != null) ) {
            HostReader srcHostI = system.getHost( this.srcHost );
            HostReader dstHostI = system.getHost( this.dstHost );

            if ( (srcHostI == null) || (dstHostI == null) )
                throw new WebApplicationException(
                        Response.Status.NOT_FOUND ); // 404

            if ( system.getConnectionPerformance( srcHostI, dstHostI ) != null ) {
                liveList.add( buildNfvConnection(
                        system.getConnectionPerformance( srcHostI, dstHostI ),
                        srcHostI,
                        dstHostI,
                        this.uriInfo,
                        true ) );
            }
        }

        if ( liveList.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204

        return result;
    }




    protected static NfvArc buildNfvConnection(
            ConnectionPerformanceReader connectionI,
            HostReader srcHost,
            HostReader dstHost,
            UriInfo uriInfo,
            boolean detailed ) {

        String connectionName =
                new String( srcHost.getName() + "TO" + dstHost.getName() );

        NfvArc connection = new NfvArc();
        connection.setName( connectionName );
        connection.setSrc( srcHost.getName() );
        connection.setDst( dstHost.getName() );
        connection.setThroughput( new Float( connectionI.getThroughput() ) );
        connection.setLatency( new Integer( connectionI.getLatency() ) );

        connection.setSelf(
                Utils.getConnectionLink(
                        uriInfo,
                        srcHost.getName(),
                        dstHost.getName() ) );

        connection.setSrcLink( Utils.getHostLink( uriInfo, srcHost.getName() ) );
        connection.setDstLink( Utils.getHostLink( uriInfo, dstHost.getName() ) );

        if ( (connection.getSelf() == null)
                || (connection.getSrcLink() == null)
                || (connection.getDstLink() == null) )
            return null;

        return connection;
    }
}
