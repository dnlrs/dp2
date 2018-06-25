package it.polito.dp2.NFV.sol3.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.NFV.sol3.service.model.nfvdeployer.Services;

@Path( "/" )
public class MainResource {

    @Context
    UriInfo uriInfo;


    public MainResource() {}

    @GET
    @Produces( MediaType.APPLICATION_XML )
    public Services getEntryPoint() {

        Services result = new Services();

        result.setServiceLink( Utils.getServiceLink(this.uriInfo) );
        result.setHostsLink( Utils.getHostsLink( this.uriInfo ) );
        result.setConnectionsLink( Utils.getConnectionsLink( this.uriInfo ) );
        result.setVnfsLink( Utils.getVNFsLink( this.uriInfo ) );
        result.setNffgsLink( Utils.getNFFGsLink( this.uriInfo ) );
        result.setNodesLink( Utils.getNodesLink( this.uriInfo ) );

        if ( (result.getHostsLink() == null)
                || (result.getConnectionsLink() == null)
                || (result.getVnfsLink()        == null)
                || (result.getNffgsLink()       == null)
                || (result.getNodesLink()       == null) )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }
}
