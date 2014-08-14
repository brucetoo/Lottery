package com.bruce.Lottery.engine;

import com.bruce.Lottery.net.protocol.Message;

/**
 * Created by Bruce
 * Data 2014/8/14
 * Time 17:38.
 */
public interface CommonInfoEngine {

    /**
     * 获取当前彩票种类信息
     * @return
     * Integer 彩票标示
     */
    Message getCurrentIssueInfo(Integer param);
}
