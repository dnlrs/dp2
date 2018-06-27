package it.polito.dp2.NFV.sol3.service.resources;

import java.util.GregorianCalendar;
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
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.sol3.service.ServiceException;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvArc;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvNFFG;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvNFFGs;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvNode;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvNodes;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;

@Path( "/nffgs" )
public class NFFGsResource {
    private final static Logger    logger = Logger.getLogger( NFFGsResource.class.getName() );
    private final static NfvSystem system = new NfvSystem();


    @Context
    UriInfo uriInfo;

    @DefaultValue("0") @QueryParam("page")
    private int page;
    @DefaultValue("20") @QueryParam("itemsPerPage")
    private int itemsPerPage;

    @QueryParam("date")
    XMLGregorianCalendar date;

    @DefaultValue("1") @QueryParam("detailed")
    private int detailed;

    public NFFGsResource() {}

    @GET
    @Produces( MediaType.APPLICATION_XML )
    public NfvNFFGs getNFFGs() {

        Set<NffgReader> nffgs;
        if ( this.page != 0 ) {

            nffgs = Utils.getPage(
                            this.page,
                            this.itemsPerPage,
                            system.getNffgs(
                                    (this.date == null ?
                                            null : this.date.toGregorianCalendar() ) )
                            );
        } else {
            nffgs = system.getNffgs( (this.date == null ?
                                            null : this.date.toGregorianCalendar() ) );
        }

        if ( nffgs == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        if ( nffgs.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204


        NfvNFFGs      result   = new NfvNFFGs();
        List<NfvNFFG> liveList = result.getNfvNFFG();

        NfvNFFG nffg = null;
        for ( NffgReader nffgI : nffgs ) {

            nffg = buildNfvNFFG(
                    nffgI,
                    this.uriInfo,
                    (this.detailed == 1 ? true : false) );

            if ( nffg == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( nffg );
        }

        return result;
    }



    @GET
    @Path( "/{nffgName: [a-zA-Z][a-zA-Z0-9]*}" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvNFFG getNFFG(
            @PathParam( "nffgName" ) String nffgName ) {

        NffgReader nffgI = system.getNffg( nffgName );

        if ( nffgI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        NfvNFFG result = buildNfvNFFG(
                            nffgI,
                            this.uriInfo,
                            (this.detailed == 1 ? true : false) );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }


    @GET
    @Path( "/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/nodes" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvNodes getNFFGNodes(
            @PathParam( "nffgName" ) String nffgName ) {

        NffgReader nffgI = system.getNffg( nffgName );

        if ( nffgI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        Set<NodeReader> nodes = null;

        if ( this.page != 0 ) {
            nodes = Utils.getPage( this.page, this.itemsPerPage, nffgI.getNodes() );
        } else {
            nodes = nffgI.getNodes();
        }

        NfvNodes      result   = new NfvNodes();
        List<NfvNode> liveList = result.getNfvNode();

        NfvNode node = null;
        for ( NodeReader nodeI : nodes ) {

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

    @POST
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_XML )
    public NfvNFFG postNFFG( NfvNFFG nffg ) {

        if ( !(isValid( nffg )) )
            throw new WebApplicationException(
                    Response.Status.BAD_REQUEST ); // 400

        try {
            system.addNffg( nffg.getName() );

            for ( NfvNode node : nffg.getNfvNode() ) {
                system.addNode(
                        node.getName(),
                        node.getHostingHost(),
                        node.getFunctionalType(),
                        node.getAssociatedNFFG() );
            }

            for ( NfvNode node : nffg.getNfvNode() ) {
                for ( NfvArc link : node.getNfvArc() ) {
                    system.addLink(
                            link.getName(),
                            link.getSrc(),
                            link.getDst(),
                            link.getThroughput(),
                            link.getLatency() );
                }
            }
        } catch ( ServiceException e ) {

            system.deleteNffg( nffg.getName() ); // clean up

            throw new WebApplicationException(
                    Response.Status.FORBIDDEN ); // 403
        }

        NffgReader nffgI = system.getNffg( nffg.getName() );

        NfvNFFG result = buildNfvNFFG( nffgI, this.uriInfo, false );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }

    @DELETE
    @Path( "/{nffgName: [a-zA-Z][a-zA-Z0-9]*}" )
    public Response deleteNFFG(
            @PathParam( "nffgName" ) String nffgName ) {

        if ( system.getNffg( nffgName ) == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        system.deleteNffg( nffgName );

        return Response.noContent().build();
    }




    protected static NfvNFFG buildNfvNFFG(
            NffgReader nffgI,
            UriInfo uriInfo,
            boolean detailed ) {

        NfvNFFG nffg = new NfvNFFG();
        nffg.setName( nffgI.getName() );

        /* set NFFG deploy time */
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis( nffgI.getDeployTime().getTimeInMillis() );
        XMLGregorianCalendar time = null;
        try {
            time = DatatypeFactory.newInstance().newXMLGregorianCalendar( gc );
        } catch ( Exception e ) {}

        nffg.setDeployTime( time );

        if ( detailed ) {
            List<NfvNode> nodes = nffg.getNfvNode();
            for ( NodeReader nodeI : nffgI.getNodes() ) {
                nodes.add( NodesResource.buildNfvNode( nodeI, uriInfo, true ) );
            }
        }

        nffg.setSelf(
                Utils.getNFFGLink( uriInfo, nffgI.getName() ) );
        nffg.setAllocatedNodesLink(
                Utils.getNFFGNodesLink( uriInfo, nffgI.getName() ) );
        nffg.setLinksLink(
                Utils.getNFFGLinksLink( uriInfo, nffgI.getName() ) );

        return nffg;
    }


    protected static boolean isValid ( NfvNFFG nffg ) {

        if ( nffg.getName() == null )
            return false;

        for ( NfvNode node : nffg.getNfvNode() ) {
            if ( !(NodesResource.isValid( node )) )
                    return false;
        }

        return true;
    }

}
