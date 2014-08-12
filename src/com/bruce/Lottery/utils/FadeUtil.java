package com.bruce.Lottery.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by bruce-too
 * on 2014/8/12
 * Time 23:01.
 * UI的淡入淡出效果
 */
public class FadeUtil {


    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            View view = (View) msg.obj;
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
    };

    /**
     * 淡出
     *
     * @param view
     * @param duration
     */
    public static void FadeOut(final View view, int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Message msg = Message.obtain();
                msg.obj = view;
                handler.sendMessage(msg);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(alphaAnimation);
    }

    /**
     * @param view
     * @param delay    延迟
     * @param duration
     */
    public static void FadeIn( View view, int delay, int duration) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        //设置延迟
        alphaAnimation.setStartOffset(delay);
        alphaAnimation.setDuration(duration);
        view.setAnimation(alphaAnimation);
    }
}
