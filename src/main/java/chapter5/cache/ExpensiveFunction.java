package chapter5.cache;

import java.math.BigInteger;

/**
 * Created by localadmin on 3/29/2017.
 */
public class ExpensiveFunction implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) throws InterruptedException {
        return new BigInteger(arg);
    }
}
