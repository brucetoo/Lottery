package com.bruce.Lottery.View;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.custom.MyGridView;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.View.manager.BottomManager;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.View.manager.TitleManager;
import com.bruce.Lottery.adapter.PoolAdapter;
import com.bruce.Lottery.bean.Oelement;
import com.bruce.Lottery.bean.ShoppingCart;
import com.bruce.Lottery.bean.Ticket;
import com.bruce.Lottery.engine.CommonInfoEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.CurrentIssueElement;
import com.bruce.Lottery.utils.BeanFactory;
import com.bruce.Lottery.utils.PromptManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Bruce
 * Data 2014/8/18
 * Time 9:18.
 */
public class playSSQ extends BaseUI {

    // 机选
    private Button randomRed;
    private Button randomBlue;
    // 选号容器
    private MyGridView redContainer;
    private GridView blueContainer;

    private PoolAdapter redAdapter;
    private PoolAdapter blueAdapter;

    //记录点击的红球
    private List<Integer> redNum;
    private List<Integer> blueNum;

    /**
     * 传感器管理
     */
    private SensorManager manager;
    private shakeListener listener;

    public playSSQ(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        randomBlue.setOnClickListener(this);
        randomRed.setOnClickListener(this);

        redContainer.setOnActionUpListener(new MyGridView.OnActionUpListener() {

            @Override
            public void OnActionUp(View view, int position) {
                //首先判断选中的item是否被点击过
                if (!redNum.contains(position + 1)) {
                    view.setBackgroundResource(R.drawable.id_redball);
                    redNum.add(position + 1);
                } else { //被选中就直接变颜色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    //此处是按照Object删除，不能单纯用position+1
                    redNum.remove((Object) (position + 1));
                }
                changeNotice();
            }
        });
      /*  redContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //首先判断选中的item是否被点击过
                if (!redNum.contains(position + 1)) {
                    redNum.add(position + 1);
                } else { //被选中就直接变颜色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    //此处是按照Object删除，不能单纯用position+1
                    redNum.remove((Object)(position+1));
                }
            }
        });*/


        blueContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //首先判断选中的item是否被点击过
                if (!blueNum.contains(position + 1)) {
                    //没有选中就设置背景色为红色，加上抖动效果
                    view.setBackgroundResource(R.drawable.id_blueball);
                    view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.id_red_ball));
                    blueNum.add(position + 1);
                } else { //被选中就直接变颜色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    //此处是按照Object删除，不能单纯用position+1
                    blueNum.remove((Object) (position + 1));
                }
                changeNotice();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        clear();
        //注册传感器，传感器很耗时，所以在退出该页面时 onPause需要注销掉
        listener = new shakeListener(context) {
            @Override
            public void randomCure() {
                randomSSQ();
            }
        };
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        changeTitle();  //改变标题
        changeNotice(); //改变底部通知
    }

    /**
     * 随机选择一注双色球
     */
    private void randomSSQ() {
        Random random = new Random();
        redNum.clear();
        blueNum.clear();
        while (redNum.size() < 6) {
            int num = random.nextInt(33) + 1;
            if (redNum.contains(num)) {
                continue;
            }
            redNum.add(num);
        }
        int num = random.nextInt(16) + 1;
        blueNum.add(num);
        redAdapter.notifyDataSetChanged();
        blueAdapter.notifyDataSetChanged();
        changeNotice();
    }

    @Override
    public void onPause() {
        super.onPause();
        //注销传感器
        listener = null;
        manager.unregisterListener(listener);
    }

    @Override
    protected void init() {

        showInMiddle = (RelativeLayout) View.inflate(context, R.layout.il_playssq, null);
        redContainer = (MyGridView) showInMiddle.findViewById(R.id.ii_ssq_red_number_container);
        blueContainer = (GridView) showInMiddle.findViewById(R.id.ii_ssq_blue_number_container);
        randomRed = (Button) showInMiddle.findViewById(R.id.ii_ssq_random_red);
        randomBlue = (Button) showInMiddle.findViewById(R.id.ii_ssq_random_blue);


        redNum = new ArrayList<Integer>();
        blueNum = new ArrayList<Integer>();

        redAdapter = new PoolAdapter(context, 33, redNum, R.drawable.id_redball);
        blueAdapter = new PoolAdapter(context, 16, blueNum, R.drawable.id_blueball);

        blueContainer.setAdapter(blueAdapter);
        redContainer.setAdapter(redAdapter);

        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SSQ;
    }

    @Override
    public void onClick(View v) {
        Random random = new Random();
        switch (v.getId()) {
            case R.id.ii_ssq_random_red:
                redNum.clear();
                while (redNum.size() < 6) {
                    int num = random.nextInt(33) + 1;
                    if (redNum.contains(num)) {
                        continue;
                    }
                    redNum.add(num);
                }
                redAdapter.notifyDataSetChanged();
                changeNotice();
                break;
            case R.id.ii_ssq_random_blue:
                blueNum.clear();
                int num = random.nextInt(16) + 1;
                blueNum.add(num);
                blueAdapter.notifyDataSetChanged();
                changeNotice();
                break;
        }
    }

    /**
     * 利用传递过来的数据-修改标题
     */
    private void changeTitle() {

        String title;
        if (bundle != null) {
            //如果存数据
            title = "双色球第" + bundle.getString("issue") + "期";
        } else {
            title = "双色球选号";
        }

        TitleManager.getInstance().changeTitle(title);
    }

    /**
     * 底部提示信息
     */
    private void changeNotice() {

        String notice = "";
        if (redNum.size() < 6) {
            notice = "你还要选择" + (6 - redNum.size()) + "个红球";
        } else if (blueNum.size() == 0) {
            notice = "请选择一个蓝球";
        } else {
            notice = "共" + calc() + "注" + "  共" + calc() * 2 + "元";
        }
        BottomManager.getInstrance().changeGameBottomNotice(notice);
    }

    private int calc() {
        int redC = (int) (factorial(redNum.size()) / (factorial(6) * factorial(redNum.size() - 6)));
        int blueC = blueNum.size();
        return redC * blueC;
    }

    /**
     * 计算一个数的阶乘
     *
     * @param num
     * @return
     */
    private long factorial(int num) {
        if (num > 1) {
            return factorial(num - 1) * num;
        } else if (num == 1 || num == 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 清空选择
     */
    public void clear() {
        redNum.clear();
        blueNum.clear();

        //改变双色球个数后 需调用
        changeNotice();

        blueAdapter.notifyDataSetChanged();
        redAdapter.notifyDataSetChanged();
    }

    /**
     * 选好了
     */
    public void done() {
        // ①判断：用户是否选择了一注投注
        if (redNum.size() >= 6 && blueNum.size() >= 1) {
            //获取当前销售期信息，有就用
           // if (bundle != null) {
                // ③封装用户的投注信息：红球、蓝球、注数
                Ticket ticket = new Ticket();
                DecimalFormat decimalFormat = new DecimalFormat("00");
                StringBuffer redBuffer = new StringBuffer();
                for (Integer item : redNum) {
                    redBuffer.append(" ").append(decimalFormat.format(item));
                }
                ticket.setRedNum(redBuffer.substring(1));

                StringBuffer blueBuffer = new StringBuffer();
                for (Integer item : blueNum) {
                    blueBuffer.append(" ").append(decimalFormat.format(item));
                }

                ticket.setBlueNum(blueBuffer.substring(1));

                ticket.setNum(calc());

                // ④创建彩票购物车，将投注信息添加到购物车中
                ShoppingCart.getInstance().getTickets().add(ticket);

                // ⑤设置彩种的标示，设置彩种期次
               if(bundle!=null) {
                   ShoppingCart.getInstance().setIssue(bundle.getString("issue"));
               }else{
                   ShoppingCart.getInstance().setIssue("双色球期数");
               }
                ShoppingCart.getInstance().setLotteryid(ConstantValue.SSQ);

                //跳转到购物车
                MiddleManager.getInstance().changeUI(Shopping.class,bundle);
           // } else {//没有就重新获取
                //防止断网出现的问题
               // getCurrentIssueInfo();
            //}

        } else {
            // 提示：需要选择一注
            PromptManager.showToast(context, "需要选择一注");
        }
    }

    /**
     * 获取双色球当前销售期信息
     */
    private void getCurrentIssueInfo() {
        /**
         * 在执行 doInBackground之前就要确定是否有网络，一般情况 Thread.start（）方法是可重新的
         * 但是 excute方法不可重新，所以就仿照excute的方法，设置了个execueProxy的方法，提前就判断是否有网
         */
        new MyHttpTask<Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                PromptManager.showProgressDialog(context);
            }

            @Override
            protected Message doInBackground(Integer... params) {
                //后台统一获取 业务的调用
                CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);
                return engine.getCurrentIssueInfo(params[0]);
            }

            @Override
            protected void onPostExecute(Message message) {
                super.onPostExecute(message);
                PromptManager.closeProgressDialog();
                if (message != null) {
                    Oelement oelement = message.getBody().getOelement();
                    //服务器返回成功
                    if (oelement.getErrorode().equals("0")) {
                        //notifyChanged(message.getBody().getElements().get(0));
                        //创建bundle，获取到信息
                        CurrentIssueElement element = (CurrentIssueElement) message.getBody().getElements().get(0);
                        bundle = new Bundle();
                        bundle.putString("issue", element.getIssue());
                    } else {
                        PromptManager.showErrorDialog(context, oelement.getErrormsg());
                    }
                } else {
                    PromptManager.showErrorDialog(context, "服务器忙,稍后再试....");
                }

            }
        }.executeProxy(ConstantValue.SSQ);

    }
}
