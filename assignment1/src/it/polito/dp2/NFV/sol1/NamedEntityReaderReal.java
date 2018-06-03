package it.polito.dp2.NFV.sol1;

import it.polito.dp2.NFV.NamedEntityReader;

/**
 * An implementation of the  {@link NamedEntityReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class NamedEntityReaderReal implements NamedEntityReader {

	private String name;

	protected void setName( String name ) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
