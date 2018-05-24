package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;

public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory {
	
	
	public static NfvReaderFactory newInstance() {	
		return new NfvReaderFactory();
	}
	
	@Override
	public NfvReader newNfvReader() throws NfvReaderException {
		NfvReaderImpl nri;
		
		try {
			
			nri = new NfvReaderImpl();
			
		} catch (Exception e) {
			throw new NfvReaderException();
		}
		
		return nri;
	}

}