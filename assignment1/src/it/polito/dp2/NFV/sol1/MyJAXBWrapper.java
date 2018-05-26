package it.polito.dp2.NFV.sol1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.SchemaFactoryConfigurationError;

import org.xml.sax.SAXException;

public class MyJAXBWrapper {
	
	protected static void marshallerSetSchema( Marshaller m, String schemaFileName ) 
			throws SAXException, JAXBException, IllegalArgumentException, 
		       	   NullPointerException, SchemaFactoryConfigurationError,
			       UnsupportedOperationException, FileNotFoundException {
		
		if ( ( m == null ) || ( schemaFileName == null ) )
			throw new NullPointerException("Null argument");

		
		StreamSource source = new StreamSource( new FileInputStream( schemaFileName ) );
		SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
		Schema schema = sf.newSchema( source );			

		m.setSchema(schema);
		m.setEventHandler(
				new ValidationEventHandler() {
					@Override
					public boolean handleEvent(ValidationEvent event) {
						if (event.getSeverity() != ValidationEvent.WARNING) {
	                        ValidationEventLocator vel = event.getLocator();
	                        System.out.println("Line:Col[" + vel.getLineNumber() + ":" +
	                                           vel.getColumnNumber() + "]:" + event.getMessage());
	                    }
						return true;
					}
				}
				);
		
	}
	
	protected static void unmarshallerSetSchema( Unmarshaller um, String schemaFileName ) 
			throws SAXException, JAXBException, IllegalArgumentException, 
			       NullPointerException, SchemaFactoryConfigurationError,
				   UnsupportedOperationException, FileNotFoundException {
		
		if ( ( um == null ) || ( schemaFileName == null ) )
			throw new NullPointerException("Null argument");
		

		StreamSource source = new StreamSource( new FileInputStream( schemaFileName ) );
		SchemaFactory sf    = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema       = sf.newSchema( source );

        um.setSchema(schema);
        um.setEventHandler(
            new ValidationEventHandler() {
                public boolean handleEvent(ValidationEvent ve) {
                    if (ve.getSeverity() != ValidationEvent.WARNING) {
                        ValidationEventLocator vel = ve.getLocator();
                        System.out.println("Line:Col[" + vel.getLineNumber() + ":" +
                                           vel.getColumnNumber() + "]:" + ve.getMessage());
                    }
                    return true;
                }
            }
        );
	}

}
