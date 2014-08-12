package com.bruce.Lottery.engine;

import com.bruce.Lottery.bean.User;
import com.bruce.Lottery.net.protocol.Message;

/**
 * Created by Bruce
 * Data 2014/8/12
 * Time 14:25.
 * 请求业务操作的接口
 */
public interface UserEngine {

    /**
     * 用户登录
     * @param user
     * @return
     */
    Message login(User user);
}
