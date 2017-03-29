package chapter5.synchronizer.semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

/**
 * Created by localadmin on 3/29/2017.
 */
public class BoundedHashSet<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoundedHashSet.class);
    private final Set<T> set;
    private final Semaphore sem;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<T>());
        sem = new Semaphore(bound);
    }

    private static void startThreadAdd(BoundedHashSet<Integer> boundedHashSet, int n) {
        Thread[] threads = new Thread[n];
        IntStream.range(0, n)/*.parallel()*/.forEach(thread -> new Thread(() -> {
            IntStream.range(0, 10)/*.parallel()*/.forEach(value -> {
                try {
                    boundedHashSet.add(value);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }).start());
    }

    private static void startThreadRemove(BoundedHashSet<Integer> boundedHashSet, int n) {
        Thread[] threads = new Thread[n];
        IntStream.range(0, n)/*.parallel()*/.forEach(thread -> new Thread(() -> {
            IntStream.range(0, 10)/*.parallel()*/.forEach(value -> {
                boundedHashSet.remove(value);
            });
        }).start());
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedHashSet<Integer> boundedHashSet = new BoundedHashSet<>(10);
        BoundedHashSet.startThreadAdd(boundedHashSet, 3);
//        BoundedHashSet.startThreadRemove(boundedHashSet, 3);

    }

    public boolean add(T o) throws InterruptedException {
        LOGGER.info("{} run, begin acquire a permit", Thread.currentThread().getName());
        sem.acquire();
        LOGGER.info("{} run, had acquire a permit", Thread.currentThread().getName());

        boolean wasAdded = false;
        try {
            wasAdded = set.add(o);
            LOGGER.info("{} run, add value:{}", Thread.currentThread().getName(), set.toString());
            return wasAdded;
        } finally {
            if (!wasAdded) {
                LOGGER.info("{} run, release a permit", Thread.currentThread().getName());
                sem.release();
            }
        }
    }

    public boolean remove(Object o) {
        System.out.println("was remove");
        boolean wasRemoved = set.remove(o);
        if (wasRemoved)
            sem.release();
        return wasRemoved;
    }
}
