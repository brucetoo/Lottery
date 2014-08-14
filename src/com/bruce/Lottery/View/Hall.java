package com.bruce.Lottery.View;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.engine.CommonInfoEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.utils.BeanFactory;

/**
 * Created by bruce-too
 * on 2014/8/13
 * Time 22:55.
 */
public class Hall extends BaseUI {
    public Hall(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall1, null);
    }


    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 获取双色球当前销售期信息
     */
    private void getCurrentIssueInfo(){
          //异步访问网络获取数据

        //传递彩票信息的唯一标示
       // new MyHttpTask().execute(ConstantValue.SSQ);
        new MyHttpTask<Integer>() {

            @Override
            protected Message doInBackground(Integer... params) {
                //后台统一获取 业务的调用
                CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);
                return engine.getCurrentIssueInfo(params[0]);
            }

            @Override
            protected void onPostExecute(Message message) {
                super.onPostExecute(message);


            }
        }.executeProxy(ConstantValue.SSQ);

    }

}
