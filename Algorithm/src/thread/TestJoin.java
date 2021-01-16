package thread;

import java.util.concurrent.TimeUnit;

/**
 * @author chaodong.xi
 * @date 2020/10/20 1:35 下午
 */
public class TestJoin {
    public static void main(String[] args) {
        Thread thread = new Thread(new JoinThread());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("join finish");
    }
}

class JoinThread implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
