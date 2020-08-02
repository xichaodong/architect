package com.tristeza.distributiontransaction.xa.mapper.db70;

import com.tristeza.distributiontransaction.xa.model.XATest;

import java.util.List;

public interface XA70TestMapper {
    List<XATest> loadAllXATest();

    Long insertXATest(XATest xaTest);
}