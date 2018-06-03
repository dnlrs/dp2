package it.polito.dp2.NFV.sol2;

import it.polito.dp2.NFV.lab2.ReachabilityTester;
import it.polito.dp2.NFV.lab2.ReachabilityTesterException;

public class ReachabilityTesterFactory extends it.polito.dp2.NFV.lab2.ReachabilityTesterFactory {

    @Override
    public ReachabilityTester newReachabilityTester() throws ReachabilityTesterException {

        ReachabilityTester r = null;
        try {

            r = new ReachabilityTesterReal();

        } catch (Exception e) {

            e.printStackTrace();
            throw new ReachabilityTesterException(e.getMessage());

        }
        return r;
    }

}
