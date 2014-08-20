package com.bruce.Lottery.View;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.GlobalParams;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.bean.Oelement;
import com.bruce.Lottery.bean.ShoppingCart;
import com.bruce.Lottery.bean.Ticket;
import com.bruce.Lottery.bean.User;
import com.bruce.Lottery.engine.UserEngine;
import com.bruce.Lottery.net.protocol.Message;
import com.bruce.Lottery.net.protocol.element.BetElement;
import com.bruce.Lottery.utils.BeanFactory;
import com.bruce.Lottery.utils.PromptManager;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Bruce
 * Data 2014/8/19
 * Time 19:28.
 */
public class PreBet extends BaseUI {
    // 通用三步

    // ①填充ListView
    // ②提示信息处理
    // ③倍投和追期的设置
    // ④立即购买

    private TextView bettingNum;// 注数
    private TextView bettingMoney;// 金额

    private Button subAppnumbers;// 减少倍投
    private TextView appnumbersInfo;// 倍数
    private Button addAppnumbers;// 增加倍投

    private Button subIssueflagNum;// 减少追期
    private TextView issueflagNumInfo;// 追期
    private Button addIssueflagNum;// 增加追期

    private ImageButton lotteryPurchase;// 投注
    private ListView shoppingList;// 购物车展示

    private ShoppingAdapter adapter;

    public PreBet(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() {
        showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_play_prefectbetting, null);

        bettingNum = (TextView) showInMiddle.findViewById(R.id.ii_shopping_list_betting_num);
        bettingMoney = (TextView) showInMiddle.findViewById(R.id.ii_shopping_list_betting_money);

        subAppnumbers = (Button) showInMiddle.findViewById(R.id.ii_sub_appnumbers);
        appnumbersInfo = (TextView) showInMiddle.findViewById(R.id.ii_appnumbers);
        addAppnumbers = (Button) showInMiddle.findViewById(R.id.ii_add_appnumbers);

        subIssueflagNum = (Button) showInMiddle.findViewById(R.id.ii_sub_issueflagNum);
        issueflagNumInfo = (TextView) showInMiddle.findViewById(R.id.ii_issueflagNum);
        addIssueflagNum = (Button) showInMiddle.findViewById(R.id.ii_add_issueflagNum);

        lotteryPurchase = (ImageButton) showInMiddle.findViewById(R.id.ii_lottery_purchase);
        shoppingList = (ListView) showInMiddle.findViewById(R.id.ii_lottery_shopping_list);

        adapter = new ShoppingAdapter();
        shoppingList.setAdapter(adapter);

    }

    @Override
    public void setListener() {
        // 倍数
        addAppnumbers.setOnClickListener(this);
        subAppnumbers.setOnClickListener(this);
        // 追期
        addIssueflagNum.setOnClickListener(this);
        subIssueflagNum.setOnClickListener(this);
        // 投注
        lotteryPurchase.setOnClickListener(this);

    }

    @Override
    public int getID() {

        return ConstantValue.VIEW_PREBET;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_add_appnumbers:
                // 增加倍数
                if(ShoppingCart.getInstance().addAppnumber(true)){
                    changeNotice();
                };
                break;
            case R.id.ii_sub_appnumbers:
                // 减少倍数
                if(ShoppingCart.getInstance().addAppnumber(false)){
                    changeNotice();
                }
                break;
            case R.id.ii_add_issueflagNum:
                // 增加追期
                if(ShoppingCart.getInstance().addIssuesnumbers(true)){
                    changeNotice();
                }
                break;
            case R.id.ii_sub_issueflagNum:
                // 减少追期
                if(ShoppingCart.getInstance().addIssuesnumbers(false)){
                    changeNotice();
                }
                break;
            case R.id.ii_lottery_purchase:
                // 投注请求
                User user=new User();
                user.setUserName(GlobalParams.USERNAME);
                new MyHttpTask<User>() {

                    @Override
                    protected Message doInBackground(User... params) {
                        UserEngine engine= BeanFactory.getImpl(UserEngine.class);
                        return engine.bet(params[0]);
                    }
                    protected void onPostExecute(Message result) {
                        if(result!=null)
                        {
                            Oelement oelement = result.getBody().getOelement();
                            if(ConstantValue.SUCCESS.equals(oelement.getErrorode()))
                            {
                                BetElement element=(BetElement) result.getBody().getElements().get(0);
                                // 修改用户的余额信息
                                GlobalParams.MONEY=Float.parseFloat(element.getActvalue());
                                // 清理返回键
                                MiddleManager.getInstance().clear();
                                // 跳转到购彩大厅，提示对话框
                                MiddleManager.getInstance().changeUI(Hall.class);
                                PromptManager.showErrorDialog(context, "投注成功！");

                                // 清空购物车
                                ShoppingCart.getInstance().clear();
                            }
                        }


                    }
                }.executeProxy(user);
                break;
        }
    }

    @Override
    public void onResume() {
        changeNotice();
        super.onResume();
    }

    private void changeNotice() {
        Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
        Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();
        // GlobalParams.MONEY;

        String number = context.getResources().getString(R.string.is_shopping_list_betting_num);
        String money = context.getResources().getString(R.string.is_shopping_list_betting_money);

        number = StringUtils.replace(number, "NUM", lotterynumber.toString());
        money = StringUtils.replaceEach(money, new String[]{"MONEY1", "MONEY2"}, new String[]{lotteryvalue.toString(), String.valueOf(GlobalParams.MONEY)});

        // private TextView bettingNum;// 注数
        // private TextView bettingMoney;// 金额
        bettingNum.setText(Html.fromHtml(number));
        bettingMoney.setText(Html.fromHtml(money));

        // 修改倍数和追期
        appnumbersInfo.setText(ShoppingCart.getInstance().getAppnumbers().toString());
        issueflagNumInfo.setText(ShoppingCart.getInstance().getIssuesnumbers().toString());
    }

    private class ShoppingAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ShoppingCart.getInstance().getTickets().size();
        }

        @Override
        public Object getItem(int position) {
            return ShoppingCart.getInstance().getTickets().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.il_play_prefectbetting_row, null);

                holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
                holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
            holder.redNum.setText(ticket.getRedNum());
            holder.blueNum.setText(ticket.getBlueNum());

            return convertView;
        }

        class ViewHolder {
            TextView redNum;
            TextView blueNum;
        }

    }

}
