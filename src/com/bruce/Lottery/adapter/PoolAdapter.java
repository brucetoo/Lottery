package com.bruce.Lottery.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bruce.Lottery.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Bruce
 * Data 2014/8/18
 * Time 11:14.
 */
public class PoolAdapter extends BaseAdapter {

    private Context context;
    private int endNum;  //球总数
    private List<Integer> selectedNum; //存放已选球
    private int bgResouce;


    public PoolAdapter(Context context, int endNum, List<Integer> selectedNum, int bgResouce) {
        this.context = context;
        this.endNum = endNum;
        this.selectedNum = selectedNum;
        this.bgResouce = bgResouce;
    }

    @Override
    public int getCount() {
        return endNum;
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
        TextView  ball = new TextView(context);
        DecimalFormat format = new DecimalFormat("00");
        ball.setText(format.format(position+1));
        if(selectedNum.contains(position+1)){
            ball.setBackgroundResource(bgResouce);
        }else{
            ball.setBackgroundResource(R.drawable.id_defalut_ball);
        }
        ball.setGravity(Gravity.CENTER);
        ball.setTextSize(16);
        return ball;
    }
}
