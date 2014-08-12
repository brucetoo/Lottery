package com.bruce.Lottery.engine;

import android.util.Xml;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.utils.DES;
import com.bruce.Lottery.utils.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
/**
 * Created by Bruce
 * Data 2014/8/12
 * Time 14:25.
 * 抽取出解析xml的公共部分。避免代码冗余
 */
public abstract class BaseEngine {
	public Message getResult(String xml) {
		// 第二步和第三步

		// 第二步(代码不变)：发送xml到服务器端，等待回复
		// HttpClientUtil.sendXml
		// 在这行代码前，没有判断网络类型？
		HttpClientUtil util = new HttpClientUtil();
		InputStream is = util.sendXml(ConstantValue.LOTTERY_URI, xml);
		// 判断输入流非空
		if (is != null) {
			Message result = new Message();

			// 第三步(代码不变)：数据的校验（MD5数据校验）
			// timestamp+digest+body
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(is, ConstantValue.ENCODING);

				int eventType = parser.getEventType();
				String name;

				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if ("timestamp".equals(name)) {
							result.getHeader().getTimestamp()
									.setTagValue(parser.nextText());
						}
						if ("digest".equals(name)) {
							result.getHeader().getDigest()
									.setTagValue(parser.nextText());
						}
						if ("body".equals(name)) {
							result.getBody().setServieceBodyInsideDESInfo(
									parser.nextText());
						}
						break;
					}
					eventType = parser.next();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// 原始数据还原：时间戳（解析）+密码（常量）+body明文（解析+解密DES）
			// body明文（解析+解密DES）
			DES des = new DES();
			String body = "<body>"
					+ des.authcode(result.getBody()
							.getServieceBodyInsideDESInfo(), "ENCODE",
							ConstantValue.DES_PASSWORD) + "</body>";

			String orgInfo = result.getHeader().getTimestamp().getTagValue()
					+ ConstantValue.AGENTER_PASSWORD + body;

			// 利用工具生成手机端的MD5
			String md5Hex = DigestUtils.md5Hex(orgInfo);
			// 将手机端与服务器的进行比对
			if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
				// 比对通过
				return result;
			}
		}

		return null;
	}
}
