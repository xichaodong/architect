package com.tristeza.distributiontransaction;

import com.tristeza.distributiontransaction.xa.mapper.db70.XA70TestMapper;
import com.tristeza.distributiontransaction.xa.mapper.db71.XA71TestMapper;
import com.tristeza.distributiontransaction.xa.model.XATest;
import com.tristeza.distributiontransaction.xa.service.XATestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
class DistributionTransactionApplicationTests {
    @Resource
    private XATestService xaTestService;

    @Test
    void xaTest() {
        xaTestService.saveXATest();
    }
}
