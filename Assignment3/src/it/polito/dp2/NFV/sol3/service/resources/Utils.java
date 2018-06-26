package it.polito.dp2.NFV.sol3.service.resources;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

import it.polito.dp2.NFV.NamedEntityReader;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.Link;


public class Utils {

    public static <T extends NamedEntityReader> Set<T> getPage(
            int page,
            int itemsPerPage,
            Set<T> all ) {


        if ( page == 0 )
            return all;

        if ( (page < 0) || (itemsPerPage < 1) )
            throw new WebApplicationException( Response.Status.BAD_REQUEST ); // 400

        int startIndex = (page - 1) * itemsPerPage;
        int lastIndex  = all.size();

        if ( startIndex > lastIndex )
            throw new WebApplicationException( Response.Status.NOT_FOUND ); // 404

        ArrayList<T> orderedList = new ArrayList<T>();
        orderedList.addAll( all );
//        orderedList.sort( Comparator.comparing( e -> e.getName() ) ); // lists are already ordered

        Set<T> result = new HashSet<T>();

        for ( int index = startIndex;
                (index < (startIndex + itemsPerPage)) && (index < lastIndex);
                index++ ) {
            result.add( orderedList.get( index ) );
        }

        return result;
    }


    /*
     * Links
     */

    /**
     * "{baseURL}/hosts"
     *
     * @param uriInfo
     * @return
     */
    protected static Link getHostsLink(
            UriInfo uriInfo ) {

        if ( uriInfo == null )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/hosts" )
                                   .build()
                                   .toString() );

        } catch ( IllegalArgumentException
                  | UriBuilderException e ) {
            return null;
        }

        return result;
    }

    /**
     * "{baseURL}/hosts/{hostName}"
     *
     * @param uriInfo
     * @param hostName
     * @return
     */
    protected static Link getHostLink(
            UriInfo uriInfo,
            String hostName ) {

        if ( (uriInfo == null) || (hostName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/hosts/{hostName}" )
                                   .build( hostName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }

    /**
     * "{baseURL}/hosts/{hostName}/nodes"
     *
     * @param uriInfo
     * @param hostName
     * @return
     */
    protected static Link getHostNodesLink(
            UriInfo uriInfo,
            String hostName ) {

        if ( (uriInfo == null) || (hostName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/hosts/{hostName}/nodes" )
                                   .build( hostName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;


    }



    /**
     * {baseURL}/connections
     *
     * @param uriInfo
     * @return
     */
    protected static Link getConnectionsLink(
            UriInfo uriInfo ) {

        if ( uriInfo == null )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/connections" )
                                   .build()
                                   .toString() );

        } catch ( IllegalArgumentException
                  | UriBuilderException e ) {
            return null;
        }

        return result;
    }


    protected static Link getConnectionLink(
            UriInfo uriInfo,
            String srcHostName,
            String dstHostName ) {

        if ( (uriInfo == null)
                || (srcHostName == null)
                || (dstHostName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/connections" )
                                   .queryParam( "sourceHost", srcHostName )
                                   .queryParam( "destinationHost", dstHostName )
                                   .build()
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
        }

        return result;
    }


    /**
     * {baseURL}/vnfs
     *
     * @param uriInfo
     * @return
     */
    protected static Link getVNFsLink(
            UriInfo uriInfo ) {

        if ( uriInfo == null )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/vnfs" )
                                   .build()
                                   .toString() );

        } catch ( IllegalArgumentException
                  | UriBuilderException e ) {
            return null;
        }

        return result;
    }


    /**
     * {baseURL}/vnfs/{vnfName}
     *
     * @param uriInfo
     * @param vnfName
     * @return
     */
    protected static Link getVNFLink(
            UriInfo uriInfo,
            String vnfName ) {

        if ( (uriInfo == null) || (vnfName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/vnfs/{vnfName}" )
                                   .build( vnfName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }


    /**
     * {baseURL}/nffgs
     *
     * @param uriInfo
     * @return
     */
    protected static Link getNFFGsLink(
            UriInfo uriInfo ) {

        if ( uriInfo == null )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nffgs" )
                                   .build()
                                   .toString() );

        } catch ( IllegalArgumentException
                  | UriBuilderException e ) {
            return null;
        }

        return result;
    }


    /**
     * {baseURL}/nffgs/{nffgName}
     *
     * @param uriInfo
     * @param nffgName
     * @return
     */
    protected static Link getNFFGLink(
            UriInfo uriInfo,
            String nffgName ) {

        if ( (uriInfo == null) || (nffgName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nffgs/{nffgName}" )
                                   .build( nffgName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }


    /**
     *  {baseURL}/nffgs/{nffgName}/nodes
     *
     * @param uriInfo
     * @param nffgName
     * @return
     */
    protected static Link getNFFGNodesLink(
            UriInfo uriInfo,
            String nffgName ) {

        if ( (uriInfo == null) || (nffgName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nffgs/{nffgName}/nodes" )
                                   .build( nffgName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }


    /**
     *  {baseURL}/nffgs/{nffgName}/links
     *
     * @param uriInfo
     * @param nffgName
     * @return
     */
    protected static Link getNFFGLinksLink(
            UriInfo uriInfo,
            String nffgName ) {

        if ( (uriInfo == null) || (nffgName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nffgs/{nffgName}/links" )
                                   .build( nffgName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }

    /**
     *  {baseURL}/nffgs/{nffgName}/links/{linkName}
     *
     * @param uriInfo
     * @param nffgName
     * @return
     */
    protected static Link getLinkLink(
            UriInfo uriInfo,
            String nffgName,
            String linkName ) {

        if ( (uriInfo == null) || (nffgName == null) || (linkName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nffgs/{nffgName}/links/{linkName}" )
                                   .build( nffgName, linkName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }



    /**
     * {baseURL}/nodes
     *
     * @param uriInfo
     * @return
     */
    protected static Link getNodesLink(
            UriInfo uriInfo ) {


        if ( uriInfo == null )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nodes" )
                                   .build()
                                   .toString() );

        } catch ( IllegalArgumentException
                  | UriBuilderException e ) {
            return null;
        }

        return result;
    }

    /**
     * {baseURL}/nodes/{nodeName}
     *
     * @param uriInfo
     * @param nodeName
     * @return
     */
    protected static Link getNodeLink(
            UriInfo uriInfo,
            String nodeName ) {


        if ( (uriInfo == null) || (nodeName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nodes/{nodeName}" )
                                   .build( nodeName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }


    /**
     * {baseURL}/nodes/{nodeName}/links
     *
     * @param uriInfo
     * @param nodeName
     * @return
     */
    protected static Link getNodeLinksLink(
            UriInfo uriInfo,
            String nodeName ) {


        if ( (uriInfo == null) || (nodeName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nodes/{nodeName}/links" )
                                   .build( nodeName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }


    /**
     * {baseURL}/nodes/{nodeName}/reachableHosts
     *
     * @param uriInfo
     * @param nodeName
     * @return
     */
    protected static Link getNodeReachableHostsLink(
            UriInfo uriInfo,
            String nodeName ) {


        if ( (uriInfo == null) || (nodeName == null) )
            return null;

        Link result = new Link();

        try {

            result.setHref( uriInfo.getBaseUriBuilder()
                                   .path( "/nodes/{nodeName}/reachableHosts" )
                                   .build( nodeName )
                                   .toString() );

        } catch ( IllegalArgumentException
                | UriBuilderException e ) {
          return null;
      }

        return result;
    }


    protected static Link getServiceLink(
            UriInfo uriInfo ) {

        if ( uriInfo == null )
            return null;

        Link result = new Link();
        result.setHref( uriInfo.getBaseUri().toString() );

        return result;
    }


}
