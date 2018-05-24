package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;

public class VNFTypeReaderImpl implements VNFTypeReader {
	
	private String name;
	private int requiredMemory;
	private int requiredStorage;
	private FunctionalType functionctionalType;
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public FunctionalType getFunctionalType() {
		return functionctionalType;
	}

	public FunctionalType getFunctionctionalType() {
		return functionctionalType;
	}

	public void setFunctionctionalType(FunctionalType functionctionalType) {
		this.functionctionalType = functionctionalType;
	}

	@Override
	public int getRequiredMemory() {
		return requiredMemory;
	}

	public void setRequiredMemory(int requiredMemory) {
		this.requiredMemory = requiredMemory;
	}

	@Override
	public int getRequiredStorage() {
		return requiredStorage;
	}

	public void setRequiredStorage(int requiredStorage) {
		this.requiredStorage = requiredStorage;
	}

}
