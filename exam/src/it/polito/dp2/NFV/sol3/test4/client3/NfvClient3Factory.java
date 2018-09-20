package it.polito.dp2.NFV.sol3.test4.client3;

import it.polito.dp2.NFV.lab3.NfvClientException;
import it.polito.dp2.NFV.lab3.test4.NfvClient3;

public class NfvClient3Factory extends it.polito.dp2.NFV.lab3.test4.NfvClient3Factory {

	@Override
	public NfvClient3 newNfvClient3() throws NfvClientException {
		
		Client3Exam result = null;
		
		try {
			result = new Client3Exam();
		} catch (Exception e) {
			throw new NfvClientException( e.getMessage() );
		}
	
		return result;
	}

}
