package thread;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chaodong.xi
 * @date 2020/10/27 11:01 上午
 */
public class FinalFieldTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1111");
        System.out.println(list.toString());
    }
}
