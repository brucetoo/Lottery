package com.bruce.Lottery.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce
 * Data 2014/8/19
 * Time 11:22.
 */
public class ShoppingCart {
    private static ShoppingCart instance = new ShoppingCart();
    private ShoppingCart(){}
    public static ShoppingCart getInstance(){
        return  instance;
    }

    // 投注
    // lotteryid string * 玩法编号
    // issue string * 期号（当前销售期）
    // lotterycode string * 投注号码，注与注之间^分割
    // lotterynumber string 注数
    // lotteryvalue string 方案金额，以分为单位

    // appnumbers string 倍数
    // issuesnumbers string 追期
    // issueflag int * 是否多期追号 0否，1多期
    // bonusstop int * 中奖后是否停止：0不停，1停

    private Integer lotteryid;
    private String issue;
    private List<Ticket> tickets = new ArrayList<Ticket>();//投注信息
    private Integer lotterynumber;
    private Integer lotteryvalue;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public Integer getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(Integer lotteryid) {
        this.lotteryid = lotteryid;
    }

    /**
     * 只有get方法，方便向里面添加投注信息（不止有一次投注）
     *
     * @return
     */
    public List<Ticket> getTickets() {
        return tickets;
    }

    public Integer getLotterynumber() {
        lotterynumber = 0;
        for (Ticket ticket : tickets) {
            lotterynumber += ticket.getNum();
        }
        return lotterynumber;
    }

    public Integer getLotteryvalue() {

        return getLotterynumber()*2;
    }
}
