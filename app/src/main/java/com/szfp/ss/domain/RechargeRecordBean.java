package com.szfp.ss.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * author：ct on 2017/9/14 18:03
 * email：cnhttt@163.com
 */

/**
 * 充值 记录
 */
@Entity
public class RechargeRecordBean implements Serializable {
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private Long id;
    private Long userId; //用户ID
    private String lastName;
    private String firstName;

    private String rechargeAmount;//充值交易的金额
    private String tradeType ; //交易类型 1-充值 2-购买·····


    //2
    private String  twoCashAmount  ="0";
    private String  twoCardAmount  ="0";
    private double  twoAmount;
    private  int twoBuyType;
    private int     twoBuyNum;
    private   String   twoBuyName;
    private long createTime;
    private long createDayTime;
    private String UUID;
    @Generated(hash = 1218791956)
    public RechargeRecordBean(Long id, Long userId, String lastName,
            String firstName, String rechargeAmount, String tradeType,
            String twoCashAmount, String twoCardAmount, double twoAmount,
            int twoBuyType, int twoBuyNum, String twoBuyName, long createTime,
            long createDayTime, String UUID) {
        this.id = id;
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.rechargeAmount = rechargeAmount;
        this.tradeType = tradeType;
        this.twoCashAmount = twoCashAmount;
        this.twoCardAmount = twoCardAmount;
        this.twoAmount = twoAmount;
        this.twoBuyType = twoBuyType;
        this.twoBuyNum = twoBuyNum;
        this.twoBuyName = twoBuyName;
        this.createTime = createTime;
        this.createDayTime = createDayTime;
        this.UUID = UUID;
    }
    @Generated(hash = 1096484208)
    public RechargeRecordBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getRechargeAmount() {
        return this.rechargeAmount;
    }
    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
    public String getTradeType() {
        return this.tradeType;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
    public String getTwoCashAmount() {
        return this.twoCashAmount;
    }
    public void setTwoCashAmount(String twoCashAmount) {
        this.twoCashAmount = twoCashAmount;
    }
    public String getTwoCardAmount() {
        return this.twoCardAmount;
    }
    public void setTwoCardAmount(String twoCardAmount) {
        this.twoCardAmount = twoCardAmount;
    }
    public int getTwoBuyType() {
        return this.twoBuyType;
    }
    public void setTwoBuyType(int twoBuyType) {
        this.twoBuyType = twoBuyType;
    }
    public int getTwoBuyNum() {
        return this.twoBuyNum;
    }
    public void setTwoBuyNum(int twoBuyNum) {
        this.twoBuyNum = twoBuyNum;
    }
    public String getTwoBuyName() {
        return this.twoBuyName;
    }
    public void setTwoBuyName(String twoBuyName) {
        this.twoBuyName = twoBuyName;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getCreateDayTime() {
        return this.createDayTime;
    }
    public void setCreateDayTime(long createDayTime) {
        this.createDayTime = createDayTime;
    }
    public String getUUID() {
        return this.UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    public double getTwoAmount() {
        return this.twoAmount;
    }
    public void setTwoAmount(double twoAmount) {
        this.twoAmount = twoAmount;
    }
   

}
