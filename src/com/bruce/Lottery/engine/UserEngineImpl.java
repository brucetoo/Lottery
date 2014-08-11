package com.bruce.Lottery.engine;

import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.bean.User;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.UserLoginElement;
import com.bruce.Lottery.utils.DES;
import com.bruce.Lottery.utils.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;

/**
 * Created by bruce-too
 * on 2014/8/9
 * Time 12:01.
 */
public class UserEngineImpl {
    /**
     * 用户登录
     *
     * @param user
     */
    public void login(User user) {
        // 第一步：获取到登录用的xml
        // 创建登录用Element
        UserLoginElement element = new UserLoginElement();
        element.getActpastword().setTagValue(user.getPassword());
        //设置用户数据,获取发送请求xml
        Message message = new Message();
        message.getHeader().getUsername().setTagValue(user.getUserName());
        String xml = message.getXml();

        // 第二步(代码不变)：发送xml到服务器端，等待回复
        // HttpClientUtil.sendXml
        InputStream is = new HttpClientUtil().sendXml(ConstantValue.LOTTERY_URI, xml);
        if (is != null) {

            Message result = new Message();//存放解析读取的数据
            // 第三步(代码不变)：数据的校验（MD5数据校验）
            // timestamp+digest+body
             //解析xml
            XmlPullParser parser = Xml.newPullParser();
            try {
                parser.setInput(is,ConstantValue.ENCODING);

                int eventType = parser.getEventType();
                String tagName;
                while(eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            if("timestamp".equals(tagName)){
                                 result.getHeader().getTimestamp().setTagValue(parser.nextText());
                            }
                            if("digest".equals(tagName)){
                                result.getHeader().getDigest().setTagValue(parser.nextText());
                            }
                            if("body".equals(tagName)){
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
            String body ="<body>"+ des.authcode(result.getBody().getServieceBodyInsideDESInfo(), "ENCODE", ConstantValue.DES_PASSWORD)+"</body>";
            String orgInfo = result.getHeader().getTimestamp().getTagValue()
                    +ConstantValue.AGENTER_PASSWORD+body;
            // body明文（解析+解密DES）
        }
    }

}
