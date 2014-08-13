package com.bruce.Lottery.View.manager;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import com.bruce.Lottery.utils.AnimationController;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruce
 * Data 2014/8/13
 * Time 9:51.
 * 中间容器的管理类
 */
public class MiddleManager {

    //单例模式！在 find id的时候就实例化
    private static MiddleManager instance = new MiddleManager();
    private MiddleManager(){};

    public static MiddleManager getInstance() {
        return instance;
    }

    //获取中间容器
    private RelativeLayout br_middle;

   //在mainactivity中设置值，单例能保证以后都不变
    public void setBr_middle(RelativeLayout br_middle) {
        this.br_middle = br_middle;
    }



    private Map<String,BaseUI> uiCache = new HashMap<String, BaseUI>();//存放创建的ＵＩ界面

    private BaseUI currentUI; //当前显示的UI

    /**
     * 切换界面
     * 中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个
     */
    public void changeUI(Class<? extends BaseUI>  targetClazz) {

        //判断现在显示的页面是否和目标切换UI相同
        if(currentUI!=null && targetClazz.getSimpleName().equals(currentUI.getClass().getSimpleName())){
            return;
        }
        // 切换界面的核心代码
        BaseUI targetUI = null;
        //将创建的View保存到list中，每次changeUI的时候都判定一下是否存在
        //存在就考虑重用
        String key = targetClazz.getSimpleName();
        if(uiCache.containsKey(key)){
            //存在就重用
            targetUI = uiCache.get(key);
        }else{
            //不存在就创建
            try {
                //  targetUI = targetClazz.newInstance(); 该方法是调用无参的构造方法
                //调用有参数的构造方法
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                //存储创建过的 UI
                uiCache.put(key,targetUI);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------targetUI:"+targetUI.toString());
        br_middle.removeAllViews();
        View view = targetUI.getChild();
        br_middle.addView(view);
        AnimationController.slideFadeIn(view, 2000, 0);
        //页面切换成功后，设置正在显示的页面
        currentUI = targetUI;
    }

    /**
     * 切换界面 字节码 传递,而且还必须是BaseUI的子类
     * 在标题容器中每次点击都在创建一个目标界面
     */
    public void changeUI2(Class<? extends BaseUI>  targetClazz) {
        // 切换界面的核心代码
         BaseUI targetUI = null;
        //将创建的View保存到list中，每次changeUI的时候都判定一下是否存在
        //存在就考虑重用
        String key = targetClazz.getSimpleName();
        if(uiCache.containsKey(key)){
            //存在就重用
            targetUI = uiCache.get(key);
        }else{
            //不存在就创建
            try {
              //  targetUI = targetClazz.newInstance(); 该方法是调用无参的构造方法
              //调用有参数的构造方法
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                //存储创建过的 UI
                uiCache.put(key,targetUI);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------targetUI:"+targetUI.toString());
        br_middle.removeAllViews();
        View view = targetUI.getChild();
        br_middle.addView(view);
        AnimationController.slideFadeIn(view, 2000, 0);
    }

    private Context getContext() {
        return br_middle.getContext();
    }


    /**
     * 切换界面test
     */
    public void changeUI1(BaseUI  ui) {
        // 切换界面的核心代码
        br_middle.removeAllViews();
        View view = ui.getChild();
        br_middle.addView(view);
        AnimationController.slideFadeIn(view, 2000, 0);
    }
}
