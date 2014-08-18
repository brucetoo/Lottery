package com.bruce.Lottery.View.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.bruce.Lottery.R;
import com.bruce.Lottery.utils.DensityUtil;

/**
 * Created by Bruce
 * Data 2014/8/18
 * Time 14:36.
 */
public class MyGridView extends GridView {

    private PopupWindow pop;
    private TextView ball;
    private OnActionUpListener onActionUpListener;

    //实例中 设置监听
    public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
        this.onActionUpListener = onActionUpListener;
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // ①手指按下
        // 显示放大的号码
        // ②手指滑动
        // 更新：号码内容+显示位置
        // ③手指抬起
        // 修改手指下面的球的背景
        pop = new PopupWindow();
        View view = View.inflate(context, R.layout.il_gridview_item_pop, null);
        ball = (TextView) view.findViewById(R.id.ii_pretextView);
        pop.setContentView(view);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dip ---> px 兼容屏幕
        pop.setWidth(DensityUtil.dip2px(context, 55));
        pop.setHeight(DensityUtil.dip2px(context, 53));

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //获取点击的位置 得到点击的Item
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int position = pointToPosition(x, y);
        //当位置中不是 合法的 item
        if (position == INVALID_POSITION) {
            return false;
        }
        //被点击的Item
        TextView child = (TextView) getChildAt(position);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showPop(child);
                break;
            case MotionEvent.ACTION_MOVE:
                update(child);
                break;
            case MotionEvent.ACTION_UP:
                hidePop();
                //设置一个监听接口，在实例中实现背景点击切换
                if(onActionUpListener != null) {
                    onActionUpListener.OnActionUp(child,position);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 显示PopWindow
     *
     * @param child
     */
    public void showPop(TextView child) {
        //显示位置的获取,设置偏移量
        int yOffset = -(pop.getHeight() + child.getHeight());
        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
        /*int yOffset = -(pop.getHeight() + child.getHeight());
        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;*/
        ball.setText(child.getText());
        //默认pop是显示在child的 bottom_left
        pop.showAsDropDown(child, xOffset, yOffset);
    }

    /**
     * 更新PopWindow位置
     *
     * @param child
     */
    public void update(TextView child) {
        int yOffset = -(pop.getHeight() + child.getHeight());
        int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
        ball.setText(child.getText());
        pop.update(child, xOffset, yOffset, -1, -1);
    }

    /**
     * 隐藏popWindow
     */
    public void hidePop() {
        pop.dismiss();
    }

    /**
     * 监听用户点击完成的操作
     */
    public interface OnActionUpListener {
        void OnActionUp(View view,int Position);
    }
}
