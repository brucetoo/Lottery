package com.bruce.Lottery.net.protocol;

import com.bruce.Lottery.ConstantValue;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 10:43.
 * Header节点的处理，里面包含都是叶子节点
 */
public class Header {

    // <agenterid>889931</agenterid>
    private Leaf agenterid = new Leaf("agenterid", ConstantValue.AGENTERID);

    // <source>ivr</source>
    private Leaf source = new Leaf("source", ConstantValue.SOURCE);

    // <compress>DES</compress>
    private Leaf compress = new Leaf("compress", ConstantValue.COMPRESS);
    //
    // <messengerid>20131013101533000001</messengerid>
    private Leaf messengerid = new Leaf("messengerid");

    // <timestamp>20131013101533</timestamp>
    private Leaf timestamp = new Leaf("timestamp");

    // <digest>7ec8582632678032d25866bd4bce114f</digest>
    private Leaf digest = new Leaf("digest");
    //
    // <transactiontype>12002</transactiontype>
    private Leaf transactiontype = new Leaf("transactiontype");

    // <username>13200000000</username>
    private Leaf username = new Leaf("username");


    /**
     * 序列化header 和 每个 叶子
     */

    public void serializeHeader(XmlSerializer serializer, String body) {
        //timestamp 时间戳， messengerid由 时间戳+六位的随机数表示

        //设置时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = format.format(new Date());
        timestamp.setTagValue(time);
        //设置messengerid

        Random random = new Random();
        int num = random.nextInt(999999) + 1;//[0,999999)->[1,99999]

        //数字格式化的类  0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置
        DecimalFormat decimalFormat = new DecimalFormat("000000");//取六位整数，不足用0补齐
        messengerid.setTagValue(time + decimalFormat.format(num));

        //设置digest 由时间戳+密码+body
        String oriInfo = time + ConstantValue.AGENTER_PASSWORD + body; //该body是谁调用该方法就传入值
        //调用加密包
        String md5 = DigestUtils.md5Hex(oriInfo);
        digest.setTagValue(md5);

        //transactiontype和username在Header设置中没法进行初始化，只能在后面谁调用的时候进行去设置
        //因此抛出get 方法，然后去调用对应的set方法
        try {
            serializer.startTag(null, "header");
            agenterid.serializeLeaf(serializer);
            source.serializeLeaf(serializer);
            compress.serializeLeaf(serializer);

            messengerid.serializeLeaf(serializer);
            timestamp.serializeLeaf(serializer);
            digest.serializeLeaf(serializer);

            transactiontype.serializeLeaf(serializer);
            username.serializeLeaf(serializer);
            serializer.endTag(null, "header");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Leaf getTransactiontype() {
        return transactiontype;
    }

    public Leaf getUsername() {
        return username;
    }

}
