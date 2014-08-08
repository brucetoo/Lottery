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
