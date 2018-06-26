package it.polito.dp2.NFV.sol3.client2;

import java.util.regex.PatternSyntaxException;

import it.polito.dp2.NFV.NamedEntityReader;

/**
 * An implementation of the {@link NamedEntityReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealNamedEntity implements NamedEntityReader {

    private final static String NAME_REGEX = "[a-zA-Z][a-zA-Z0-9]*";

    private final String name;


    protected RealNamedEntity( String name ) {

        /*
         * Checks
         */
        if ( name == null )
            throw new IllegalArgumentException( "setName: null argument" );

        if ( !( name.matches( NAME_REGEX ) ) )
            throw new IllegalArgumentException( "setName: invalid name" );

        this.name = new String( name );
    }

    @Override
    public String getName() {
        return new String ( this.name );
    }

    public static boolean nameIsValid( String name ) {
        if ( name == null )
            return false;
        try {
            return name.matches( NAME_REGEX );
        } catch ( PatternSyntaxException e ) {
            return false;
        }
    }
}
