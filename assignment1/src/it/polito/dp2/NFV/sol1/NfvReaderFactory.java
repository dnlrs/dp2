package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;

/**
 * Defines a factory API that enables applications to obtain one or more
 * objects implementing the NfvReader interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory {


	public static NfvReaderFactory newInstance() {
		return new NfvReaderFactory();
	}



	@Override
	public NfvReader newNfvReader() throws NfvReaderException {

	    NfvSystem nfvSystem = null;
		try {

			 nfvSystem = new NfvSystem();

		} catch (NfvReaderException e) {
			System.err.println( e.getMessage() );
			e.printStackTrace();
			throw new NfvReaderException();
		}
	    return nfvSystem;
	}


}