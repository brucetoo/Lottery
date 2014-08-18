package com.bruce.Lottery.View;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
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
import com.bruce.Lottery.View.manager.TitleManager;
import com.bruce.Lottery.adapter.PoolAdapter;

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
                    view.setBackgroundResource(R.drawable.id_redball);
                    view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.id_red_ball));
                    blueNum.add(position + 1);
                } else { //被选中就直接变颜色
                    view.setBackgroundResource(R.drawable.id_defalut_ball);
                    //此处是按照Object删除，不能单纯用position+1
                    blueNum.remove((Object) (position + 1));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册传感器，传感器很耗时，所以在退出该页面时 onPause需要注销掉
        listener = new shakeListener();
        manager.registerListener(listener,manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
        changeTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
     //注销传感器
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

        redAdapter = new PoolAdapter(context, 33,redNum,R.drawable.id_redball);
        blueAdapter = new PoolAdapter(context, 16,blueNum,R.drawable.id_blueball);

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
                break;
            case R.id.ii_ssq_random_blue:
                blueNum.clear();
                int num = random.nextInt(16) + 1;
                blueNum.add(num);
                blueAdapter.notifyDataSetChanged();
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
}
