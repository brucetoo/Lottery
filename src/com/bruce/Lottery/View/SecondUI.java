package com.bruce.Lottery.View;

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
public class SecondUI {
    private Context context;
    public SecondUI(Context context) {
        this.context = context;
    }

    public View getChild(){
        TextView textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.BLUE);
        textView.setText("这是测试页面2");
        return textView;
    }
}
