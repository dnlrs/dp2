package it.polito.dp2.NFV.sol3.client2;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;

/**
 * An implementation of the {@link VNFTypeReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealVNFType extends RealNamedEntity implements VNFTypeReader {


    private final FunctionalType functionalType;
    private final Integer  requiredMemory;
    private final Integer  requiredStorage;

    // constructors

    protected RealVNFType(
            String name, FunctionalType functionalType,
            int requiredMemory, int requiredStorage )
                    throws NullPointerException, IllegalArgumentException {

        super( name );

        /*
         * Checks
         */
        if ( functionalType == null )
            throw new IllegalArgumentException(
                    "new VNFType: null argument" );

        boolean functionalTypeValid = false;
        for ( FunctionalType type : FunctionalType.values() )
            if ( type.value().equals(functionalType.value()) ) {
                functionalTypeValid = true;
                break;
            }
        if ( !functionalTypeValid )
            throw new IllegalArgumentException(
                    "new VNFType: invalid enum" );

        if ( requiredMemory < 0 )
            throw new IllegalArgumentException(
                    "new VNFType: memory cannot be less than 0" );

        if ( requiredStorage < 0 )
            throw new IllegalArgumentException(
                    "new VNFType: storage cannot be less than 0" );


        this.functionalType  = functionalType;
        this.requiredMemory  = new Integer( requiredMemory );
        this.requiredStorage = new Integer( requiredStorage );
    }



    // getters



    @Override
    public FunctionalType getFunctionalType() {
        return this.functionalType;
    }

    @Override
    public int getRequiredMemory() {
        return this.requiredMemory.intValue();
    }

    @Override
    public int getRequiredStorage() {
        return this.requiredStorage.intValue();
    }
}
