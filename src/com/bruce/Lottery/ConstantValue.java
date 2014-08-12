package com.bruce.Lottery;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 10:14.
 * 常亮接口类
 */
public interface ConstantValue {
    /**
     * 编码 UTF-8
     */
    String ENCODING = "UTF-8";

    /**
     * 代理ID
     */
    String AGENTERID = "889931";
    /**
     * 信息来源（android）
     */
    String SOURCE = "ivr";
    /**
     * body里面的加密算法
     */
    String COMPRESS = "DES";

    /**
     * 子代理商的密钥(.so) JNI
     */
    String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";

    /**
     * des加密用密钥
     */
    String DES_PASSWORD = "9b2648fcdfbad80f";
    /**
    /**
     * 服务器地址
     */
    String LOTTERY_URI = "http://192.168.22.189:8080/Entrance";

}
