/**
 * 
 */
package it.polito.dp2.NFV.lab3.test4;

import java.net.URL;
import java.util.Set;

import it.polito.dp2.NFV.lab3.NfvClient;
import it.polito.dp2.NFV.lab3.ServiceException;
import it.polito.dp2.NFV.lab3.UnknownEntityException;

/**
 * An extension of the {@link it.polito.dp2.NFV.lab3.NfvClient NfvClient} interface
 * which also lets a client to
 * - set the base URL of the service to contact
 * - specify a set of hosts onto which the operations of the {@link it.polito.dp2.NFV.lab3.NfvClient NfvClient} should not allocate nodes 
 */
public interface NfvClient3 extends NfvClient {
	
	/**
	 * Sets the base URL of the service to be used by this client (this is a local operation)
	 * @param url
	 */
	public void setBaseServiceURL(URL url);
	
	/**
	 * gets the base URL of the service currently being used by this client (this is a local operation)
	 * @return the base URL of the service currently being used by this client
	 */
	public URL getBaseServiceURL();
	
	/**
	 * stores locally in this client the specified set of names of unwanted hosts
	 * @param unwantedHosts the set of names of unwanted hosts
	 * @throws UnknownEntityException if some of the unwanted host names are unknown (i.e. are not names of hosts available in the system)
	 * @throws ServiceException if any other error occurs
	 */
	public void setUnwantedHosts(Set<String> unwantedHosts) throws UnknownEntityException, ServiceException;

	/**
	 * gets the set of names of unwanted hosts
	 * @return the set of names of unwanted hosts for this client
	 */
	public Set<String> getUnwantedHosts();

}
