package thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chaodong.xi
 * @date 2020/7/6 11:17 下午
 */
public class ThreadSafe implements Runnable {
    static final ThreadSafe threadSafe = new ThreadSafe();
    final boolean[] marked = new boolean[100000];
    int index = 0;
    static AtomicInteger realIndex = new AtomicInteger(0);
    static AtomicInteger wrongCount = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(threadSafe);
        Thread thread2 = new Thread(threadSafe);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("表面上的结果: " + threadSafe.index);
        System.out.println("总数是: " + realIndex.get() + " 错误数是: " + wrongCount.get());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            index++;
            realIndex.incrementAndGet();
            synchronized (threadSafe) {
                if (marked[index]) {
                    wrongCount.incrementAndGet();
                    System.out.println("发生错误: " + index);
                }
                marked[index] = true;
            }
        }
    }
}
