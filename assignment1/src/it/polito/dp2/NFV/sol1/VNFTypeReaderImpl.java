package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol1.jaxb.VNF;

public class VNFTypeReaderImpl implements VNFTypeReader {
	
	private VNF vnf;
	
	protected VNFTypeReaderImpl() {}
	
	protected VNFTypeReaderImpl( VNF v ) {
		this.vnf = v;		
	}
	
	protected void setVNF( VNF v ) {
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
