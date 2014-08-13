package com.bruce.Lottery.View;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.View.manager.BaseUI;

/**
 * Created by bruce-too
 * on 2014/8/12
 * Time 21:48.
 * 第一个测试界面
 */
public class SecondUI extends BaseUI{
    private Context context;
    public SecondUI(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    public View getChild(){
        TextView textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.BLUE);
        textView.setText("这是测试页面2");
        return textView;
    }

    @Override
    public int getID() {
        return ConstantValue.SECOND_VIEW;
    }
}
