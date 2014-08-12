package com.bruce.Lottery.View;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by bruce-too
 * on 2014/8/12
 * Time 21:48.
 * 第一个测试界面
 */
public class FirstUI {
    private Context context;
    public FirstUI(Context context) {
        this.context = context;
    }

    public View getChild(){
        TextView textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.RED);
        textView.setText("这是测试页面1");
        return textView;
    }
}
