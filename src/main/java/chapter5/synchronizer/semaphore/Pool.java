package chapter5.synchronizer.semaphore;

import java.util.concurrent.Semaphore;

/**
 * Created by localadmin on 3/29/2017.
 */
public class Pool {
    private static final int MAX_AVAILABLE = 2;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
    protected Object[] items = new String[]{"a", "b", "c"}; //... whatever kinds of items being managed
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    // Not a particularly efficient data structure; just for demo

    public static void main(String[] args) throws InterruptedException {
        Pool pool = new Pool();
        pool.getItem();
        pool.putItem("a");
    }

    public void getItem() throws InterruptedException {
        available.acquire();
        getNextAvailableItem();
    }

    public void putItem(Object x) {
        if (markAsUnused(x))
            available.release();
    }

    protected synchronized void getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                System.out.println(items[i]);
            }
        }
    }

    protected synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                System.out.println("release");
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }
}
