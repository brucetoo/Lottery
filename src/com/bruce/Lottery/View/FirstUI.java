package com.bruce.Lottery.View;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bruce.Lottery.View.manager.BaseUI;

/**
 * Created by bruce-too
 * on 2014/8/12
 * Time 21:48.
 * 第一个测试界面
 */
public class FirstUI extends BaseUI{

    private TextView textView;
    public FirstUI(Context context) {
        super(context);
        //初始化操作只需要被创建一次
        init();
    }

    public View getChild(){

        return textView;
    }

    private void init() {
        textView = new TextView(context);

        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.RED);
        textView.setText("这是测试页面1");
    }
}
