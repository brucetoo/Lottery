package com.bruce.Lottery;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import com.bruce.Lottery.View.Hall;
import com.bruce.Lottery.View.manager.BottomManager;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.View.manager.TitleManager;
import com.bruce.Lottery.utils.PromptManager;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private RelativeLayout br_middle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        TitleManager manager = TitleManager.getInstance();
        manager.init(this);
        manager.showUnLoginContainer();

        BottomManager bottomManager = BottomManager.getInstrance();
        bottomManager.init(this);
        bottomManager.showCommonBottom();

        br_middle = (RelativeLayout) findViewById(R.id.br_middle);
        MiddleManager middleManager = MiddleManager.getInstance();
        middleManager.setBr_middle(br_middle);
        //建立观察者和被观察者间的关系
        middleManager.addObserver(manager);
        middleManager.addObserver(bottomManager);

        middleManager.changeUI(Hall.class);



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean result = MiddleManager.getInstance().goBack();
            // 返回键操作失败
            if (!result) {
                //Toast.makeText(MainActivity.this, "是否退出系统", 1).show();
                PromptManager.showExitSystem(this);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
  View first;
    private void loadFirstUI() {
        FirstUI firstUI = new FirstUI(this);
        first = firstUI.getChild();
        br_middle.addView(firstUI.getChild());
    }


    private void loadSecondUI() {
        SecondUI secondUI = new SecondUI(this);

        View child = secondUI.getChild();
        br_middle.addView(child);
       // FadeUtil.FadeIn(child,2000,2000);
        AnimationController.scaleRotateIn(child,2000,2000);
    }

*/
/*
    *//**
     * 切换界面
     *//*
    protected void changeUI(BaseUI  ui) {
        // 切换界面的核心代码
        br_middle.removeAllViews();
        View view = ui.getChild();
         br_middle.addView(view);
      //  FadeUtil.FadeOut(first,2000);
        AnimationController.slideFadeIn(view, 2000, 0);
     //   loadSecondUI();
    }*/
}
