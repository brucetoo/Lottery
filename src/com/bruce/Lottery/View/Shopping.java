package com.bruce.Lottery.View;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bruce.Lottery.ConstantValue;
import com.bruce.Lottery.GlobalParams;
import com.bruce.Lottery.R;
import com.bruce.Lottery.View.manager.BaseUI;
import com.bruce.Lottery.View.manager.MiddleManager;
import com.bruce.Lottery.bean.ShoppingCart;
import com.bruce.Lottery.bean.Ticket;
import com.bruce.Lottery.utils.PromptManager;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Bruce
 * Data 2014/8/19
 * Time 11:41.
 */
public class Shopping extends BaseUI {

    private Button addOptional;// 添加自选
    private Button addRandom;// 添加机选

    private ListView shoppingList;// 用户选择信息列表

    private ImageButton shoppingListClear;// 清空购物车
    private TextView notice;// 提示信息
    private Button buy;// 购买

    private ShoppingAdapter adapter;

    public Shopping(Context context) {
        super(context);
    }

    @Override
    protected void setListener() {
        addOptional.setOnClickListener(this);
        addRandom.setOnClickListener(this);
        shoppingListClear.setOnClickListener(this);
        buy.setOnClickListener(this);
    }

    @Override
    protected void init() {
        showInMiddle = (android.view.ViewGroup) View.inflate(context, R.layout.il_shopping, null);
        addOptional = (Button) showInMiddle.findViewById(R.id.ii_add_optional);
        addRandom = (Button) showInMiddle.findViewById(R.id.ii_add_random);
        shoppingListClear = (ImageButton) showInMiddle.findViewById(R.id.ii_shopping_list_clear);
        notice = (TextView) showInMiddle.findViewById(R.id.ii_shopping_lottery_notice);
        buy = (Button) showInMiddle.findViewById(R.id.ii_lottery_shopping_buy);
        shoppingList = (ListView) showInMiddle.findViewById(R.id.ii_shopping_list);

        adapter = new ShoppingAdapter();
        shoppingList.setAdapter(adapter);
    }

    @Override
    public int getID() {
        return ConstantValue.VIEW_SHOPPING;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ii_add_optional: //自选
                MiddleManager.getInstance().changeUI(playSSQ.class);
                break;
            case R.id.ii_add_random: //机选
                addRandom();
                noticeChange();
                break;
            case R.id.ii_shopping_list_clear: //清空
                ShoppingCart.getInstance().getTickets().clear();
                adapter.notifyDataSetChanged();
                noticeChange();
                break;
            case R.id.ii_lottery_shopping_buy://购买
                //1.判断是否投注
                if(ShoppingCart.getInstance().getTickets().size()>=1){
                    //2.判断是否登录
                    if(GlobalParams.isLogin){
                         //3.判断余额是否够
                        if(ShoppingCart.getInstance().getTickets().size()>GlobalParams.MONEY){
                            MiddleManager.getInstance().changeUI(PreBet.class,bundle);
                        }else {
                            //3.提示用户充值
                            PromptManager.showToast(context,"充值去");
                        }
                    }else{
                        //2.提示用户登录
                        PromptManager.showToast(context,"登录去");
                        MiddleManager.getInstance().changeUI(Login.class,bundle);
                    }
                }else{
                    //1.提示用户选择
                    PromptManager.showToast(context,"至少选择一注！");
                }
                break;
        }
    }

    /**
     * 点击随机添加一注双色球
     */
    private void addRandom() {
        //机选双色球
        Random random = new Random();
        List<Integer> redNum = new ArrayList<Integer>();
        List<Integer> blueNum = new ArrayList<Integer>();
        while (redNum.size() < 6) {
            int num = random.nextInt(33) + 1;
            if (!redNum.contains(num)) {
                redNum.add(num);
            }
        }
        int num = random.nextInt(6) + 1;
        blueNum.add(num);

        //封装投注信息
        Ticket ticket = new Ticket();
        DecimalFormat format = new DecimalFormat("00");
        StringBuffer buffer = new StringBuffer();
        for (Integer item : redNum){
            buffer.append(" ").append(format.format(item));
        }
        StringBuffer buffer1 = new StringBuffer();
        for (Integer item : blueNum){
            buffer1.append(" ").append(format.format(item));
        }
        ticket.setRedNum(buffer.substring(1));
        ticket.setBlueNum(buffer1.substring(1));
        ticket.setNum(1);

        ShoppingCart.getInstance().getTickets().add(ticket);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        noticeChange();
    }

    /**
     * 修改集合 size 就调用
     */

    private void noticeChange(){
        Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
        Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();
       // String noticeInfo = context.getResources().getString(R.string.is_shopping_list_notice);
        String noticeInfo = "注数: NUM 注 金额: MONEY 元";
        noticeInfo = StringUtils.replaceEach(noticeInfo,
                                             new String[]{"NUM","MONEY"},new String[]{lotterynumber.toString(),lotteryvalue.toString()});
        //notice.setText(Html.fromHtml(noticeInfo));
        SpannableString sp = new SpannableString(noticeInfo);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")),
                   noticeInfo.indexOf("数: ")+2 ,noticeInfo.indexOf(" 注"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")),
                   noticeInfo.indexOf("额: ")+2 ,noticeInfo.indexOf(" 元"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE );
        notice.setText(sp);
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
                convertView = View.inflate(context, R.layout.il_shopping_item, null);

                holder.delete = (ImageButton) convertView.findViewById(R.id.ii_shopping_item_delete);
                holder.redNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
                holder.blueNum = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
                holder.num = (TextView) convertView.findViewById(R.id.ii_shopping_item_money);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
            holder.redNum.setText(ticket.getRedNum());
            holder.blueNum.setText(ticket.getBlueNum());
            holder.num.setText(ticket.getNum() + "注 "+ticket.getNum()*2+"元");

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCart.getInstance().getTickets().remove(position);
                    //ListView有增减项时用,没有则局部更新
                    notifyDataSetChanged();
                    noticeChange();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageButton delete;
            TextView redNum;
            TextView blueNum;
            TextView num;
        }
    }
}
