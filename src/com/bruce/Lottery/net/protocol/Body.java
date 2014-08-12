package com.bruce.Lottery.net.protocol;

import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.bean.Oelement;
import com.bruce.Lottery.utils.DES;
import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 11:54.
 */
public class Body {

    private List<Element> elements = new ArrayList<Element>();


    /**
     * *********************处理服务端回复******************************
     */
    private String servieceBodyInsideDESInfo;    //处理服务端回复的 Des加密信息
    private Oelement oelement = new Oelement();  //服务端回复的数据


    public Oelement getOelement() {
        return oelement;
    }

    public void setOelement(Oelement oelement) {
        this.oelement = oelement;
    }

    public String getServieceBodyInsideDESInfo() {
        return servieceBodyInsideDESInfo;
    }

    public void setServieceBodyInsideDESInfo(String servieceBodyInsideDESInfo) {
        this.servieceBodyInsideDESInfo = servieceBodyInsideDESInfo;
    }
    /************************处理服务端回复*******************************/


    public List<Element> getElements() {
        return elements;
    }  //请求的element


    /**
     * 序列化请求
     */
    public void serializerBody(XmlSerializer serializer) {
        /**
         * <body>
         <elements>
         <element>
         <lotteryid>118</lotteryid>
         <issues>1</issues>
         </element>
         </elements>
         </body>
         */

        try {
            serializer.startTag(null, "body");
            serializer.startTag(null, "elements");

            for (Element item : elements) {
                item.serializeElement(serializer);
            }

            serializer.endTag(null, "elements");
            serializer.endTag(null, "body");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取到完整的body
     * @return
     */
    public String getWholeBody()
    {
        StringWriter writer=new StringWriter();

        XmlSerializer temp= Xml.newSerializer();
        try {
            temp.setOutput(writer);
            this.serializerBody(temp);
            // 在setOutput后必须调用flush才能写入数据
            temp.flush();
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取body里面的DES加密数据
     * @return
     */
    public String getBodyInsideDESInfo()
    {
        // 加密数据
        String wholeBody = getWholeBody();
        String orgDesInfo= StringUtils.substringBetween(wholeBody, "<body>", "</body>");
        DES des=new DES();
        return des.authcode(orgDesInfo, "DECODE", ConstantValue.DES_PASSWORD);
    }

}
