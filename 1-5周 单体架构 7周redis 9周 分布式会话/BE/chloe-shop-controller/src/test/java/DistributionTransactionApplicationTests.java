import com.chloe.Application;
import com.chloe.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = Application.class)
class DistributionTransactionApplicationTests {
    @Resource
    private OrderService orderService;

    @Test
    void xaTest() {
        orderService.updateOrderStatus("190827F2R9A6ZT2W", 2);
    }
}
