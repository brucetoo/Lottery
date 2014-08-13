package com.bruce.Lottery.View.manager;

import android.content.Context;
import android.view.View;

/**
 * Created by Bruce
 * Data 2014/8/13
 * Time 9:07.
 * 所以中间界面的基类
 */
public abstract class BaseUI {

    protected Context context;
    protected BaseUI(Context context) {
        this.context = context;
    }

    /**
     * 中间界面的内容填充view
     * @return
     */
    public abstract View getChild();
}
