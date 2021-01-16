package thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/10/23 5:00 下午
 */
public class NoCountDownLatchTest {
    public static void main(String[] args) {
        new Thread(new Task("thread 1", 5)).start();
        new Thread(new Task("thread 2", 10)).start();
        System.out.println("all finish");
    }

    static class Task implements Runnable {
        private final String name;
        private final Integer sleepTime;

        public Task(String name, Integer sleepTime) {
            this.name = name;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
                System.out.println(name + " finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
