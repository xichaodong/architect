package com.tristeza.distributiontransaction.xa.service.impl;

import com.tristeza.distributiontransaction.xa.mapper.db70.XA70TestMapper;
import com.tristeza.distributiontransaction.xa.mapper.db71.XA71TestMapper;
import com.tristeza.distributiontransaction.xa.model.XATest;
import com.tristeza.distributiontransaction.xa.service.XATestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author chaodong.xi
 * @date 2020/8/1 7:53 下午
 */
@Service
public class XATestServiceImpl implements XATestService {
    @Resource
    private XA70TestMapper xa70TestMapper;
    @Resource
    private XA71TestMapper xa71TestMapper;

    @Transactional(transactionManager = "xaTransaction")
    public void saveXATest() {
        XATest xaTest = new XATest();
        xaTest.setId(2);
        xaTest.setName("tristeza");
        System.out.println(System.currentTimeMillis());
        xa70TestMapper.insertXATest(xaTest);
//        xa71TestMapper.insertXATest(xaTest);
        System.out.println(System.currentTimeMillis());
    }
}
