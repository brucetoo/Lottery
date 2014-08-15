package com.bruce.Lottery.engine.impl;

import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.engine.BaseEngine;
import com.bruce.Lottery.engine.CommonInfoEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.CurrentIssueElement;
import com.bruce.Lottery.utils.DES;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;

/**
 * Created by Bruce
 * Data 2014/8/14
 * Time 17:41.
 */
public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine{

    @Override
    public Message getCurrentIssueInfo(Integer param) {
        //设置 当前采种信息的 element
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue(param.toString());

        Message message = new Message();
        String xml = message.getXml(element);

        //向服务器发送请求，并获取返回请求
        Message result = getResult(xml);
        if(result != null){
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
                CurrentIssueElement resultElement = null;
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
                            //resultElement的初始化 ---判断是否有element标签
                            if("element".equals(tagName)){
                                resultElement = new CurrentIssueElement();
                                //将element放到Body中
                                result.getBody().getElements().add(resultElement);
                            }

                            //解析请求的特殊数据
                            if("issue".equals(tagName)){
                                resultElement.setIssue(parser.nextText());
                            }

                            if("lasttime".equals(tagName)){
                                resultElement.setLasttime(parser.nextText());
                            }


                    }
                    eventType = parser.next();   //下一个标签
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
