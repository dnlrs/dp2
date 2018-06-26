package it.polito.dp2.NFV.sol3.service.resources;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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

import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvArcs;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;


@Path( "/nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/links" )
public class LinksResource {
    private final static Logger    logger = Logger.getLogger( LinksResource.class.getName() );
    private final static NfvSystem system = new NfvSystem();

    @Context
    UriInfo uriInfo;

    @DefaultValue("0") @QueryParam("page")
    private int page;

    @DefaultValue("20") @QueryParam("itemsPerPage")
    private int itemsPerPage;

    @DefaultValue("1") @QueryParam("update")
    private int update;

    public LinksResource() {}


    @GET
    @Produces(MediaType.APPLICATION_XML)
    public NfvArcs getLinks (
            @PathParam( "nffgName" ) String nffgName ) {

        NffgReader nffgI = system.getNffg( nffgName );

        if ( nffgI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        Set<LinkReader> links = null;
        if ( this.page != 0 ) {
            links = Utils.getPage( this.page, this.itemsPerPage, system.getLinks( nffgName ) );
        } else {
            links = system.getLinks( nffgName );
        }

        if ( links.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204

        NfvArcs      result   = new NfvArcs();
        List<NfvArc> liveList = result.getNfvArc();

        NfvArc link = null;
        for ( LinkReader linkI : links ) {

            link = buildNfvLink( linkI, this.uriInfo, true );

            if ( link == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( link );
        }

        return result;
    }





    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path( "{linkName: [a-zA-Z][a-zA-Z0-9]*}" )
    public NfvArc getLink(
            @PathParam( "nffgName" ) String nffgName,
            @PathParam( "linkName" ) String linkName ) {

        LinkReader linkI = system.getLink( nffgName, linkName );

        if ( linkI == null )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 404

        NfvArc result = buildNfvLink( linkI, this.uriInfo, true );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }


    @POST
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_XML )
    public NfvArc createLink(
            @PathParam( "nffgName" ) String nffgName,
            NfvArc link ) {

        if ( !isValid( link ) )
            throw new WebApplicationException(
                    Response.Status.BAD_REQUEST ); // 400

        NodeReader srcNode = system.getNode( link.getSrc() );
        NodeReader dstNode = system.getNode( link.getDst() );

        if ( (srcNode == null) || (dstNode == null) )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        if ( srcNode.getNffg().getName().compareTo(
                dstNode.getNffg().getName() ) != 0 )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        String linkName = link.getName();
        for ( LinkReader linkI : srcNode.getLinks() ) {
            if ( linkI.getDestinationNode().getName().compareTo( dstNode.getName()) == 0 ) {
                if ( this.update != 1 )
                    throw new WebApplicationException(
                            Response.Status.NOT_ACCEPTABLE ); // 406
                else {
                    linkName = linkI.getName();
                    system.deleteLink( nffgName, linkI.getName() );
                }
            }
        }

        try {

            system.addLink(
                    linkName,
                    link.getSrc(),
                    link.getDst(),
                    link.getThroughput(),
                    link.getLatency() );

        } catch ( ServiceException e ) {

            system.deleteLink( nffgName, linkName );
            throw new WebApplicationException(
                    Response.Status.FORBIDDEN ); // 403
        }

        LinkReader linkI =  system.getLink( nffgName, linkName );
        NfvArc result = buildNfvLink( linkI, this.uriInfo, true );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }

    @DELETE
    @Path( "{linkName: [a-zA-Z][a-zA-Z0-9]*}" )
    public Response deleteLink(
            @PathParam( "nffgName" ) String nffgName,
            @PathParam( "linkName" ) String linkName ) {

        if ( system.getLink( nffgName, linkName ) == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        system.deleteLink( nffgName, linkName );

        return Response.noContent().build();

    }


    protected static NfvArc buildNfvLink(
            LinkReader linkI,
            UriInfo uriInfo,
            boolean detailed ) {

        NfvArc link = new NfvArc();

        link.setName( linkI.getName() );
        link.setSrc( linkI.getSourceNode().getName() );
        link.setDst( linkI.getDestinationNode().getName() );
        link.setThroughput( new Float( linkI.getThroughput() ) );
        link.setLatency( new Integer( linkI.getLatency() ) );

        link.setSelf(
                Utils.getLinkLink(
                        uriInfo,
                        linkI.getSourceNode().getNffg().getName(),
                        linkI.getName() ) );

        link.setSrcLink(
                Utils.getNodeLink( uriInfo, linkI.getSourceNode().getName() ) );
        link.setDstLink(
                Utils.getNodeLink( uriInfo, linkI.getDestinationNode().getName() ) );

        if ( (link.getSelf() == null)
                || (link.getSrcLink() == null)
                || (link.getDstLink() == null) )
            return null;


        return link;
    }

    protected static boolean isValid ( NfvArc link ) {

        if ( (link.getName() == null)
                || (link.getSrc() == null)
                || (link.getDst() == null) )
            return false;

        if ( (link.getThroughput() < 0F) || (link.getLatency() < 0) )
            return false;

        return true;
    }

}
