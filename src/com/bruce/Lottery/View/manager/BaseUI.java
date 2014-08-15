package com.bruce.Lottery.View.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.utils.NetUtil;
import com.bruce.Lottery.utils.PromptManager;

/**
 * Created by Bruce
 * Data 2014/8/13
 * Time 9:07.
 * 所以中间界面的基类
 */
public abstract class BaseUI implements View.OnClickListener {

    public Context context;
    //将要在中间显示的布局文件放在父类中
    public ViewGroup showInMiddle;

    public BaseUI(Context context) {
        this.context = context;
        init();
        setListener();
    }

    /**
     * 设置界面点击事件
     */
    protected abstract void setListener();

    /**
     * 初始化界面
     */
    protected abstract void init();

    /**
     * 中间界面的内容填充view
     *
     * @return
     */
    public View getChild() {
        //在容器中布局返回前，设置器LayoutParam
        //在自定义的VIew用inflate填充时，可能会出现屏幕没填满
        //原因是root == null 导致了   layoutMiddle.getLayoutParams() == null
        if (showInMiddle.getLayoutParams() == null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            showInMiddle.setLayoutParams(params);
        }
        return showInMiddle;
    }

    /**
     * 给每个VIEW设定一个唯一的标示
     */
    public abstract int getID();

    /**
     * 删除页面前调用
     */
    public void onPause(){
    }


    /**
     * 添加页面后调用
     */
    public void onResume(){
    }


    /**
     * 异步检查联网的抽象类
     * @param <Parma> 标示实例化 异步联网时 传递的参数类型
     */
    public abstract class MyHttpTask<Parma> extends AsyncTask<Parma,Void,Message> {
        /**
         * 类似于Thread中的start的方法，但是excute方法不能重写，所以只能对其改名，然后调用父类的excute方法
         * @param params
         * @return
         */
        public final AsyncTask<Parma,Void,Message> executeProxy(Parma... params) {
            if(NetUtil.checkNet(context)){
                return super.execute(params);
            }else{
                PromptManager.showNoNetWork(context);
            }
            return null;
        }
    }
}
