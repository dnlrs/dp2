package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NamedEntityReader;

public class NamedEntityReaderReal implements NamedEntityReader {
	
	private String name;
	
	protected void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

}
