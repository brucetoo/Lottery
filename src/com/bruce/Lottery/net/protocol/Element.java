package com.bruce.Lottery.net.protocol;

import org.xmlpull.v1.XmlSerializer;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 11:32.
 * 请求数据的封装
 */
public abstract class Element {

  // Element中方的都是请求的东西，而请求的总类会分很多种
  // 所以要对请求公共的部门进行抽象化处理

    //序列化
    /**
     * 每个请求都需要序列化自己
     * @param serializer
     */
    public abstract void serializeElement(XmlSerializer serializer);

    // ②每个请求都有自己的标示
    /**
     * 每个请求都有自己的标示
     * @return
     */
    public abstract String getTransactionType();


/*
    // <lotteryid>118</lotteryid>
    // <issues>1</issues>


    private Leaf lotteryid = new Leaf("lotteryid");

    private Leaf issues = new Leaf("issues","1");//常量直接传递

    //序列化

    public void serializeElement(XmlSerializer serializer){
        try {
            serializer.startTag(null, "element");
            lotteryid.serializeLeaf(serializer);
            issues.serializeLeaf(serializer);
            serializer.endTag(null, "element");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    */
/**
	 * 获取请求的标示
     *//*


	public String getTransactionType() {
		return "12002";
	}
*/

}
