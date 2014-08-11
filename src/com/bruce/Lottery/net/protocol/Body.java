package com.bruce.Lottery.net.protocol;

import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 11:54.
 */
public class Body {

    private List<Element> elements=new ArrayList<Element>();


    /************************处理服务端回复*******************************/
          private  String servieceBodyInsideDESInfo;    //处理服务端回复的 Des加密信息

    public String getServieceBodyInsideDESInfo() {
        return servieceBodyInsideDESInfo;
    }

    public void setServieceBodyInsideDESInfo(String servieceBodyInsideDESInfo) {
        this.servieceBodyInsideDESInfo = servieceBodyInsideDESInfo;
    }
    /************************处理服务端回复*******************************/


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

            for(Element item:elements){
                item.serializeElement(serializer);
            }

            serializer.endTag(null, "elements");
            serializer.endTag(null, "body");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
