package com.bruce.Lottery.net.protocol.element;

import com.bruce.Lottery.net.protocol.Element;
import com.bruce.Lottery.net.protocol.Leaf;
import org.xmlpull.v1.XmlSerializer;

/**
 * Created by bruce-too
 * on 2014/8/9
 * Time 11:57.
 * 用户登陆请求
 */
public class UserLoginElement extends Element {

    private Leaf actpastword = new Leaf("actpastword");

    @Override
    public void serializeElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            actpastword.serializeLeaf(serializer);
            serializer.endTag(null, "element");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "14001";
    }

    public Leaf getActpastword() {
        return actpastword;
    }
}
