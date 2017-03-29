package chapter5.synchronizer.latch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by localadmin on 3/28/2017.
 */
public class TestHarness {
    public static long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; i++) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
            };
            t.start();
        }
        long start = System.currentTimeMillis();
        startGate.countDown();
        endGate.await();
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static void main(String[] args) {
        try {
            Random random = new Random();
            long millis = timeTasks(6, () -> System.out.println(Thread.currentThread()));
            System.out.println(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
