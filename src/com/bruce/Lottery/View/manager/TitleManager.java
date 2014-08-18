package com.bruce.Lottery.View.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.SecondUI;
import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Bruce
 * Data 2014/8/12
 * Time 16:33.
 */
public class TitleManager implements Observer{

    /**
     * 单例模式，因为TitleManager只需要被加载一次就OK
     */
    private static TitleManager instance = new TitleManager();
    private TitleManager() {
    }
    public static TitleManager getInstance() {
        return instance;
    }

    private RelativeLayout commonContainer;
    private RelativeLayout loginContainer;
    private RelativeLayout unLoginContainer;


    private ImageView goback;// 返回
    private ImageView help;// 帮助
    private ImageView login;// 登录

    private TextView titleContent;// 标题内容
    private TextView userInfo;// 用户信息
    /**
     * 找到对应的布局文件
     * @param activity
     */
    public void init(Activity activity){
        commonContainer = (RelativeLayout) activity.findViewById(R.id.br_common_container);
        unLoginContainer = (RelativeLayout) activity.findViewById(R.id.br_unlogin_title);
        loginContainer = (RelativeLayout) activity.findViewById(R.id.br_login_title);

        goback = (ImageView) activity.findViewById(R.id.br_title_goback);
        help = (ImageView) activity.findViewById(R.id.br_title_help);
        login = (ImageView) activity.findViewById(R.id.br_title_login);

        titleContent = (TextView) activity.findViewById(R.id.br_title_content);
        userInfo = (TextView) activity.findViewById(R.id.br_top_user_info);

        setListener(activity);
    }

    private void setListener(final Activity activity) {
        goback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // System.out.println("返回键");
                Toast.makeText(activity.getApplicationContext(),"返回",Toast.LENGTH_LONG).show();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               // System.out.println("help");
                Toast.makeText(activity.getApplicationContext(),"帮助",Toast.LENGTH_LONG).show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //点击的时候不能重复的去创建新的UI,因此用字节码的形式
              //  MiddleManager.getInstance().changeUI(new SecondUI(activity.getApplicationContext()));
                MiddleManager.getInstance().changeUI(SecondUI.class);
            }
        });
    }

    /**
     * 隐藏所有Title
     */
    private void initTitle() {
        commonContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.GONE);
        unLoginContainer.setVisibility(View.GONE);
    }

    /**
     * 显示登陆title
     */
    public void showCommonContainer(){
        initTitle();
        commonContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示未登陆title
     */
    public void showLoginContainer(){
        initTitle();
        unLoginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示普通title
     */
    public void showUnLoginContainer(){
        initTitle();
        unLoginContainer.setVisibility(View.VISIBLE);
    }


    /**
     * 显示登陆的标题
     */
    public void showLoginTitle() {
        initTitle();
        loginContainer.setVisibility(View.VISIBLE);

    }

    public void changeTitle(String title) {
        titleContent.setText(title);
    }



    @Override
    public void update(Observable observable, Object data) {
        //data 表示在调用notifyObserver时传递的参数 在此为 view  ID
        if(data!=null && StringUtils.isNumeric(data.toString())){
            switch (Integer.valueOf(data.toString())){
                case ConstantValue.VIEW_HALL:
                    instance.showUnLoginContainer();
                    break;
                case ConstantValue.SECOND_VIEW:
                    instance.showCommonContainer();
                    break;
                case ConstantValue.VIEW_SSQ:
                    instance.showCommonContainer();
                    break;
            }
        }
    }
}
