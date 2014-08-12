package com.bruce.Lottery.test;

import android.test.AndroidTestCase;
import android.util.Log;
import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.bean.User;
import com.bruce.Lottery.engine.UserEngineImpl;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.CurrentIssueElement;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 9:59.
 */
public class XmlTest extends AndroidTestCase {



    public void testLogin(){
        UserEngineImpl login = new UserEngineImpl();
        User user = new User();
        user.setUserName("13200000000");
        user.setPassword("0000000");
        Message message = login.login(user);
        Log.i("loginError",message.getBody().getOelement().getErrorode());
    }



    public void testCreateXml(){

        Message message = new Message();
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue("118");
        String str  = message.getXml(element);
        Log.i("aaaaa",str);
    }


    public void createXml1(){

        //序列化
        XmlSerializer serializer = Xml.newSerializer();

        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ConstantValue.ENCODING,null);

            serializer.startTag(null, "message");
            serializer.startTag(null, "header");

            serializer.startTag(null, "agenterid");
            serializer.text(ConstantValue.AGENTERID);
            serializer.endTag(null, "agenterid");

            serializer.startTag(null, "agenterid");
            serializer.text(ConstantValue.AGENTERID);
            serializer.endTag(null, "agenterid");

            serializer.startTag(null, "agenterid");
            serializer.text(ConstantValue.AGENTERID);
            serializer.endTag(null, "agenterid");

            serializer.endTag(null, "header");
            serializer.startTag(null, "body");
            serializer.endTag(null, "body");
            serializer.endTag(null, "message");

            serializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createXml2(){

        //序列化
        XmlSerializer serializer = Xml.newSerializer();

        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ConstantValue.ENCODING, null);

            Message message = new Message();
            message.serializerMessage(serializer);

            serializer.endDocument();
            Log.i("XML------", writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
