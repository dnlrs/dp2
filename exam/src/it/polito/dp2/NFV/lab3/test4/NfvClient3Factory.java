package it.polito.dp2.NFV.lab3.test4;

import it.polito.dp2.NFV.FactoryConfigurationError;
import it.polito.dp2.NFV.lab3.NfvClientException;

public abstract class NfvClient3Factory {

	private static final String propertyName = "it.polito.dp2.NFV.NfvClient3Factory";
	
	protected NfvClient3Factory() {}
	
	/**
	 * Obtain a new instance of a <tt>NfvClient3Factory</tt>.
	 * 
	 * <p>
	 * This static method creates a new factory instance. This method uses the
	 * <tt>it.polito.dp2.NFV.NfvClient3Factory</tt> system property to
	 * determine the NFVClient3Factory implementation class to load.
	 * </p>
	 * <p>
	 * Once an application has obtained a reference to a
	 * <tt>NfvClient3Factory</tt> it can use the factory to obtain a new
	 * {@link NfvClient3} instance.
	 * </p>
	 * 
	 * @return a new instance of a <tt>NfvClient3Factory</tt>.
	 * 
	 * @throws FactoryConfigurationError if the implementation is not available 
	 * or cannot be instantiated.
	 */
	public static NfvClient3Factory newInstance() throws FactoryConfigurationError {
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if(loader == null) {
			loader = NfvClient3Factory.class.getClassLoader();
		}
		
		String className = System.getProperty(propertyName);
		if (className == null) {
			throw new FactoryConfigurationError("cannot create a new instance of a NfvClient3Factory"
												+ "since the system property '" + propertyName + "'"
												+ "is not defined");
		}
		
		try {
			Class<?> c = (loader != null) ? loader.loadClass(className) : Class.forName(className);
			return (NfvClient3Factory) c.newInstance();
		} catch (Exception e) {
			throw new FactoryConfigurationError(e, "error instantiatig class '" + className + "'.");
		}
	}
	
	
	/**
	 * Creates a new instance of a {@link NfvClient3} implementation.
	 * 
	 * @return a new instance of a {@link NfvClient3} implementation.
	 * @throws NfvClientException if an implementation of {@link NfvClient3} cannot be created.
	 */
	public abstract NfvClient3 newNfvClient3() throws NfvClientException;	
	

}
