package com.bruce.Lottery.utils;

import android.text.TextUtils;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.GlobalParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bruce-too
 * on 2014/8/9
 * Time 10:39.
 * 联网工具类
 */
public class HttpClientUtil {

   private  HttpClient httpClient;
   private HttpPost post;  //发送文件

    public HttpClientUtil() {
        //判断代理是否为空
        if(!TextUtils.isEmpty(GlobalParams.PROXY)){
             //不为空就设置代理信息
              httpClient = new DefaultHttpClient();
            //设置代理信息
            HttpHost host = new HttpHost(GlobalParams.PROXY,GlobalParams.PORT);
            //为http请求设置参数
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,host);
        }
    }


    /**
     * 向指定路径发送Xml文件，获取返回输入流
     * @param uri
     * @param xml
     */
    public InputStream sendXml(String uri,String xml){

        post = new HttpPost(uri);
        try {
            //发送字符串的xml文件时，需要对齐进行编码，避免乱码
            StringEntity entity = new StringEntity(xml, ConstantValue.ENCODING);
            //设置发送的内容
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);

            //200
            if(response.getStatusLine().getStatusCode() == 200){
                //连接成功返回输入流
               return response.getEntity().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
