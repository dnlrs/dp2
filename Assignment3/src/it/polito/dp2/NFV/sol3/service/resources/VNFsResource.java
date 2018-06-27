package it.polito.dp2.NFV.sol3.service.resources;

import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.FunctionalTypeEnum;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvVNF;
import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.NfvVNFs;
import it.polito.dp2.NFV.sol3.service.nfvSystem.NfvSystem;

@Path( "/vnfs" )
public class VNFsResource {

    private final static NfvSystem system = new NfvSystem();

    @Context
    UriInfo uriInfo;

    public VNFsResource() {}

    @GET
    @Produces( MediaType.APPLICATION_XML )
    public NfvVNFs getVNFs() {

        Set<VNFTypeReader> vnfs = system.getVNFCatalog();

        if ( vnfs.isEmpty() )
            throw new WebApplicationException(
                    Response.Status.NO_CONTENT ); // 204

        NfvVNFs      result   = new NfvVNFs();
        List<NfvVNF> liveList = result.getNfvVNF();

        NfvVNF vnf = null;
        for ( VNFTypeReader vnfI : vnfs ) {
            vnf = buildNfvVNF( vnfI, this.uriInfo, true );

            if ( vnf == null )
                throw new WebApplicationException(
                        Response.Status.INTERNAL_SERVER_ERROR ); // 500

            liveList.add( vnf );
        }

        return result;
    }

    @GET
    @Path( "/{vnfName: [a-zA-Z][a-zA-Z0-9]*}" )
    @Produces( MediaType.APPLICATION_XML )
    public NfvVNF getVNF(
            @PathParam("vnfName") String vnfName ) {

        VNFTypeReader vnfI = system.getVNF( vnfName );

        if ( vnfI == null )
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND ); // 404

        NfvVNF result = buildNfvVNF( vnfI, this.uriInfo, true );

        if ( result == null )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }




    protected static NfvVNF buildNfvVNF(
            VNFTypeReader vnfI,
            UriInfo uriInfo,
            boolean detailed ) {

        NfvVNF vnf = new NfvVNF();

        vnf.setName( vnfI.getName() );
        vnf.setFunctionalType( FunctionalTypeEnum.valueOf( vnfI.getFunctionalType().name() ) );
        vnf.setRequiredMemory( new Integer( vnfI.getRequiredMemory() )  );
        vnf.setRequiredStorage( new Integer( vnfI.getRequiredStorage() ) );

        vnf.setSelf( Utils.getVNFLink( uriInfo, vnfI.getName() ) );

        return vnf;
    }

}
