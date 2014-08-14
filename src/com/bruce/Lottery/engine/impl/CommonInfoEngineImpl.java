package com.bruce.Lottery.engine.impl;

import com.bruce.Lottery.engine.BaseEngine;
import com.bruce.Lottery.engine.CommonInfoEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.CurrentIssueElement;

/**
 * Created by Bruce
 * Data 2014/8/14
 * Time 17:41.
 */
public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine{

    @Override
    public Message getCurrentIssueInfo(Integer param) {
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue(param.toString());

        return null;
    }
}
