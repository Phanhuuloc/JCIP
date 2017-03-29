package chapter5.cache;

/**
 * Created by localadmin on 3/29/2017.
 */
public interface Computable<K, V> {
    V compute(K arg) throws InterruptedException;
}
