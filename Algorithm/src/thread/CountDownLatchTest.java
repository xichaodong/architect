package thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/10/23 5:00 下午
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Task(countDownLatch, "thread 1", 5)).start();
        new Thread(new Task(countDownLatch, "thread 2", 10)).start();
        countDownLatch.await();
        System.out.println("all finish");
    }

    static class Task implements Runnable {
        private final CountDownLatch countDownLatch;
        private final String name;
        private final Integer sleepTime;

        public Task(CountDownLatch countDownLatch, String name, Integer sleepTime) {
            this.countDownLatch = countDownLatch;
            this.name = name;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
                System.out.println(name + " finish");
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
