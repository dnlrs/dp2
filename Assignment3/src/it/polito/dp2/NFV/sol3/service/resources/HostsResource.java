package it.polito.dp2.NFV.sol3.service.resources;

import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvHost;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvHosts;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNodes;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;

@Path( "/hosts" )
public class HostsResource {

    private final static NfvSystem system = new NfvSystem();

    @Context
    UriInfo uriInfo;

    @DefaultValue("1") @QueryParam("detailed")
    private int detailed;

    @DefaultValue("0") @QueryParam("page")
    private int page;

    @DefaultValue("20") @QueryParam("itemsPerPage")
    private int itemsPerPage;

    public HostsResource() {}


    @GET
    @Produces(MediaType.APPLICATION_XML)
    public NfvHosts getHosts() {

        Set<HostReader> hosts = null;

        /* manage pagination */
        if ( this.page != 0 ) {
            hosts = Utils.getPage(
                            this.page,
                            this.itemsPerPage,
                            system.getHosts() );
        } else {
            hosts = system.getHosts();
        }

        if ( hosts == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        if ( hosts.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204

        NfvHosts      result   = new NfvHosts();
        List<NfvHost> liveList = result.getNfvHost();

        NfvHost host = null;
        for ( HostReader hostI : hosts ) {

            host = buildNfvHost( hostI, this.uriInfo, true );

            if ( host == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( host );
        }

        return result;
    }





    @GET
    @Path( "/{hostName: [a-zA-Z][a-zA-Z0-9]*}" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvHost getHost(
            @PathParam("hostName") String hostName ) {

        HostReader hostI = system.getHost( hostName );

        if ( hostI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        NfvHost result = buildNfvHost( hostI, this.uriInfo, true );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }




    @GET
    @Path( "/{hostName: [a-zA-Z][a-zA-Z0-9]*}/nodes" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvNodes getHostNodes(
            @PathParam("hostName") String hostName ) {

        HostReader hostI = system.getHost( hostName );

        if ( hostI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        if ( hostI.getNodes().isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204


        NfvNodes      result   = new NfvNodes();
        List<NfvNode> liveList = result.getNfvNode();

        NfvNode node = null;
        for ( NodeReader nodeI : hostI.getNodes() ) {

            node = NodesResource.buildNfvNode(
                                    nodeI,
                                    this.uriInfo,
                                    (this.detailed == 1 ? true : false) );

            if ( node == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( node );
        }

        return result;
    }


    protected static NfvHost buildNfvHost(
            HostReader hostI,
            UriInfo uriInfo,
            boolean detailed ) {

        NfvHost host = new NfvHost();

        host.setName( hostI.getName() );
        host.setMaxVNFs( new Integer( hostI.getMaxVNFs() ) );
        host.setInstalledMemory( new Integer( hostI.getAvailableMemory()) );
        host.setInstalledStorage( new Integer( hostI.getAvailableStorage() ) );

        List<String> hostedNodes = host.getHostedNodes();
        for ( NodeReader hostedNodeI : hostI.getNodes() ) {
            hostedNodes.add( hostedNodeI.getName() );
        }

        host.setSelf( Utils.getHostLink( uriInfo, hostI.getName() ) );
        host.setHostedNodesLink(
                Utils.getHostNodesLink( uriInfo, hostI.getName() ) );

        if ( (host.getSelf() == null) || (host.getHostedNodes() == null) )
            return null;

        return host;
    }



}
