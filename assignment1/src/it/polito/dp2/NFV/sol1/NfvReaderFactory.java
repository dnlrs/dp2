package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NfvReader;
import it.polito.dp2.NFV.NfvReaderException;

public class NfvReaderFactory extends it.polito.dp2.NFV.NfvReaderFactory {
	
	
	public static NfvReaderFactory newInstance() {
		NfvReaderFactory nrf = new NfvReaderFactory();
		
		return nrf;
	}
	
	@Override
	public NfvReader newNfvReader() throws NfvReaderException {
		
		return new NfvReaderImpl();
	}

}