package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;

public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory {
	
	
	public static NfvReaderFactory newInstance() {	
		return new NfvReaderFactory();
	}
	
	@Override
	public NfvReader newNfvReader() throws NfvReaderException {
		NfvReaderReal realNfvReader;
		
		try {
			
			realNfvReader = new NfvReaderReal();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new NfvReaderException();
		}
		
		return realNfvReader;
	}

}