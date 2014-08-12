package com.bruce.Lottery;

import android.app.Activity;
import android.os.Bundle;
import com.bruce.Lottery.View.manager.BottomManager;
import com.bruce.Lottery.View.manager.TitleManager;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
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
    }
}
