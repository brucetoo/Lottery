package com.bruce.Lottery.net.protocol;

import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 10:36.
 * 单独的叶子节点
 */
public class Leaf {
    // <agenterid>889931</agenterid>

    private String tagName; //叶子名字
    private String tagValue;//叶子值

    public Leaf(String tagName) {
        super();
        this.tagName = tagName;
    }

    //带常量的构造函数
    public Leaf(String tagName, String tagValue) {
        this.tagName = tagName;
        this.tagValue = tagValue;
    }

    /**
     * 序列化每个叶子节点
     *
     * @param serializer
     */
    public void serializeLeaf(XmlSerializer serializer) {

        try {
            serializer.startTag(null, tagName);
            if (tagValue == null) {
                tagValue = "";
            }
            serializer.text(tagValue);
            serializer.endTag(null, tagName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }


}
