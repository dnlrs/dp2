package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

/**
 * An implementation of the {@link VNFTypeReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class VNFTypeReaderReal implements VNFTypeReader {

	private VNF vnf;


	protected VNFTypeReaderReal( VNF v ) throws NullPointerException {
		if ( v == null )
			throw new NullPointerException("Null argument.");

		this.vnf = v;
	}


	@Override
	public String getName() {
		return vnf.getName();
	}

	@Override
	public FunctionalType getFunctionalType() {
		return FunctionalType.fromValue( vnf.getFunctionalType().value() );
	}

	@Override
	public int getRequiredMemory() {
		return vnf.getRequiredMemory().getValue().intValue();
	}

	@Override
	public int getRequiredStorage() {
		return vnf.getRequiredStorage().getValue().intValue();
	}
}
