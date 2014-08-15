package com.bruce.Lottery.View;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
public class Hall2 extends BaseUI {

    private ListView ii_hall_lottery_list;
    private CategoryAdapter adapter;

    public Hall2(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall2, null);
        ii_hall_lottery_list  = (ListView) showInMiddle.findViewById(R.id.ii_hall_lottery_list);
        adapter = new CategoryAdapter();
        ii_hall_lottery_list.setAdapter(adapter);
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
        TextView view = (TextView) ii_hall_lottery_list.findViewWithTag(0);
        if(view != null){
            view.setText(text);
        }
    }


    // 资源信息
    private int[] logoResIds = new int[] { R.drawable.id_ssq, R.drawable.id_3d, R.drawable.id_qlc };
    private int[] titleResIds = new int[] { R.string.is_hall_ssq_title, R.string.is_hall_3d_title, R.string.is_hall_qlc_title };

    private class CategoryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = View.inflate(context, R.layout.il_hall_lottery_item, null);

                holder.logo = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_logo);
                holder.title = (TextView) convertView.findViewById(R.id.ii_hall_lottery_title);
                holder.summary = (TextView) convertView.findViewById(R.id.ii_hall_lottery_summary);
                holder.bet = (ImageView) convertView.findViewById(R.id.ii_hall_lottery_bet);

                // A tag can be used to mark a view in its hierarchy and does not have to be unique within the hierarchy.
                // Tags can also be used to store data within a view without resorting to another data structure.
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.logo.setImageResource(logoResIds[position]);
            holder.title.setText(titleResIds[position]);

            holder.summary.setTag(position);

            return convertView;
        }

        // 依据item的layout
        class ViewHolder {
            ImageView logo;
            TextView title;
            TextView summary;
            ImageView bet;
        }

    }

}
