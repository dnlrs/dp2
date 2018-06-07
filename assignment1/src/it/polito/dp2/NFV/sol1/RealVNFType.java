package it.polito.dp2.NFV.sol1;

import java.util.concurrent.atomic.AtomicInteger;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;

/**
 * An implementation of the {@link VNFTypeReader} interface.
 *
 * @author    Daniel C. Rusu
 * @studentID 234428
 */
public class RealVNFType extends RealNamedEntity implements VNFTypeReader {


    private FunctionalType functionalType;
    private AtomicInteger  requiredMemory;
    private AtomicInteger  requiredStorage;

    private final Object lockFunctionalType = new Object();


    // constructors


    protected RealVNFType(
            String name, FunctionalType functionalType,
            int requiredMemory, int requiredStorage )
                    throws NullPointerException, IllegalArgumentException {

        super( name );
        this.setFunctionalType( functionalType );
        this.setRequiredMemory( requiredMemory );
        this.setRequiredStorage( requiredStorage );
    }



    // getters



    @Override
    public FunctionalType getFunctionalType() {
        synchronized ( this.lockFunctionalType ) {
            return this.functionalType;
        }
    }

    @Override
    public int getRequiredMemory() {
        return this.requiredMemory.intValue();
    }

    @Override
    public int getRequiredStorage() {
        return this.requiredStorage.intValue();
    }


    // setters


    protected void setFunctionalType( FunctionalType functionalType )
            throws IllegalArgumentException {

        if ( functionalType == null )
            throw new IllegalArgumentException( "setFunctionalType: null argument" );

        boolean functionalTypeValid = false;
        for ( FunctionalType type : FunctionalType.values() )
            if ( type.value().equals(functionalType.value()) ) {
                functionalTypeValid = true;
                break;
            }

        if ( !functionalTypeValid )
            throw new IllegalArgumentException( "setFunctionalType: invalid enum" );

        synchronized ( this.lockFunctionalType ) {
            this.functionalType = functionalType;
        }
    }



    protected void setRequiredMemory( int requiredMemory )
            throws IllegalArgumentException {

        if ( requiredMemory < 0 )
            throw new IllegalArgumentException( "setRequiredMemory: memory cannot be less than 0" );

        this.requiredMemory = new AtomicInteger( requiredMemory );
    }



    protected void setRequiredStorage(Integer requiredStorage)
            throws IllegalArgumentException {

        if ( requiredStorage < 0 )
            throw new IllegalArgumentException( "setRequiredStorage: storage cannot be less than 0" );

        this.requiredStorage = new AtomicInteger( requiredStorage );
    }

}
