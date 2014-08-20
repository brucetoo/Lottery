package com.bruce.Lottery.View;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.GlobalParams;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.bean.Oelement;
import com.bruce.Lottery.bean.User;
import com.bruce.Lottery.engine.UserEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.BalanceElement;
import com.bruce.Lottery.utils.BeanFactory;
import com.bruce.Lottery.utils.PromptManager;

/**
 * Created by Bruce
 * Data 2014/8/19
 * Time 19:22.
 */
public class Login extends BaseUI {

    private EditText username;
    private ImageView clear;//情况按钮
    private EditText password;
    private Button login;

    public Login(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        clear.setOnClickListener(this);
        login.setOnClickListener(this);
        //用户输入时，显示清除图片按钮
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(username.getText())) {
                    clear.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void init() {
        showInMiddle = (android.view.ViewGroup) View.inflate(context, R.layout.il_user_login, null);
        username = (EditText) showInMiddle.findViewById(R.id.ii_user_login_username);
        clear = (ImageView) showInMiddle.findViewById(R.id.ii_clear);
        password = (EditText) showInMiddle.findViewById(R.id.ii_user_login_password);
        login = (Button) showInMiddle.findViewById(R.id.ii_user_login);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_LOGIN;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_clear:
                username.setText("");
                clear.setVisibility(View.INVISIBLE);
                break;
            case R.id.ii_user_login:
                // 用户输入信息
                if (checkUserInfo()) {
                    // 登录
                    User user = new User();
                    user.setUserName(username.getText().toString());
                    user.setPassword(password.getText().toString());

                    new MyHttpTask<User>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            PromptManager.showProgressDialog(context);
                        }

                        @Override
                        protected Message doInBackground(User... params) {
                            UserEngine engine = BeanFactory.getImpl(UserEngine.class);
                            Message result = engine.login(params[0]);
                            Oelement oelement;
                            if(result != null){
                                String errorode = result.getBody().getOelement().getErrorode();
                                if(ConstantValue.SUCCESS.equals(errorode)){
                                    //登陆成功
                                    GlobalParams.isLogin = true;
                                    GlobalParams.USERNAME = params[0].getUserName();
                                    // 成功了获取余额
                                    Message balance = engine.getBalance(params[0]);
                                    if (balance != null) {
                                        oelement = balance.getBody().getOelement();
                                        if (ConstantValue.SUCCESS.equals(oelement.getErrorode())) {
                                            BalanceElement element = (BalanceElement) balance.getBody().getElements().get(0);
                                            GlobalParams.MONEY = Float.parseFloat(element.getInvestvalues());
                                            System.out.println("--------------------"+GlobalParams.MONEY);
                                            return balance;
                                        }
                                    }
                                }
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Message message) {
                            super.onPostExecute(message);
                            PromptManager.closeProgressDialog();
                            if(message != null){
                                //跳转页面
                                PromptManager.showToast(context,"登陆成功......");
                                MiddleManager.getInstance().goBack();
                            }else{
                                PromptManager.showToast(context,"服务器忙......");
                            }
                        }
                    }.executeProxy(user);
                }
                break;
        }
    }

    private boolean checkUserInfo() {
        return true;
    }
}
