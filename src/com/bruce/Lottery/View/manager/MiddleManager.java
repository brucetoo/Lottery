package com.bruce.Lottery.View.manager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import com.bruce.Lottery.View.Hall;
import com.bruce.Lottery.utils.AnimationController;
import com.bruce.Lottery.utils.MemoryManager;
import com.bruce.Lottery.utils.PromptManager;
import com.bruce.Lottery.utils.SoftMap;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Bruce
 * Data 2014/8/13
 * Time 9:51.
 * 中间容器的管理类
 */
public class MiddleManager extends Observable{

    //单例模式！在 find id的时候就实例化
    private static MiddleManager instance = new MiddleManager();

    private MiddleManager() {};

    public static MiddleManager getInstance() {
        return instance;
    }

    //获取中间容器
    private RelativeLayout br_middle;

    //在mainactivity中设置值，单例能保证以后都不变
    public void setBr_middle(RelativeLayout br_middle) {
        this.br_middle = br_middle;
    }

    /**
     * 存放UI界面，String UI类名  BaseUI UI类型
     */
    private static Map<String, BaseUI> uiCache;//= new HashMap<String, BaseUI>();//存放创建的ＵＩ界面

    //1.将占用内存的UI添加到袋子（套上软引用）
    //2.GC回收后，清空对应的袋子
    static{
        //内存充足就用HashMap 不充足就用SoftMap
        if(MemoryManager.hasAcailMemory()){
            uiCache = new HashMap<String, BaseUI>();
        }else{
            uiCache = new SoftMap<String, BaseUI>();
        }

    }

    private BaseUI currentUI; //当前显示的UI


    /**
     * 带数据传递的ChangeUI
     * @param targetClazz
     * @param bundle
     */
    public void changeUI(Class<? extends BaseUI> targetClazz, Bundle bundle) {

        //判断现在显示的页面是否和目标切换UI相同
        if (currentUI != null && targetClazz.getSimpleName().equals(currentUI.getClass().getSimpleName())) {
            return;
        }
        // 切换界面的核心代码
        BaseUI targetUI = null;
        //将创建的View保存到list中，每次changeUI的时候都判定一下是否存在
        //存在就考虑重用
        String key = targetClazz.getSimpleName();
        if (uiCache.containsKey(key)) {
            //存在就重用
            targetUI = uiCache.get(key);
        } else {
            //不存在就创建
            try {
                //  targetUI = targetClazz.newInstance(); 该方法是调用无参的构造方法
                //调用有参数的构造方法
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                //存储创建过的 UI
                uiCache.put(key, targetUI);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------targetUI:" + targetUI.toString());


        if(targetUI != null){
            targetUI.setBundle(bundle);
        }

        //在移除之前的页面前。 ---onPause方法，
        if(currentUI != null){
            currentUI.onPause();
        }
        br_middle.removeAllViews();
        View view = targetUI.getChild();
        br_middle.addView(view);
        AnimationController.slideFadeIn(view, 2000, 0);

        //在添加页面成功够 -----onResume方法
        targetUI.onResume();

        //页面切换成功后，设置正在显示的页面  ---用于判断是否同一个UI
        currentUI = targetUI;
        //切换成功，保存到操作到栈顶---用于返回键的操作
        history.addFirst(key);


        //切换页面成功后，需要改变title和bottom ---用 观察者模式
        changeTitleAndBottom();

    }

    /**
     * 对外抛出方法，使外面能获取到当前显示的UI
     * @return
     */
    public BaseUI getCurrentUI() {
        return currentUI;
    }

    /**
     * 切换界面
     * 中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个
     */
    public void changeUI(Class<? extends BaseUI> targetClazz) {

        //判断现在显示的页面是否和目标切换UI相同
        if (currentUI != null && targetClazz.getSimpleName().equals(currentUI.getClass().getSimpleName())) {
            return;
        }
        // 切换界面的核心代码
        BaseUI targetUI = null;
        //将创建的View保存到list中，每次changeUI的时候都判定一下是否存在
        //存在就考虑重用
        String key = targetClazz.getSimpleName();
        if (uiCache.containsKey(key)) {
            //存在就重用
            targetUI = uiCache.get(key);
        } else {
            //不存在就创建
            try {
                //  targetUI = targetClazz.newInstance(); 该方法是调用无参的构造方法
                //调用有参数的构造方法
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                //存储创建过的 UI
                uiCache.put(key, targetUI);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------targetUI:" + targetUI.toString());

        //在移除之前的页面前。 ---onPause方法，
        if(currentUI != null){
            currentUI.onPause();
        }
        br_middle.removeAllViews();
        View view = targetUI.getChild();
        br_middle.addView(view);
        AnimationController.slideFadeIn(view, 2000, 0);

        //在添加页面成功够 -----onResume方法
        targetUI.onResume();

        //页面切换成功后，设置正在显示的页面  ---用于判断是否同一个UI
        currentUI = targetUI;
        //切换成功，保存到操作到栈顶---用于返回键的操作
        history.addFirst(key);


        //切换页面成功后，需要改变title和bottom ---用 观察者模式
        changeTitleAndBottom();
    }

    private void changeTitleAndBottom() {
        // 降低三个容器的耦合度
        // 当中间容器变动的时候，中间容器“通知”其他的容器，你们该变动了，唯一的标示传递，其他容器依据唯一标示进行容器内容的切换
        // ①将中间容器变成被观察的对象
        // ②标题和底部导航变成观察者
        // ③建立观察者和被观察者之间的关系（标题和底部导航添加到观察者的容器里面）
        // ④一旦中间容器变动，修改boolean，然后通知所有的观察者.updata()
        setChanged();
        notifyObservers(currentUI.getID());

    }
    /**
     * 用户按返回键时候切换View
     *
     * @return
     */
    public boolean goBack() {
        if (history.size() > 0) {
            //只有一个时 不删除（用户误操作可能）
            if (history.size() == 1) {
                return false;
            }
            //移除栈顶第一个
            history.removeFirst();

            if (history.size() > 0) {
                //得到移除后的第一个
                String key = history.getFirst();
                BaseUI targetUI = uiCache.get(key);

                //因为用了软引用，uiCache中前面的界面可能被ＧＣ
                if(targetUI != null) {
                    targetUI.onPause();
                    br_middle.removeAllViews();
                    br_middle.addView(targetUI.getChild());
                    targetUI.onResume();

                    currentUI = targetUI; //将正在显示的UI 改变
                    changeTitleAndBottom();
                }else{
                    //界面被回收以后,可跳转到一个不需要传递数据的view中
                    changeUI(Hall.class);
                    PromptManager.showToast(getContext(),"应用崩溃，自动跳到大厅页");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 切换界面 字节码 传递,而且还必须是BaseUI的子类
     * 在标题容器中每次点击都在创建一个目标界面
     */
    public void changeUI2(Class<? extends BaseUI> targetClazz) {
        // 切换界面的核心代码
        BaseUI targetUI = null;
        //将创建的View保存到list中，每次changeUI的时候都判定一下是否存在
        //存在就考虑重用
        String key = targetClazz.getSimpleName();
        if (uiCache.containsKey(key)) {
            //存在就重用
            targetUI = uiCache.get(key);
        } else {
            //不存在就创建
            try {
                //  targetUI = targetClazz.newInstance(); 该方法是调用无参的构造方法
                //调用有参数的构造方法
                Constructor<? extends BaseUI> constructor = targetClazz.getConstructor(Context.class);
                targetUI = constructor.newInstance(getContext());
                //存储创建过的 UI
                uiCache.put(key, targetUI);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------targetUI:" + targetUI.toString());
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
    public void changeUI1(BaseUI ui) {
        // 切换界面的核心代码
        br_middle.removeAllViews();
        View view = ui.getChild();
        br_middle.addView(view);
        AnimationController.slideFadeIn(view, 2000, 0);
    }

    //String 代表 uiCache中的key
    private LinkedList<String> history = new LinkedList<String>();//用户界面操作的记录


    public void clear() {

        history.clear();
    }
}
