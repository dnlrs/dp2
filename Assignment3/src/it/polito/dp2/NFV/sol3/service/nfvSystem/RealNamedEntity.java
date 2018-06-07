package it.polito.dp2.NFV.sol3.service.nfvSystem;

import it.polito.dp2.NFV.NamedEntityReader;

/**
 * An implementation of the {@link NamedEntityReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealNamedEntity implements NamedEntityReader {

    private final static String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*";

    private String name;


    protected RealNamedEntity( String name ) {
        this.setName( name );
    }

    @Override
    public String getName() {
        synchronized ( this.name ) {
            return new String ( this.name );
        }
    }

    protected void setName( String name )
            throws IllegalArgumentException {

        if ( name == null )
            throw new IllegalArgumentException( "setName: null argument" );

        if ( !( name.matches( NAME_REGEX ) ) )
            throw new IllegalArgumentException( "setName: invalid name" );

        synchronized ( this.name ) {
            this.name = new String( name );
        }
    }

    public static boolean nameIsValid( String name ) {
        if ( name == null )
            return false;

        return name.matches( NAME_REGEX );
    }
}
