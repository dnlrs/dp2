package it.polito.dp2.NFV.sol1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.SchemaFactoryConfigurationError;

import it.polito.dp2.NFV.NfvReaderException;
import it.polito.dp2.NFV.sol1.jaxb.NFVSystemType;

/**
 * Serializes into an XML file passed as first argument all the data accessible
 * through the NVFSystem interfaces.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvInfoSerializer {

	private static final String HOW_TO_USE           = "use: java <program name> <output XML file name>";

	private static final String JAXB_CLASSES_PACKAGE = "it.polito.dp2.NFV.sol1.jaxb";

	private static final String TARGET_NAMESPACE     = "http://www.example.org/nfvInfo";
	private static final String SCHEMA_LOCATION      = "/xsd/nfvInfo.xsd";

	private static final String PROPERTY_USER_DIR    = "user.dir";

	private XMLBuilder builder;

	/**
	 * Class constructor.
	 *
	 * @throws NfvReaderException
	 */
	public NfvInfoSerializer() throws NfvReaderException {
		this.builder = new XMLBuilder(); // note: the builder class instantiates the NfvReaderFactory
	}


	/**
	 * Main entry point.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		// Check arguments --------------------------------------------------
		if ( ( args.length == 0 ) || ( args.length > 1 ) ) {
			System.err.println( "ERROR: " + HOW_TO_USE );
			System.exit(-1);
		}

		// Instantiate data generator and get work done ---------------------
		try {

			NfvInfoSerializer nis = new NfvInfoSerializer();
			nis.doWork( args[0] );

		} catch (NfvReaderException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}



	/**
	 * Retrieves data from NFVSystem interfaces and marshalls it to the output
	 * XML file.
	 *
	 * @param outputFileName the output XML file name
	 * @throws JAXBException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 * @throws DataBindingException
	 * @throws NfvReaderException
	 */
	private void doWork( String outputFileName )
			throws JAXBException, NullPointerException, IllegalArgumentException,
                   FileNotFoundException, DataBindingException, NfvReaderException {

		if ( outputFileName == null )
			throw new NullPointerException( "doWork: output file name cannot be null" );

		// read data to marshal from interfaces -----------------------------
		NFVSystemType nfvSystem = this.builder.buildNFVSystemType();

		// marshal data -----------------------------------------------------
		JAXBContext jaxbContext = JAXBContext.newInstance( JAXB_CLASSES_PACKAGE );
		Marshaller  marshaller  = jaxbContext.createMarshaller();

		// set marshaller properties ------------------------------------
		try {

		    String schemaFile = ( System.getProperty( PROPERTY_USER_DIR ) == null
		            ? new String("") : System.getProperty(PROPERTY_USER_DIR) );

		    schemaFile = schemaFile.concat( SCHEMA_LOCATION );

			JAXBUtils.marshallerSetSchema(marshaller, schemaFile);

			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			String schemaLocationString = new String( TARGET_NAMESPACE + " " + SCHEMA_LOCATION );
			marshaller.setProperty( Marshaller.JAXB_SCHEMA_LOCATION, schemaLocationString );

		} catch ( SchemaFactoryConfigurationError sfce ) {
			System.err.println(sfce);
			System.err.println("Could not set unmarshaller validation schema."); // no validation
		} catch ( Exception e ) {
			System.err.println( e.getMessage() );
			System.err.println("could not set marshaller properties.");
		}


		// marshal ------------------------------------------------------
		JAXBElement<NFVSystemType> nfvRootElement = this.builder.getRootElement( nfvSystem );

		if ( nfvRootElement == null )
			throw new NullPointerException("nfvRootElement is null.");

		JAXB.marshal( nfvRootElement, new FileOutputStream( outputFileName ) );
	}
}
