package com.tristeza.distributiontransaction.xa.mapper.db71;

import com.tristeza.distributiontransaction.xa.model.XATest;

import java.util.List;

public interface XA71TestMapper {
    List<XATest> loadAllXATest();

    Long insertXATest(XATest xaTest);
}