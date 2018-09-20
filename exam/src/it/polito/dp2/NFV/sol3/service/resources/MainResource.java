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

    public MainResource() {}

    @GET
    @Produces( MediaType.APPLICATION_XML )
    public Services getEntryPoint(
            @Context UriInfo uriInfo ) {

        Services result = new Services();


        result.setServiceLink( Utils.getServiceLink( uriInfo.getBaseUri() ) );
        result.setHostsLink( Utils.getHostsLink( uriInfo.getBaseUri() ) );
        result.setConnectionsLink( Utils.getConnectionsLink( uriInfo.getBaseUri() ) );
        result.setVnfsLink( Utils.getVNFsLink( uriInfo.getBaseUri() ) );
        result.setNffgsLink( Utils.getNFFGsLink( uriInfo.getBaseUri() ) );
        result.setNodesLink( Utils.getNodesLink( uriInfo.getBaseUri() ) );

        if ( (result.getServiceLink() == null)
                || (result.getHostsLink()       == null)
                || (result.getConnectionsLink() == null)
                || (result.getVnfsLink()        == null)
                || (result.getNffgsLink()       == null)
                || (result.getNodesLink()       == null) )
            throw new WebApplicationException(
                    Response.Status.INTERNAL_SERVER_ERROR ); // 500

        return result;
    }
}
