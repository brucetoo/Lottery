package com.bruce.Lottery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.bruce.Lottery.View.FirstUI;
import com.bruce.Lottery.View.manager.BottomManager;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.View.manager.TitleManager;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private RelativeLayout br_middle;

   /* Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //changeUI();
            changeUI(new SecondUI(MainActivity.this));

        }
    };*/
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
        MiddleManager.getInstance().setBr_middle(br_middle);
        MiddleManager.getInstance().changeUI(FirstUI.class);
      //  loadFirstUI();
      //  handler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public void onBackPressed() {
        boolean canBack = MiddleManager.getInstance().goBack();
        if(!canBack) {
            super.onBackPressed();
        }
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
