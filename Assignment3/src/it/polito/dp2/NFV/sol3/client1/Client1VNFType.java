package it.polito.dp2.NFV.sol3.client1;

import java.net.URI;

import it.polito.dp2.NFV.FunctionalType;
import it.polito.dp2.NFV.VNFTypeReader;
import it.polito.dp2.NFV.sol3.model.nfvdeployer.NfvVNF;

/**
 * An implementation of the {@link VNFTypeReader} interface that retrieves
 * data from the NfvDeployer Web Service.
 *
 * @author Daniel C. Rusu
 */
public class Client1VNFType implements VNFTypeReader {

    private final URI BASE_URI;
    private final NfvVNF vnf;

    public Client1VNFType( NfvVNF xmlVNF, URI base ) {
        this.vnf      = xmlVNF;
        this.BASE_URI = base;
    }

    @Override
    public String getName() {
        return this.vnf.getName();
    }

    @Override
    public FunctionalType getFunctionalType() {
        return FunctionalType.fromValue( this.vnf.getFunctionalType().value() );
    }

    @Override
    public int getRequiredMemory() {
        return this.vnf.getRequiredMemory();
    }

    @Override
    public int getRequiredStorage() {
        return this.vnf.getRequiredStorage();
    }

}
