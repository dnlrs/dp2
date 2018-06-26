
package it.polito.dp2.NFV.sol3.client2.model.nfvdeployer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.uri.UriTemplate;

@Generated(value = {
    "wadl|http://localhost:8080/NfvDeployer/rest/application.wadl"
}, comments = "wadl2java, http://wadl.java.net", date = "2018-06-26T13:08:24.222+02:00")
public class Localhost_NfvDeployerRest {

    /**
     * The base URI for the resource represented by this proxy
     * 
     */
    public final static URI BASE_URI;

    static {
        URI originalURI = URI.create("http://localhost:8080/NfvDeployer/rest/");
        // Look up to see if we have any indirection in the local copy
        // of META-INF/java-rs-catalog.xml file, assuming it will be in the
        // oasis:name:tc:entity:xmlns:xml:catalog namespace or similar duck type
        java.io.InputStream is = Localhost_NfvDeployerRest.class.getResourceAsStream("/META-INF/jax-rs-catalog.xml");
        if (is!=null) {
            try {
                // Ignore the namespace in the catalog, can't use wildcard until
                // we are sure we have XPath 2.0
                String found = javax.xml.xpath.XPathFactory.newInstance().newXPath().evaluate(
                    "/*[name(.) = 'catalog']/*[name(.) = 'uri' and @name ='" + originalURI +"']/@uri", 
                    new org.xml.sax.InputSource(is)); 
                if (found!=null && found.length()>0) {
                    originalURI = java.net.URI.create(found);
                }
                
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    is.close();
                } catch (java.io.IOException e) {
                }
            }
        }
        BASE_URI = originalURI;
    }

    public static Localhost_NfvDeployerRest.NffgsNffgNameLinks nffgsNffgNameLinks(Client client, URI baseURI, String nffgname) {
        return new Localhost_NfvDeployerRest.NffgsNffgNameLinks(client, baseURI, nffgname);
    }

    /**
     * Template method to allow tooling to customize the new Client
     * 
     */
    private static void customizeClientConfiguration(ClientConfig cc) {
    }

    /**
     * Template method to allow tooling to override Client factory
     * 
     */
    private static Client createClientInstance(ClientConfig cc) {
        return Client.create(cc);
    }

    /**
     * Create a new Client instance
     * 
     */
    public static Client createClient() {
        ClientConfig cc = new DefaultClientConfig();
        customizeClientConfiguration(cc);
        return createClientInstance(cc);
    }

    public static Localhost_NfvDeployerRest.NffgsNffgNameLinks nffgsNffgNameLinks(String nffgname) {
        return nffgsNffgNameLinks(createClient(), BASE_URI, nffgname);
    }

    public static Localhost_NfvDeployerRest.NffgsNffgNameLinks nffgsNffgNameLinks(Client client, String nffgname) {
        return nffgsNffgNameLinks(client, BASE_URI, nffgname);
    }

    public static Localhost_NfvDeployerRest.Nodes nodes(Client client, URI baseURI) {
        return new Localhost_NfvDeployerRest.Nodes(client, baseURI);
    }

    public static Localhost_NfvDeployerRest.Nodes nodes() {
        return nodes(createClient(), BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Nodes nodes(Client client) {
        return nodes(client, BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Vnfs vnfs(Client client, URI baseURI) {
        return new Localhost_NfvDeployerRest.Vnfs(client, baseURI);
    }

    public static Localhost_NfvDeployerRest.Vnfs vnfs() {
        return vnfs(createClient(), BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Vnfs vnfs(Client client) {
        return vnfs(client, BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Nffgs nffgs(Client client, URI baseURI) {
        return new Localhost_NfvDeployerRest.Nffgs(client, baseURI);
    }

    public static Localhost_NfvDeployerRest.Nffgs nffgs() {
        return nffgs(createClient(), BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Nffgs nffgs(Client client) {
        return nffgs(client, BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Hosts hosts(Client client, URI baseURI) {
        return new Localhost_NfvDeployerRest.Hosts(client, baseURI);
    }

    public static Localhost_NfvDeployerRest.Hosts hosts() {
        return hosts(createClient(), BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Hosts hosts(Client client) {
        return hosts(client, BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Connections connections(Client client, URI baseURI) {
        return new Localhost_NfvDeployerRest.Connections(client, baseURI);
    }

    public static Localhost_NfvDeployerRest.Connections connections() {
        return connections(createClient(), BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Connections connections(Client client) {
        return connections(client, BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Root root(Client client, URI baseURI) {
        return new Localhost_NfvDeployerRest.Root(client, baseURI);
    }

    public static Localhost_NfvDeployerRest.Root root() {
        return root(createClient(), BASE_URI);
    }

    public static Localhost_NfvDeployerRest.Root root(Client client) {
        return root(client, BASE_URI);
    }

    public static class Connections {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private Connections(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public Connections(Client client, URI baseUri) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/connections");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        public NfvArcs getAsNfvArcs() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvArcs.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvArcs getAsNfvArcs(Integer page, Integer itemsperpage, String sourcehost, String destinationhost) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (sourcehost == null) {
            }
            if (sourcehost!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("sourceHost", sourcehost);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("sourceHost", ((Object[]) null));
            }
            if (destinationhost == null) {
            }
            if (destinationhost!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("destinationHost", destinationhost);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("destinationHost", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvArcs.class);
        }

        public<T >T getAsXml(Integer page, Integer itemsperpage, String sourcehost, String destinationhost, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (sourcehost == null) {
            }
            if (sourcehost!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("sourceHost", sourcehost);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("sourceHost", ((Object[]) null));
            }
            if (destinationhost == null) {
            }
            if (destinationhost!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("destinationHost", destinationhost);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("destinationHost", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Integer page, Integer itemsperpage, String sourcehost, String destinationhost, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (sourcehost == null) {
            }
            if (sourcehost!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("sourceHost", sourcehost);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("sourceHost", ((Object[]) null));
            }
            if (destinationhost == null) {
            }
            if (destinationhost!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("destinationHost", destinationhost);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("destinationHost", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

    }

    public static class Hosts {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private Hosts(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public Hosts(Client client, URI baseUri) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/hosts");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        public NfvHosts getAsNfvHosts() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvHosts.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvHosts getAsNfvHosts(Integer detailed, Integer page, Integer itemsperpage) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvHosts.class);
        }

        public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public Localhost_NfvDeployerRest.Hosts.HostNameNodes hostNameNodes(String hostname) {
            return new Localhost_NfvDeployerRest.Hosts.HostNameNodes(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), hostname);
        }

        public Localhost_NfvDeployerRest.Hosts.HostName hostName(String hostname) {
            return new Localhost_NfvDeployerRest.Hosts.HostName(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), hostname);
        }

        public static class HostName {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private HostName(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public HostName(Client client, URI baseUri, String hostname) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{hostName: [a-zA-Z][a-zA-Z0-9]*}");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("hostName", hostname);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public HostName(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/hosts/{hostName: [a-zA-Z][a-zA-Z0-9]*}");
                } else {
                    template.append("hosts/{hostName: [a-zA-Z][a-zA-Z0-9]*}");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get hostName
             * 
             */
            public String getHostname() {
                return ((String) _templateAndMatrixParameterValues.get("hostName"));
            }

            /**
             * Duplicate state and set hostName
             * 
             */
            public Localhost_NfvDeployerRest.Hosts.HostName setHostname(String hostname) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("hostName", hostname);
                return new Localhost_NfvDeployerRest.Hosts.HostName(_client, copyUriBuilder, copyMap);
            }

            public NfvHost getAsNfvHost() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvHost.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvHost getAsNfvHost(Integer detailed, Integer page, Integer itemsperpage) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvHost.class);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

        public static class HostNameNodes {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private HostNameNodes(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public HostNameNodes(Client client, URI baseUri, String hostname) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{hostName: [a-zA-Z][a-zA-Z0-9]*}/nodes");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("hostName", hostname);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public HostNameNodes(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/hosts/{hostName: [a-zA-Z][a-zA-Z0-9]*}/nodes");
                } else {
                    template.append("hosts/{hostName: [a-zA-Z][a-zA-Z0-9]*}/nodes");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get hostName
             * 
             */
            public String getHostname() {
                return ((String) _templateAndMatrixParameterValues.get("hostName"));
            }

            /**
             * Duplicate state and set hostName
             * 
             */
            public Localhost_NfvDeployerRest.Hosts.HostNameNodes setHostname(String hostname) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("hostName", hostname);
                return new Localhost_NfvDeployerRest.Hosts.HostNameNodes(_client, copyUriBuilder, copyMap);
            }

            public NfvNodes getAsNfvNodes() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNodes.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvNodes getAsNfvNodes(Integer detailed, Integer page, Integer itemsperpage) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNodes.class);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

    }

    public static class Nffgs {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private Nffgs(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public Nffgs(Client client, URI baseUri) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/nffgs");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        public NfvNFFGs getAsNfvNFFGs() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNFFGs.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvNFFGs getAsNfvNFFGs(Integer page, Integer itemsperpage, String date, Integer detailed) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (date == null) {
            }
            if (date!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
            }
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNFFGs.class);
        }

        public<T >T getAsXml(Integer page, Integer itemsperpage, String date, Integer detailed, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (date == null) {
            }
            if (date!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
            }
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Integer page, Integer itemsperpage, String date, Integer detailed, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (date == null) {
            }
            if (date!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
            }
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvNFFG postXmlAsNfvNFFG(NfvNFFG input) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNFFG.class);
        }

        public<T >T postXml(Object input, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T postXml(Object input, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvNFFG postXmlAsNfvNFFG(NfvNFFG input, Integer page, Integer itemsperpage, String date, Integer detailed) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (date == null) {
            }
            if (date!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
            }
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNFFG.class);
        }

        public<T >T postXml(Object input, Integer page, Integer itemsperpage, String date, Integer detailed, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (date == null) {
            }
            if (date!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
            }
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T postXml(Object input, Integer page, Integer itemsperpage, String date, Integer detailed, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (date == null) {
            }
            if (date!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
            }
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public Localhost_NfvDeployerRest.Nffgs.NffgName nffgName(String nffgname) {
            return new Localhost_NfvDeployerRest.Nffgs.NffgName(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), nffgname);
        }

        public Localhost_NfvDeployerRest.Nffgs.NffgNameNodes nffgNameNodes(String nffgname) {
            return new Localhost_NfvDeployerRest.Nffgs.NffgNameNodes(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), nffgname);
        }

        public static class NffgName {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private NffgName(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public NffgName(Client client, URI baseUri, String nffgname) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{nffgName: [a-zA-Z][a-zA-Z0-9]*}");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("nffgName", nffgname);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public NffgName(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}");
                } else {
                    template.append("nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get nffgName
             * 
             */
            public String getNffgname() {
                return ((String) _templateAndMatrixParameterValues.get("nffgName"));
            }

            /**
             * Duplicate state and set nffgName
             * 
             */
            public Localhost_NfvDeployerRest.Nffgs.NffgName setNffgname(String nffgname) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("nffgName", nffgname);
                return new Localhost_NfvDeployerRest.Nffgs.NffgName(_client, copyUriBuilder, copyMap);
            }

            public<T >T deleteAs(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T deleteAs(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public<T >T deleteAs(Integer page, Integer itemsperpage, String date, Integer detailed, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T deleteAs(Integer page, Integer itemsperpage, String date, Integer detailed, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvNFFG getAsNfvNFFG() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNFFG.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvNFFG getAsNfvNFFG(Integer page, Integer itemsperpage, String date, Integer detailed) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNFFG.class);
            }

            public<T >T getAsXml(Integer page, Integer itemsperpage, String date, Integer detailed, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer page, Integer itemsperpage, String date, Integer detailed, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

        public static class NffgNameNodes {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private NffgNameNodes(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public NffgNameNodes(Client client, URI baseUri, String nffgname) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/nodes");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("nffgName", nffgname);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public NffgNameNodes(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/nodes");
                } else {
                    template.append("nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/nodes");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get nffgName
             * 
             */
            public String getNffgname() {
                return ((String) _templateAndMatrixParameterValues.get("nffgName"));
            }

            /**
             * Duplicate state and set nffgName
             * 
             */
            public Localhost_NfvDeployerRest.Nffgs.NffgNameNodes setNffgname(String nffgname) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("nffgName", nffgname);
                return new Localhost_NfvDeployerRest.Nffgs.NffgNameNodes(_client, copyUriBuilder, copyMap);
            }

            public NfvNodes getAsNfvNodes() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNodes.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvNodes getAsNfvNodes(Integer page, Integer itemsperpage, String date, Integer detailed) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNodes.class);
            }

            public<T >T getAsXml(Integer page, Integer itemsperpage, String date, Integer detailed, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer page, Integer itemsperpage, String date, Integer detailed, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (date == null) {
                }
                if (date!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", date);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("date", ((Object[]) null));
                }
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

    }

    public static class NffgsNffgNameLinks {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private NffgsNffgNameLinks(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public NffgsNffgNameLinks(Client client, URI baseUri, String nffgname) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/links");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
            _templateAndMatrixParameterValues.put("nffgName", nffgname);
        }

        /**
         * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
         * 
         */
        public NffgsNffgNameLinks(Client client, URI uri) {
            _client = client;
            StringBuilder template = new StringBuilder(BASE_URI.toString());
            if (template.charAt((template.length()- 1))!= '/') {
                template.append("/nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/links");
            } else {
                template.append("nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/links");
            }
            _uriBuilder = UriBuilder.fromPath(template.toString());
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
            UriTemplate uriTemplate = new UriTemplate(template.toString());
            HashMap<String, String> parameters = new HashMap<String, String>();
            uriTemplate.match(uri.toString(), parameters);
            _templateAndMatrixParameterValues.putAll(parameters);
        }

        /**
         * Get nffgName
         * 
         */
        public String getNffgname() {
            return ((String) _templateAndMatrixParameterValues.get("nffgName"));
        }

        /**
         * Duplicate state and set nffgName
         * 
         */
        public Localhost_NfvDeployerRest.NffgsNffgNameLinks setNffgname(String nffgname) {
            Map<String, Object> copyMap;
            copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
            UriBuilder copyUriBuilder = _uriBuilder.clone();
            copyMap.put("nffgName", nffgname);
            return new Localhost_NfvDeployerRest.NffgsNffgNameLinks(_client, copyUriBuilder, copyMap);
        }

        public NfvArcs getAsNfvArcs() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvArcs.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvArcs getAsNfvArcs(Integer page, Integer itemsperpage, Integer update) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (update == null) {
            }
            if (update!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvArcs.class);
        }

        public<T >T getAsXml(Integer page, Integer itemsperpage, Integer update, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (update == null) {
            }
            if (update!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Integer page, Integer itemsperpage, Integer update, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (update == null) {
            }
            if (update!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvArc postXmlAsNfvArc(NfvArc input) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvArc.class);
        }

        public<T >T postXml(Object input, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T postXml(Object input, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvArc postXmlAsNfvArc(NfvArc input, Integer page, Integer itemsperpage, Integer update) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (update == null) {
            }
            if (update!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvArc.class);
        }

        public<T >T postXml(Object input, Integer page, Integer itemsperpage, Integer update, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (update == null) {
            }
            if (update!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T postXml(Object input, Integer page, Integer itemsperpage, Integer update, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            if (update == null) {
            }
            if (update!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public Localhost_NfvDeployerRest.NffgsNffgNameLinks.LinkName linkName(String linkname) {
            return new Localhost_NfvDeployerRest.NffgsNffgNameLinks.LinkName(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), linkname);
        }

        public static class LinkName {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private LinkName(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public LinkName(Client client, URI baseUri, String linkname) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("{linkName: [a-zA-Z][a-zA-Z0-9]*}");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("linkName", linkname);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public LinkName(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/links/{linkName: [a-zA-Z][a-zA-Z0-9]*}");
                } else {
                    template.append("nffgs/{nffgName: [a-zA-Z][a-zA-Z0-9]*}/links/{linkName: [a-zA-Z][a-zA-Z0-9]*}");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get linkName
             * 
             */
            public String getLinkname() {
                return ((String) _templateAndMatrixParameterValues.get("linkName"));
            }

            /**
             * Duplicate state and set linkName
             * 
             */
            public Localhost_NfvDeployerRest.NffgsNffgNameLinks.LinkName setLinkname(String linkname) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("linkName", linkname);
                return new Localhost_NfvDeployerRest.NffgsNffgNameLinks.LinkName(_client, copyUriBuilder, copyMap);
            }

            public<T >T deleteAs(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T deleteAs(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public<T >T deleteAs(Integer page, Integer itemsperpage, Integer update, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (update == null) {
                }
                if (update!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T deleteAs(Integer page, Integer itemsperpage, Integer update, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (update == null) {
                }
                if (update!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvArc getAsNfvArc() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvArc.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvArc getAsNfvArc(Integer page, Integer itemsperpage, Integer update) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (update == null) {
                }
                if (update!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvArc.class);
            }

            public<T >T getAsXml(Integer page, Integer itemsperpage, Integer update, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (update == null) {
                }
                if (update!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer page, Integer itemsperpage, Integer update, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                if (update == null) {
                }
                if (update!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", update);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("update", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

    }

    public static class Nodes {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private Nodes(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public Nodes(Client client, URI baseUri) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/nodes");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        public NfvNodes getAsNfvNodes() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNodes.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvNodes getAsNfvNodes(Integer detailed, Integer page, Integer itemsperpage) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNodes.class);
        }

        public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvNode postXmlAsNfvNode(NfvNode input) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNode.class);
        }

        public<T >T postXml(Object input, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T postXml(Object input, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public NfvNode postXmlAsNfvNode(NfvNode input, Integer detailed, Integer page, Integer itemsperpage) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvNode.class);
        }

        public<T >T postXml(Object input, Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T postXml(Object input, Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            if (detailed == null) {
            }
            if (detailed!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
            }
            if (page == null) {
            }
            if (page!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
            }
            if (itemsperpage == null) {
            }
            if (itemsperpage!= null) {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
            } else {
                localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
            }
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            resourceBuilder = resourceBuilder.type("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("POST", ClientResponse.class, input);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public Localhost_NfvDeployerRest.Nodes.NodeNameLinks nodeNameLinks(String nodename) {
            return new Localhost_NfvDeployerRest.Nodes.NodeNameLinks(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), nodename);
        }

        public Localhost_NfvDeployerRest.Nodes.NodeNameReachableHosts nodeNameReachableHosts(String nodename) {
            return new Localhost_NfvDeployerRest.Nodes.NodeNameReachableHosts(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), nodename);
        }

        public Localhost_NfvDeployerRest.Nodes.NodeName nodeName(String nodename) {
            return new Localhost_NfvDeployerRest.Nodes.NodeName(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), nodename);
        }

        public static class NodeName {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private NodeName(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public NodeName(Client client, URI baseUri, String nodename) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{nodeName: [a-zA-Z][a-zA-Z0-9]*}");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("nodeName", nodename);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public NodeName(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/nodes/{nodeName: [a-zA-Z][a-zA-Z0-9]*}");
                } else {
                    template.append("nodes/{nodeName: [a-zA-Z][a-zA-Z0-9]*}");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get nodeName
             * 
             */
            public String getNodename() {
                return ((String) _templateAndMatrixParameterValues.get("nodeName"));
            }

            /**
             * Duplicate state and set nodeName
             * 
             */
            public Localhost_NfvDeployerRest.Nodes.NodeName setNodename(String nodename) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("nodeName", nodename);
                return new Localhost_NfvDeployerRest.Nodes.NodeName(_client, copyUriBuilder, copyMap);
            }

            public<T >T deleteAs(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T deleteAs(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public<T >T deleteAs(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T deleteAs(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("*/*");
                ClientResponse response;
                response = resourceBuilder.method("DELETE", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvNode getAsNfvNode() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNode.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvNode getAsNfvNode(Integer detailed, Integer page, Integer itemsperpage) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvNode.class);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

        public static class NodeNameLinks {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private NodeNameLinks(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public NodeNameLinks(Client client, URI baseUri, String nodename) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/links");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("nodeName", nodename);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public NodeNameLinks(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/nodes/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/links");
                } else {
                    template.append("nodes/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/links");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get nodeName
             * 
             */
            public String getNodename() {
                return ((String) _templateAndMatrixParameterValues.get("nodeName"));
            }

            /**
             * Duplicate state and set nodeName
             * 
             */
            public Localhost_NfvDeployerRest.Nodes.NodeNameLinks setNodename(String nodename) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("nodeName", nodename);
                return new Localhost_NfvDeployerRest.Nodes.NodeNameLinks(_client, copyUriBuilder, copyMap);
            }

            public NfvArcs getAsNfvArcs() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvArcs.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvArcs getAsNfvArcs(Integer detailed, Integer page, Integer itemsperpage) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvArcs.class);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

        public static class NodeNameReachableHosts {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private NodeNameReachableHosts(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public NodeNameReachableHosts(Client client, URI baseUri, String nodename) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/reachableHosts");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("nodeName", nodename);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public NodeNameReachableHosts(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/nodes/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/reachableHosts");
                } else {
                    template.append("nodes/{nodeName: [a-zA-Z][a-zA-Z0-9]*}/reachableHosts");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get nodeName
             * 
             */
            public String getNodename() {
                return ((String) _templateAndMatrixParameterValues.get("nodeName"));
            }

            /**
             * Duplicate state and set nodeName
             * 
             */
            public Localhost_NfvDeployerRest.Nodes.NodeNameReachableHosts setNodename(String nodename) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("nodeName", nodename);
                return new Localhost_NfvDeployerRest.Nodes.NodeNameReachableHosts(_client, copyUriBuilder, copyMap);
            }

            public NfvHosts getAsNfvHosts() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvHosts.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

            public NfvHosts getAsNfvHosts(Integer detailed, Integer page, Integer itemsperpage) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvHosts.class);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Integer detailed, Integer page, Integer itemsperpage, Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                if (detailed == null) {
                }
                if (detailed!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", detailed);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("detailed", ((Object[]) null));
                }
                if (page == null) {
                }
                if (page!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", page);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("page", ((Object[]) null));
                }
                if (itemsperpage == null) {
                }
                if (itemsperpage!= null) {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", itemsperpage);
                } else {
                    localUriBuilder = localUriBuilder.replaceQueryParam("itemsPerPage", ((Object[]) null));
                }
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

    }

    public static class Root {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private Root(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public Root(Client client, URI baseUri) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        public Services getAsServices() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(Services.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public Localhost_NfvDeployerRest.Root.SwaggerJson swaggerJson() {
            return new Localhost_NfvDeployerRest.Root.SwaggerJson(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues));
        }

        public Localhost_NfvDeployerRest.Root.SwaggerYaml swaggerYaml() {
            return new Localhost_NfvDeployerRest.Root.SwaggerYaml(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues));
        }

        public static class SwaggerJson {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private SwaggerJson(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public SwaggerJson(Client client, URI baseUri) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/swagger.json");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
            }

            public<T >T getAsJson(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsJson(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/json");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

        public static class SwaggerYaml {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private SwaggerYaml(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public SwaggerYaml(Client client, URI baseUri) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/swagger.yaml");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
            }

            public<T >T getAsYaml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/yaml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsYaml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/yaml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

    }

    public static class Vnfs {

        private Client _client;
        private UriBuilder _uriBuilder;
        private Map<String, Object> _templateAndMatrixParameterValues;

        private Vnfs(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
            _client = client;
            _uriBuilder = uriBuilder.clone();
            _templateAndMatrixParameterValues = map;
        }

        /**
         * Create new instance using existing Client instance, and a base URI and any parameters
         * 
         */
        public Vnfs(Client client, URI baseUri) {
            _client = client;
            _uriBuilder = UriBuilder.fromUri(baseUri);
            _uriBuilder = _uriBuilder.path("/vnfs");
            _templateAndMatrixParameterValues = new HashMap<String, Object>();
        }

        public NfvVNFs getAsNfvVNFs() {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(NfvVNFs.class);
        }

        public<T >T getAsXml(GenericType<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (response.getStatus()>= 400) {
                throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
            }
            return response.getEntity(returnType);
        }

        public<T >T getAsXml(Class<T> returnType) {
            UriBuilder localUriBuilder = _uriBuilder.clone();
            WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
            com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
            resourceBuilder = resourceBuilder.accept("application/xml");
            ClientResponse response;
            response = resourceBuilder.method("GET", ClientResponse.class);
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
            }
            if (!ClientResponse.class.isAssignableFrom(returnType)) {
                return response.getEntity(returnType);
            } else {
                return returnType.cast(response);
            }
        }

        public Localhost_NfvDeployerRest.Vnfs.VnfName vnfName(String vnfname) {
            return new Localhost_NfvDeployerRest.Vnfs.VnfName(_client, _uriBuilder.buildFromMap(_templateAndMatrixParameterValues), vnfname);
        }

        public static class VnfName {

            private Client _client;
            private UriBuilder _uriBuilder;
            private Map<String, Object> _templateAndMatrixParameterValues;

            private VnfName(Client client, UriBuilder uriBuilder, Map<String, Object> map) {
                _client = client;
                _uriBuilder = uriBuilder.clone();
                _templateAndMatrixParameterValues = map;
            }

            /**
             * Create new instance using existing Client instance, and a base URI and any parameters
             * 
             */
            public VnfName(Client client, URI baseUri, String vnfname) {
                _client = client;
                _uriBuilder = UriBuilder.fromUri(baseUri);
                _uriBuilder = _uriBuilder.path("/{vnfName: [a-zA-Z][a-zA-Z0-9]*}");
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                _templateAndMatrixParameterValues.put("vnfName", vnfname);
            }

            /**
             * Create new instance using existing Client instance, and the URI from which the parameters will be extracted
             * 
             */
            public VnfName(Client client, URI uri) {
                _client = client;
                StringBuilder template = new StringBuilder(BASE_URI.toString());
                if (template.charAt((template.length()- 1))!= '/') {
                    template.append("/vnfs/{vnfName: [a-zA-Z][a-zA-Z0-9]*}");
                } else {
                    template.append("vnfs/{vnfName: [a-zA-Z][a-zA-Z0-9]*}");
                }
                _uriBuilder = UriBuilder.fromPath(template.toString());
                _templateAndMatrixParameterValues = new HashMap<String, Object>();
                UriTemplate uriTemplate = new UriTemplate(template.toString());
                HashMap<String, String> parameters = new HashMap<String, String>();
                uriTemplate.match(uri.toString(), parameters);
                _templateAndMatrixParameterValues.putAll(parameters);
            }

            /**
             * Get vnfName
             * 
             */
            public String getVnfname() {
                return ((String) _templateAndMatrixParameterValues.get("vnfName"));
            }

            /**
             * Duplicate state and set vnfName
             * 
             */
            public Localhost_NfvDeployerRest.Vnfs.VnfName setVnfname(String vnfname) {
                Map<String, Object> copyMap;
                copyMap = new HashMap<String, Object>(_templateAndMatrixParameterValues);
                UriBuilder copyUriBuilder = _uriBuilder.clone();
                copyMap.put("vnfName", vnfname);
                return new Localhost_NfvDeployerRest.Vnfs.VnfName(_client, copyUriBuilder, copyMap);
            }

            public NfvVNF getAsNfvVNF() {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(NfvVNF.class);
            }

            public<T >T getAsXml(GenericType<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (response.getStatus()>= 400) {
                    throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                }
                return response.getEntity(returnType);
            }

            public<T >T getAsXml(Class<T> returnType) {
                UriBuilder localUriBuilder = _uriBuilder.clone();
                WebResource resource = _client.resource(localUriBuilder.buildFromMap(_templateAndMatrixParameterValues));
                com.sun.jersey.api.client.WebResource.Builder resourceBuilder = resource.getRequestBuilder();
                resourceBuilder = resourceBuilder.accept("application/xml");
                ClientResponse response;
                response = resourceBuilder.method("GET", ClientResponse.class);
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    if (response.getStatus()>= 400) {
                        throw new Localhost_NfvDeployerRest.WebApplicationExceptionMessage(Response.status(response.getClientResponseStatus()).build());
                    }
                }
                if (!ClientResponse.class.isAssignableFrom(returnType)) {
                    return response.getEntity(returnType);
                } else {
                    return returnType.cast(response);
                }
            }

        }

    }


    /**
     * Workaround for JAX_RS_SPEC-312
     * 
     */
    private static class WebApplicationExceptionMessage
        extends WebApplicationException
    {


        private WebApplicationExceptionMessage(Response response) {
            super(response);
        }

        /**
         * Workaround for JAX_RS_SPEC-312
         * 
         */
        public String getMessage() {
            Response response = getResponse();
            Response.Status status = Response.Status.fromStatusCode(response.getStatus());
            if (status!= null) {
                return (response.getStatus()+(" "+ status.getReasonPhrase()));
            } else {
                return Integer.toString(response.getStatus());
            }
        }

        public String toString() {
            String s = "javax.ws.rs.WebApplicationException";
            String message = getLocalizedMessage();
            return (s +(": "+ message));
        }

    }

}
