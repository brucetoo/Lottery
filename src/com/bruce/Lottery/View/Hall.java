package com.bruce.Lottery.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.GlobalParams;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.bean.Oelement;
import com.bruce.Lottery.engine.CommonInfoEngine;
import com.bruce.Lottery.net.protocol.Element;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.CurrentIssueElement;
import com.bruce.Lottery.utils.BeanFactory;
import com.bruce.Lottery.utils.PromptManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce-too
 * on 2014/8/13
 * Time 22:55.
 */
public class Hall extends BaseUI {

    private ListView ii_hall_lottery_list;
    private CategoryAdapter adapter;

    // ViewPager配置
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private ImageView underLine;

    private List<View> pagers;

    private TextView fcTitle;// 福彩
    private TextView tcTitle;// 体彩
    private TextView gpcTitle;// 高频彩
    public Hall(Context context) {
        super(context);
    }

    // 记录ViewPger上一个界面的position信息
    private int lastPosition = 0;

    @Override
    protected void setListener() {

        fcTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);

            }
        });

        tcTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);

            }
        });

        gpcTitle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);

            }
        });

         pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

             @Override
             public void onPageSelected(int i) {

                 TranslateAnimation animation = new TranslateAnimation(lastPosition*GlobalParams.WD_WIDTH/3,
                         i*GlobalParams.WD_WIDTH/3,0,0);
                 animation.setDuration(300);
                 animation.setFillAfter(true);
                 underLine.setAnimation(animation);
                 lastPosition = i;

                 fcTitle.setTextColor(Color.BLACK);
                 tcTitle.setTextColor(Color.BLACK);
                 gpcTitle.setTextColor(Color.BLACK);

                 switch (i){
                     case 0:
                         fcTitle.setTextColor(Color.RED);
                         break;
                     case 1:
                         tcTitle.setTextColor(Color.RED);
                         break;
                     case 2:
                         gpcTitle.setTextColor(Color.RED);
                         break;
                 }
             }

             @Override
             public void onPageScrolled(int i, float v, int i2) {

             }

             @Override
             public void onPageScrollStateChanged(int i) {

             }
         });
    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall, null);
        ii_hall_lottery_list  = new ListView(context);

        underLine = (ImageView) showInMiddle.findViewById(R.id.ii_category_selector);
        pager = (ViewPager) showInMiddle.findViewById(R.id.ii_viewpager);
        pagerAdapter = new MyPagerAdapter();

        adapter = new CategoryAdapter();
        ii_hall_lottery_list.setAdapter(adapter);

        fcTitle = (TextView) showInMiddle.findViewById(R.id.ii_category_fc);
        tcTitle = (TextView) showInMiddle.findViewById(R.id.ii_category_tc);
        gpcTitle = (TextView) showInMiddle.findViewById(R.id.ii_category_gpc);
        initPager();

        pager.setAdapter(pagerAdapter);
        //初始化选项卡
        initTabStrip();
    }

    private void initTabStrip() {

        fcTitle.setTextColor(Color.RED);

        //屏幕宽度
        //image 宽度
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.id_category_selector);
        // bitmap.getWidth();
        //得到初始化选项卡位置
        int offset = (GlobalParams.WD_WIDTH / 3 - bitmap.getWidth() )/ 2;

        //对应 scaleType中的matrix矩阵参数设置初始位置
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset,0);
        underLine.setImageMatrix(matrix);
    }

    private void initPager() {
        pagers = new ArrayList<View>();
        pagers.add(ii_hall_lottery_list);

        TextView item = new TextView(context);
        item.setText("体彩");
        pagers.add(item);

        item = new TextView(context);
        item.setText("高频彩");
        pagers.add(item);

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


    //页面切换需要传递的数据
    private Bundle ssqBuddle;

    /**
     * 通知界面 Title 改变
     * @param element
     */
    private void notifyChanged(Element element) {
        CurrentIssueElement element1 = (CurrentIssueElement) element;
        String text = context.getResources().getString(R.string.is_hall_common_summary);
        text = StringUtils.replaceEach(text,new String[]{"ISSUE","TIME"},
                                new String[]{element1.getIssue(),getLasttime(element1.getLasttime())});
        TextView view = (TextView) ii_hall_lottery_list.findViewWithTag(0);
        if(view != null){
            view.setText(text);
        }

        ssqBuddle = new Bundle();
        ssqBuddle.putString("issue",element1.getIssue());
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

            holder.bet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MiddleManager.getInstance().changeUI(playSSQ.class,ssqBuddle);
                }
            });
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


    /**
     * Viewpager用adapter
     *
     * @author Administrator
     *
     */
    private class MyPagerAdapter extends PagerAdapter {

        public Object instantiateItem(ViewGroup container, int position) {
            View view = pagers.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = pagers.get(position);

            container.removeView(view);
        }

        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
