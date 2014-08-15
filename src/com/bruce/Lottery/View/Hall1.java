package com.bruce.Lottery.View;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.bean.Oelement;
import com.bruce.Lottery.engine.CommonInfoEngine;
import com.bruce.Lottery.net.protocol.Element;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.CurrentIssueElement;
import com.bruce.Lottery.utils.BeanFactory;
import com.bruce.Lottery.utils.PromptManager;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by bruce-too
 * on 2014/8/13
 * Time 22:55.
 */
public class Hall1 extends BaseUI {

    private TextView ssqIssue;
    private ImageView ssqBet;

    public Hall1(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall1, null);
        ssqIssue = (TextView) showInMiddle.findViewById(R.id.ii_hall_ssq_summary);
        ssqBet = (ImageView) showInMiddle.findViewById(R.id.ii_hall_ssq_bet );
       // getCurrentIssueInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentIssueInfo();
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

        /**
         * 在执行 doInBackground之前就要确定是否有网络，一般情况 Thread.start（）方法是可重新的
         * 但是 excute方法不可重新，所以就仿照excute的方法，设置了个execueProxy的方法，提前就判断是否有网
         */
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
                if(message != null){
                    Oelement oelement = message.getBody().getOelement();
                    //服务器返回成功
                    if(oelement.getErrorode().equals("0")){
                      notifyChanged(message.getBody().getElements().get(0));
                    }else{
                        PromptManager.showErrorDialog(context,oelement.getErrormsg());
                    }
                }else{
                    PromptManager.showErrorDialog(context,"服务器忙,稍后再试....");
                }

            }
        }.executeProxy(ConstantValue.SSQ);

    }


    /**
     * 将秒时间转换成日时分格式
     *
     * @param lasttime
     * @return
     */
    public String getLasttime(String lasttime) {
        StringBuffer result = new StringBuffer();
        if (StringUtils.isNumericSpace(lasttime)) {
            int time = Integer.parseInt(lasttime);
            int day = time / (24 * 60 * 60);
            result.append(day).append("天");
            if (day > 0) {
                time = time - day * 24 * 60 * 60;
            }
            int hour = time / 3600;
            result.append(hour).append("时");
            if (hour > 0) {
                time = time - hour * 60 * 60;
            }
            int minute = time / 60;
            result.append(minute).append("分");
        }
        return result.toString();
    }

    private void notifyChanged(Element element) {
        CurrentIssueElement element1 = (CurrentIssueElement) element;
        String text = context.getResources().getString(R.string.is_hall_common_summary);
        text = StringUtils.replaceEach(text,new String[]{"ISSUE","TIME"},
                                new String[]{element1.getIssue(),getLasttime(element1.getLasttime())});
         ssqIssue.setText(text);

        //ssqBet.setImageResource();
    }

}
