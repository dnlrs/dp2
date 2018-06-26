package it.polito.dp2.NFV.sol3.service.resources;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab3.NoNodeException;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvArcs;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvHost;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvHosts;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvNodes;
import it.polito.dp2.NFV.sol3.service.UnknownNameException;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;
import it.polito.dp2.NFV.sol3.service.reachability.NoGraphException;
import it.polito.dp2.NFV.sol3.service.reachability.ReachabilityTester;

@Path( "/nodes" )
public class NodesResource {

    private final static NfvSystem system = new NfvSystem();

    @Context
    UriInfo uriInfo;

    @DefaultValue("1") @QueryParam("detailed")
    private int detailed;

    @DefaultValue("0") @QueryParam("page")
    private int page;

    @DefaultValue("20") @QueryParam("itemsPerPage")
    private int itemsPerPage;

    public NodesResource() {}

    @GET
    @Produces( MediaType.APPLICATION_XML )
    public NfvNodes getNodes() {

        Set<NodeReader> nodes = null;

        /* manage pagination */
        if ( this.page != 0 ) {
            nodes = Utils.getPage(
                            this.page,
                            this.itemsPerPage,
                            system.getNodes() );
        } else {
            nodes = system.getNodes();
        }

        if ( nodes == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        if ( nodes.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204

        NfvNodes      result   = new NfvNodes();
        List<NfvNode> liveList = result.getNfvNode();

        NfvNode node = null;
        for ( NodeReader nodeI : nodes ) {

            node = buildNfvNode(
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



    @GET
    @Path( "/{nodeName: [a-zA-Z][a-zA-Z0-9]*}" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvNode getNode(
            @PathParam("nodeName") String nodeName) {

        NodeReader nodeI = system.getNode( nodeName );

        if ( nodeI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        NfvNode result = buildNfvNode(
                            nodeI,
                            this.uriInfo,
                            (this.detailed == 1 ? true : false) );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }




    @GET
    @Path( "/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/links" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvArcs getNodeLinks(
            @PathParam("nodeName") String nodeName ) {

        NodeReader nodeI = system.getNode( nodeName );

        if ( nodeI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404


        Set<LinkReader> links = nodeI.getLinks();

        if ( links.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204

        NfvArcs      result   = new NfvArcs();
        List<NfvArc> liveList = result.getNfvArc();

        NfvArc link = null;
        for ( LinkReader linkI : links ) {

            link = LinksResource.buildNfvLink( linkI, this.uriInfo, true );

            if ( link == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( link );
        }

        return result;
    }


    @GET
    @Path( "/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/reachableHosts" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvHosts getNodeReachableHosts(
            @PathParam("nodeName") String nodeName ) {

        Set<HostReader> reachableHosts = null;
        try {

            ReachabilityTester tester = new ReachabilityTester();
            reachableHosts = tester.getReachableHosts( nodeName );

        } catch( UnknownNameException
                 | NoNodeException
                 | NoGraphException e ) {
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404
        } catch ( ServiceException e ) {
            throw new WebApplicationException(
                    Response.Status.BAD_GATEWAY ); // 502
        } catch ( Exception e ) {
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500
        }


        NfvHosts      result   = new NfvHosts();
        List<NfvHost> liveList = result.getNfvHost();

        NfvHost host = null;
        for ( HostReader hostI : reachableHosts ) {

            host = HostsResource.buildNfvHost( hostI, this.uriInfo, true );

            if ( host == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( host );
        }

        return result;
    }

    @POST
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_XML )
    public NfvNode postNode( NfvNode node ) {

        if ( !isValid( node ) )
            throw new WebApplicationException(
                    Response.Status.BAD_REQUEST ); // 400

        try {

            system.addNode(
                    node.getName(),
                    node.getHostingHost(),
                    node.getFunctionalType(),
                    node.getAssociatedNFFG() );

        } catch ( ServiceException e ) {
            try {
                system.deleteNode( node.getName() );
            } catch ( ServiceException wontHappen ) {}

            throw new WebApplicationException(
                    Response.Status.FORBIDDEN ); // 403
        }

        NodeReader nodeI = system.getNode( node.getName() );

        NfvNode result = buildNfvNode( nodeI, this.uriInfo, false );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }


    @DELETE
    @Path( "/{nodeName: [a-zA-Z][a-zA-Z0-9]*}" )
    public Response deleteNode(
            @PathParam("nodeName") String nodeName ) {

        if ( system.getNode( nodeName ) == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        try {
            system.deleteNode( nodeName );
        } catch ( ServiceException e ) {
            throw new WebApplicationException(
                    Response.Status.FORBIDDEN ); // 403
        }

        return Response.noContent().build();
    }



    protected static NfvNode buildNfvNode(
            NodeReader nodeI,
            UriInfo uriInfo,
            boolean detailed ) {

        NfvNode node = new NfvNode();
        node.setName( nodeI.getName() );
        node.setFunctionalType( nodeI.getFuncType().getName() );
        node.setHostingHost( nodeI.getHost().getName() );
        node.setAssociatedNFFG( nodeI.getNffg().getName() );

        if ( detailed ) {
            List<NfvArc> links = node.getNfvArc();
            for ( LinkReader linkI : nodeI.getLinks() ) {
                links.add( LinksResource.buildNfvLink( linkI, uriInfo, true ) );
            }
        }

        node.setSelf(
                Utils.getNodeLink( uriInfo, nodeI.getName() ) );
        node.setFunctionalTypeLink(
                Utils.getVNFLink( uriInfo, nodeI.getFuncType().getName() ) );
        node.setHostingHostLink(
                Utils.getHostLink( uriInfo, nodeI.getHost().getName() ) );
        node.setAssociatedNFFGLink(
                Utils.getNFFGLink( uriInfo, nodeI.getNffg().getName() ) );
        node.setLinksLink(
                Utils.getNodeLinksLink( uriInfo, nodeI.getName() ) );
        node.setReachableHostsLink(
                Utils.getNodeReachableHostsLink( uriInfo, nodeI.getName() ) );

        if ( (node.getName() == null)
                || (node.getSelf() == null)
                || (node.getFunctionalTypeLink() == null)
                || (node.getHostingHostLink() == null)
                || (node.getAssociatedNFFGLink() == null)
                || (node.getLinksLink() == null)
                || (node.getReachableHostsLink() == null) )
            return null;

        return node;
    }

    protected static boolean isValid( NfvNode node ) {

        if ( (node.getName() == null)
                || (node.getFunctionalType() == null)
                || (node.getAssociatedNFFG() == null) )
            return false;

        for ( NfvArc link : node.getNfvArc() ) {
            if ( !(LinksResource.isValid( link )) )
                return false;
        }

        return true;
    }

}
