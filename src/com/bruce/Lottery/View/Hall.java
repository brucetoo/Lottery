package com.bruce.Lottery.View;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;

/**
 * Created by bruce-too
 * on 2014/8/13
 * Time 22:55.
 */
public class Hall extends BaseUI {
    public Hall(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {

    }

    public void init() {
        showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall1, null);
    }


    @Override
    public int getID() {
        return ConstantValue.VIEW_HALL;
    }

    @Override
    public void onClick(View v) {

    }
}
