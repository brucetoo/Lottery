package com.bruce.Lottery.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.bruce.Lottery.GlobalParams;


/**
 * Created by Bruce
 * Data 2014/8/8
 * Time 16:27.
 *
 */
public class NetUtil {

    /**
     * 检查当前的网络类型
     * @return
     */
    public static boolean checkNet(Context context){
        //判断wifi是否连接
        boolean isWIFI = isWIFIConnection(context);
        //判断Mobile是否连接
        boolean isMobile = isMobileConnection(context);

        //Mobile连接成功后，判断是哪个APN被选中
        if(isMobile){
            //APN被选中的代理是否有内容，如果有是 wap方式
            readAPN(context);
        }

        if (!isWIFI && !isMobile){
            //没有联网
            return false;
        }
        return true;
    }

    /**
     * 读取被选中的APN
     * @param context
     */
    private static void readAPN(Context context) {

        //APN的路径
        Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");//4.0模拟器屏蔽掉该权限
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()){
            //获取ＡＰＮ代理值和端口
           GlobalParams.PROXY = cursor.getString(cursor.getColumnIndex("proxy"));
           GlobalParams.PORT = cursor.getInt(cursor.getColumnIndex("port"));
        }
    }

    /**
     * 判断手机联网
     * @param context
     * @return
     */
    private static boolean isMobileConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo!=null){
            return networkInfo.isConnected();
        }
        return false;
    }
    /**
     * 判断wifi联网
     * @param context
     * @return
     */
    private static boolean isWIFIConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo!=null){
            return networkInfo.isConnected();
        }
        return false;
    }
}
