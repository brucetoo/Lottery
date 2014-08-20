package com.bruce.Lottery.engine.impl;

import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.bean.ShoppingCart;
import com.bruce.Lottery.bean.Ticket;
import com.bruce.Lottery.bean.User;
import com.bruce.Lottery.engine.BaseEngine;
import com.bruce.Lottery.engine.UserEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.BalanceElement;
import com.bruce.Lottery.net.protocol.element.BetElement;
import com.bruce.Lottery.net.protocol.element.UserLoginElement;
import com.bruce.Lottery.utils.DES;
import com.bruce.Lottery.utils.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by bruce-too
 * on 2014/8/9
 * Time 12:01.
 */
public class UserEngineImpl extends BaseEngine implements UserEngine{
    /**
     * 用户登录
     *
     * @param user
     */
    public Message login(User user) {

        // 第一步：获取到登录用的xml
        // 创建登录用Element
        UserLoginElement element = new UserLoginElement();
        element.getActpastword().setTagValue(user.getPassword());
        //设置用户数据,获取发送请求xml
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());
        String xml = message.getXml(element);

        // 如果第三步比对通过result，否则返回空
        Message result = getResult(xml);

        if (result != null) {

            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容
            XmlPullParser parser = Xml.newPullParser();
            try {
                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServieceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";
                StringReader reader = new StringReader(body);
                parser.setInput(reader);
                int eventType = parser.getEventType();
                String tagName;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            if ("errorcode".equals(tagName)) {
                                result.getBody().getOelement().setErrorode(parser.nextText());
                            }
                            if ("errormsg".equals(tagName)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }

                    }
                    eventType = parser.next();   //下一个标签
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return  null;
    }

    /**
     * 余额请求
     * @param user
     * @return
     */

    @Override
    public Message getBalance(User user) {
        BalanceElement element = new BalanceElement();

        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());
        String xml = message.getXml(element);

        Message result = getResult(xml);

        if (result != null) {

            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容

            XmlPullParser parser = Xml.newPullParser();
            try {

                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServieceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;

                BalanceElement resultElement = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)) {
                                result.getBody().getOelement().setErrorode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }

                            // 正对于当前请求
                            if ("element".equals(name)) {
                                resultElement = new BalanceElement();
                                result.getBody().getElements().add(resultElement);
                            }

                            if ("investvalues".equals(name)) {
                                if (resultElement != null) {
                                    resultElement.setInvestvalues(parser.nextText());
                                }
                            }

                            break;
                    }
                    eventType = parser.next();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    public Message bet(User user) {
        BetElement element = new BetElement();
        element.getLotteryid().setTagValue(ShoppingCart.getInstance().getLotteryid().toString());

        // 彩票的业务里面：
        // ①关于注数的计算
        // ②关于投注信息封装（用户投注号码）

        // 010203040506|01^01020304050607|01

        StringBuffer codeBuffer = new StringBuffer();
        for (Ticket item : ShoppingCart.getInstance().getTickets()) {
            codeBuffer.append("^").append(item.getRedNum().replaceAll(" ", "")).append("|").append(item.getBlueNum().replaceAll(" ", ""));
        }

        element.getLotterycode().setTagValue(codeBuffer.substring(1));

        element.getIssue().setTagValue(ShoppingCart.getInstance().getIssue());
        element.getLotteryvalue().setTagValue((ShoppingCart.getInstance().getLotteryvalue() * 100) + "");

        element.getLotterynumber().setTagValue(ShoppingCart.getInstance().getLotterynumber().toString());
        element.getAppnumbers().setTagValue(ShoppingCart.getInstance().getAppnumbers().toString());
        element.getIssuesnumbers().setTagValue(ShoppingCart.getInstance().getIssuesnumbers().toString());

        element.getIssueflag().setTagValue(ShoppingCart.getInstance().getIssuesnumbers() > 1 ? "1" : "0");

        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());

        String xml = message.getXml(element);

        Message result = super.getResult(xml);

        if (result != null) {

            // 第四步：请求结果的数据处理
            // body部分的第二次解析，解析的是明文内容

            XmlPullParser parser = Xml.newPullParser();
            try {

                DES des = new DES();
                String body = "<body>" + des.authcode(result.getBody().getServieceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";

                parser.setInput(new StringReader(body));

                int eventType = parser.getEventType();
                String name;

                BetElement resultElement = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if ("errorcode".equals(name)) {
                                result.getBody().getOelement().setErrorode(parser.nextText());
                            }
                            if ("errormsg".equals(name)) {
                                result.getBody().getOelement().setErrormsg(parser.nextText());
                            }

                            // 正对于当前请求
                            if ("element".equals(name)) {
                                resultElement = new BetElement();
                                result.getBody().getElements().add(resultElement);
                            }

                            if ("actvalue".equals(name)) {
                                if (resultElement != null) {
                                    resultElement.setActvalue(parser.nextText());
                                }
                            }

                            break;
                    }
                    eventType = parser.next();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    /**
     * 用户登录1
     *
     * @param user
     */
    public Message login1(User user) {
        // 第一步：获取到登录用的xml
        // 创建登录用Element
        UserLoginElement element = new UserLoginElement();
        element.getActpastword().setTagValue(user.getPassword());
        //设置用户数据,获取发送请求xml
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());
        String xml = message.getXml(element);

        // 第二步(代码不变)：发送xml到服务器端，等待回复
        // HttpClientUtil.sendXml
        HttpClientUtil util = new HttpClientUtil();
        //发送到数据库返回的流
        InputStream is = util.sendXml(ConstantValue.LOTTERY_URI, xml);
        if (is != null) {

            Message result = new Message();//存放解析读取的数据
            // 第三步(代码不变)：数据的校验（MD5数据校验）
            // timestamp+digest+body
            //解析xml
            XmlPullParser parser = Xml.newPullParser();
            try {
                //写入流时，必要的加上编码
                parser.setInput(is, ConstantValue.ENCODING);

                int eventType = parser.getEventType();
                String tagName;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            if ("timestamp".equals(tagName)) {
                                result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if ("digest".equals(tagName)) {
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if ("body".equals(tagName)) {
                                result.getBody().setServieceBodyInsideDESInfo(parser.nextText());
                            }
                    }
                    eventType = parser.next();   //下一个标签
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 原始数据还原：时间戳（解析）+密码（常量）+body明文（解析+解密DES）
            DES des = new DES();
            String body = "<body>" + des.authcode(result.getBody().getServieceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD) + "</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue()
                    + ConstantValue.AGENTER_PASSWORD + body;
            // body明文（解析+解密DES）
            //生成手机端MD5，与服务器的对比
            String md5Hex = DigestUtils.md5Hex(orgInfo);
            if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
                //比对OK
                // 第四步：请求结果的数据处理
                // body部分的第二次解析，解析的是明文内容
                parser = Xml.newPullParser();
                try {
                    StringReader reader = new StringReader(body);
                    parser.setInput(reader);
                    int eventType = parser.getEventType();
                    String tagName;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                tagName = parser.getName();
                                if ("errorcode".equals(tagName)) {
                                    result.getBody().getOelement().setErrorode(parser.nextText());
                                }
                                if ("errormsg".equals(tagName)) {
                                    result.getBody().getOelement().setErrormsg(parser.nextText());
                                }

                        }
                        eventType = parser.next();   //下一个标签
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }

}
