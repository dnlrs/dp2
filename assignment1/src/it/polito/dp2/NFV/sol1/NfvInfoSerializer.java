package it.polito.dp2.NFV.sol1;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
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
	
	private Builder builder;
	
	/**
	 * Class constructor.
	 * 
	 * @param oxml
	 * @throws NfvReaderException
	 */
	public NfvInfoSerializer() throws NfvReaderException {
		builder = new Builder();
	}

	
	/**
	 * Main entry point.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Check arguments --------------------------------------------------
		if ( ( args.length == 0 ) || ( args.length > 1 ) ) {
			System.out.println("ERROR:" + HOW_TO_USE );
			System.exit(-1);
		}

		// Instantiate data generator and get work done ---------------------
		try {

			NfvInfoSerializer nis = new NfvInfoSerializer();
			nis.do_work( args[0] );

		} catch (NfvReaderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.exit(0);
	}

	
	
	/**
	 * Retrieves data from NFVSystem interfaces and marshalls it to the output
	 * XML file.
	 */
	private void do_work( String outputFileName ) throws Exception {
		
		if ( outputFileName == null )
			throw new NullPointerException("Output file name cannot be null.");
			
		// read data to marshal from interfaces -----------------------------
		NFVSystemType nfvSystem = builder.buildNFVSystemType();
				
		// marshal data -----------------------------------------------------
		try {
			
			JAXBContext jc = JAXBContext.newInstance( JAXB_CLASSES_PACKAGE );
			Marshaller  m  = jc.createMarshaller();
			
			// set marshaller properties ------------------------------------
			try {
				
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				setMarshallerSchema(m);

			} catch ( PropertyException pre ) { 
				pre.printStackTrace(); 
			} catch ( IllegalArgumentException iae ) {
				iae.printStackTrace();
			}
		
			// marshal ------------------------------------------------------
			JAXBElement<NFVSystemType> nfvRootElement = builder.getRootElement(nfvSystem);
			
			if ( nfvRootElement == null )
				throw new NullPointerException("nfvRootElement is null.");
			
			JAXB.marshal(nfvRootElement, new FileOutputStream(outputFileName) );
		
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new Exception(e);
//		} catch ( JAXBException jbe ) { 
//			jbe.printStackTrace();
//			System.exit(-1); 
//		} catch ( DataBindingException dbe ) {
//			dbe.printStackTrace();
//			System.exit(-1);
//		} catch ( FileNotFoundException fne ) {
//			fne.printStackTrace(); 
//			System.exit(-1);
//		} catch ( SecurityException see ) { 
//			see.printStackTrace();     
//			System.exit(-1);
        }
	}
	
	
	/**
	 * Sets validation schema for the marshaller assuming it's:
	 * {working_directory}/xsd/nfvInfo.xsd
	 * 
	 * @param  m the marshaller
	 *  
	 * @throws PropertyException
	 */
	private void setMarshallerSchema( Marshaller m ) throws PropertyException {
		
		// retrieve validation schema to be used ----------------------------
		try {
			
			// validation schema can be found under "${user.dir}/xsd/" ------
			String schemaFile = System.getProperty( PROPERTY_USER_DIR );
			
			if ( schemaFile == null )
				throw new PropertyException("Could not set marshaller schema property");
		
			
			schemaFile = schemaFile.concat( SCHEMA_LOCATION );
			
			SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			Schema schema = sf.newSchema( new File( schemaFile ) );
			
			
			m.setProperty( Marshaller.JAXB_SCHEMA_LOCATION, new String(TARGET_NAMESPACE+" "+schemaFile) );				

			m.setSchema(schema);
			m.setEventHandler(
					new ValidationEventHandler() {
						@Override
						public boolean handleEvent(ValidationEvent event) {
							
							ValidationEventLocator vel = event.getLocator();
							
							System.out.println("Line:Col[" + 
									vel.getLineNumber() + ":" + 
									vel.getColumnNumber() + "]:" +
									event.getMessage());
							
							if ( event.getSeverity() == ValidationEvent.WARNING )
								return true;
							
							return false; // non-warning level errors stop marshalling
						}
					}
					);
		
		} catch ( Exception e ) {
			throw new PropertyException("Could not set marshaller schema property");
		} catch ( SchemaFactoryConfigurationError sfce ) {
			throw new PropertyException("Could not set marshaller schema property");
		} 
	}
}
