package com.bruce.Lottery.net.protocol;

import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 11:53.
 *
 */
public class Message {

    private Header header = new Header();
    private Body body = new Body();

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    /**
     * 序列化协议
     */
    public void serializerMessage(XmlSerializer serializer) {
        try {
            // <message version="1.0">
            serializer.startTag(null, "message");
            // MUST follow a call to startTag() immediately
            serializer.attribute(null, "version", "1.0");

            header.serializeHeader(serializer, "");// 获取完整的body
			body.serializerBody(serializer);

            serializer.endTag(null, "message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取请求的xml文件
     *
     * @return
     */
    public String getXml() {

        // 序列化
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        // This method can only be called just after setOutput
        try {
            serializer.setOutput(writer);
            serializer.startDocument(ConstantValue.ENCODING, null);
            this.serializerMessage(serializer);
            serializer.endDocument();

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
