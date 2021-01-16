package JVM;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/11/24 上午11:15
 */
public class HeapTest {
    private byte[] bs = new byte[1024 * 1024];

    public static void main(String[] args) {
        List<HeapTest> tests = new ArrayList<>();
        int number = 0;
        try {
            while (true) {
                tests.add(new HeapTest());
                number++;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("nums = " + number);
        }

    }
}
