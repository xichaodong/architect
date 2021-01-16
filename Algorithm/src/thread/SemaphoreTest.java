package thread;

import java.util.concurrent.Semaphore;

/**
 * @author chaodong.xi
 * @date 2020/10/23 5:26 下午
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(10);
        for (int i = 0; i < 30; i++) {
            new Thread(new Task(semaphore, i)).start();
        }
    }

    static class Task implements Runnable {
        private final Semaphore semaphore;
        private final Integer name;

        public Task(Semaphore semaphore, Integer name) {
            this.semaphore = semaphore;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("success " + name);
                semaphore.release(1000);
                System.out.println("release " + name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
