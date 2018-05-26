package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.SchemaFactoryConfigurationError;

import org.xml.sax.SAXException;

import it.polito.dp2.NFV.ConnectionPerformanceReader;
import it.polito.dp2.NFV.HostReader;
import it.polito.dp2.NFV.LinkReader;
import it.polito.dp2.NFV.NffgReader;
import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.NodeReader;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.*;

/**
 * This class implements the {@link NvfReader} interface.
 * <p>
 * <b>Note</b>: 
 * This class maintains a set of databases with all possible interfaces in 
 * order to improve memory use, i.e. if the library user asks several times 
 * for the same interface the same object will be returned every time.
 * Databases are built when class is created, after the unmarshalling.
 * </p>
 * 
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvReaderReal implements NfvReader {
	
	private static final String JAXB_CLASSES_PACKAGE = "it.polito.dp2.NFV.sol1.jaxb";
	private static final String SCHEMA_LOCATION      = "/xsd/nfvInfo.xsd";

	private static final String PROPERTY_XML_FILE    = "it.polito.dp2.NFV.sol1.NfvInfo.file";
	private static final String PROPERTY_USER_DIR    = "user.dir"; // working directory
	
	private final NFVSystemType nfvSystem;
	private final Adapter       adapter;
	
	
	protected NfvReaderReal() 
			throws JAXBException, NullPointerException, FileNotFoundException, 
			       IllegalArgumentException, NfvReaderException {
		
		JAXBContext jc  = JAXBContext.newInstance( JAXB_CLASSES_PACKAGE );
		Unmarshaller um = jc.createUnmarshaller();
		
		// Set up validation
        try {

        	String schemaFile = ( System.getProperty( PROPERTY_USER_DIR ) == null ? 
        			                     new String("") : System.getProperty(PROPERTY_USER_DIR) );
        	schemaFile = schemaFile.concat(SCHEMA_LOCATION);
        	
        	myJAXBWrapper.unmarshallerSetSchema(um, schemaFile);
        
        } catch ( Exception e ) {
        	System.err.println(e); 
            System.err.println("Could not set unmarshaller validation schema."); // no validation
        } catch ( SchemaFactoryConfigurationError sfce ) {
        	System.err.println(sfce); 
        	System.err.println("Could not set unmarshaller validation schema."); // no validation
        }
        
		
        // get XML file to load 
		String xmlFile = System.getProperty( PROPERTY_XML_FILE );
		if ( xmlFile == null )
			throw new NullPointerException("Could not get XML file to read.");
        
        
        //unmarshal
		Object obj = um.unmarshal( new FileInputStream( xmlFile ) );

		@SuppressWarnings("unchecked")
		JAXBElement<NFVSystemType> element = (JAXBElement<NFVSystemType>) obj;
		
		nfvSystem = element.getValue();
		
		if ( nfvSystem == null )
			throw new NullPointerException("Null Element.");
		
		
		adapter = new Adapter( nfvSystem );
		adapter.init();
	}
	
	
	
	
	/**
	 * Retrieves a ConnectionPerformanceReader if one exists between hosts
	 * passed as arguments.
	 * 
	 * @param  sourceHost source Host end point of connection
	 * @param  destHost   destination Host end point of connection
	 * @return            a new ConnectionPerformanceReader interface, null if
	 *                    a connection doesn't exist or an error occurred
	 */
	@Override
	public ConnectionPerformanceReader 
        getConnectionPerformance(HostReader sourceHost, HostReader destHost ) {
		
		if ( sourceHost == null || destHost == null )
			return null; // wrong arguments
		
		String ssourceHostName = sourceHost.getName();
		String destHostName    = destHost.getName();
		
		if ( ( ssourceHostName == null ) || ( destHostName == null ) )
			return null; // source or destination hosts don't have a name
		
		return adapter.getConnectionPerformance( ssourceHostName + "-" + destHostName );
	}

	
	
	/**
	 * Retrieves a HostReader if one exists with the name passed as argument.
	 * 
	 * @param  hostName host name to be retrieved
	 * @return          a HostReader interface, null if host doesn't exist
	 */
	@Override
	public HostReader getHost(String hostName) {
		if ( hostName == null )
			return null; // wrong parameter
		
		return adapter.getHost( hostName );
	}

	
	/**
	 * Retrieves the set of all hosts present in the NFV System.
	 * 
	 * @return a set of interfaces to read all hosts in the NFV System
	 */
	@Override
	public Set<HostReader> getHosts() {
		return adapter.getHosts();
	}

	
	/**
	 * gives access to a single NFFG given its name.
	 * 
	 * @param nffgName the NFFG name
	 * @return         an interface for reading NFFG's data
	 */
	@Override
	public NffgReader getNffg( String nffgName ) {
		
		if ( nffgName == null )
			return null;
		
		return adapter.getNFFG( nffgName );
	}

	
	
	/**
	 * Retrieves a set of interfaces to read all NFFG deployed after the date
	 * passed as argument. If date is null, all NFFGs interfaces are returned.
	 * 
	 * @param  date start of time
	 * @return      
	 */
	@Override
	public Set<NffgReader> getNffgs(Calendar date) {
		return adapter.getNFFGs( date );
	}

	
	
	/**
	 * Retrieves the VNF Catalogue.
	 * 
	 * @return the set of interfaces for all VNF functions
	 */
	@Override
	public Set<VNFTypeReader> getVNFCatalog() {
		return adapter.getVNFCatalog();
	}

	
	
	/**
	 * Retrieves the interface to access a specific VNF given its name.
	 * 
	 * @param vnfName VNF's name
	 * @return        a interface to access the VNF
	 */
	protected VNFTypeReader getVNF(String vnfName) {
		if ( vnfName == null )
			return null;
		
		return adapter.getVNF( vnfName ); 
	}
	
	
	/**
	 * Retrieves a set of interfaces to links given their names.
	 * 
	 * @param  nffgName the NFFG's name
	 * @param  links    the links names to be retrieved
	 * @return          a set of interfaces to links
	 */
	protected Set<LinkReader> getLinks( String nffgName, Set<String> links ) {
		if ( ( nffgName == null ) || ( links == null ) )
			return new HashSet<LinkReader>();
		
		return adapter.getLinks( nffgName, links );
	}
	
	/**
	 * Returns a interface to a node, given its name.
	 * 
	 * @param  nodeName the node's name
	 * @return          an interface to the node requested
	 */
	protected NodeReader getNode( String nodeName ) {
		if ( nodeName == null )
			return null;
		
		return adapter.getNode( nodeName ); 
	}
	
	/**
	 * Retrieves a set of interfaces to nodes given their names.
	 * 
	 * @param  nodes nodes name to retrieve interfaces to
	 * @return       a set of interfaces to nodes
	 */
	protected Set<NodeReader> getNodes( Set<String> nodes ) {
		if ( nodes == null )
			return new HashSet<NodeReader>();
		
		return  adapter.getNodes( nodes );
	}
	
}
