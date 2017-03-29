package chapter5.synchronizer.barriers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

/**
 * Created by localadmin on 3/29/2017.
 */
public class App {
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public App() {
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, () -> System.out.println("all meet"));
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++)
            workers[i] = new Worker(i);
    }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    public void start() {
        for (int i = 0; i < workers.length; i++)
            new Thread(workers[i]).start();
    }

    class Worker implements Runnable {
        private final int threat;

        public Worker(int i) {
            this.threat = i;
        }

        @Override
        public void run() {
            IntStream.range(0, 10)
                    .filter(value -> value % 2 == 0 && value < 5)
                    .parallel()
                    .forEach(i -> System.out.println(String.format("worker run on thread %d with value %d", threat, i))
                    );
            try {
                barrier.await();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (BrokenBarrierException ex) {
                ex.printStackTrace();
            }
        }
    }
}
