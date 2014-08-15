package com.bruce.Lottery.net.protocol.element;

import com.bruce.Lottery.net.protocol.Element;
import com.bruce.Lottery.net.protocol.Leaf;
import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 15:03.
 * 获取销售期的请求
 */
public class CurrentIssueElement extends Element {

    // <lotteryid>118</lotteryid>
    // <issues>1</issues>
    private Leaf lotteryid = new Leaf("lotteryid");
    private Leaf issues = new Leaf("issues", "1");//常量直接传递

    /**
     * ****************服务器回复*******************
     */
    private String issue;
    private String lasttime;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    /**
     * ****************服务器回复*******************
     */


    @Override
    public void serializeElement(XmlSerializer serializer) {
        try {
            serializer.startTag(null, "element");
            lotteryid.serializeLeaf(serializer);
            issues.serializeLeaf(serializer);
            serializer.endTag(null, "element");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransactionType() {
        return "12002";
    }

    /**
     * 变量在调用时设置
     *
     * @return
     */
    public Leaf getLotteryid() {
        return lotteryid;
    }
}
