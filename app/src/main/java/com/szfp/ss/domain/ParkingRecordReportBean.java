package com.szfp.ss.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * author：ct on 2017/9/21 18:45
 * email：cnhttt@163.com
 */

@Entity
public class ParkingRecordReportBean implements Serializable {
    static  final  long serialVersionUID=42L;
    @Id(autoincrement = true)
    private Long id;

    private String serialNumber;

    //0停车  1完成
    private int type=0;


    //用户唯一标识
    private String UUID;
    private String cardId;
    private String cardNumber;
    private String lastName;
    private String firstName;
    private String licensePlateNumber;

    //交易前余额
    private double preTradeBalance;
    private double postTradeBalance;
    private long  parkingTimeIsValidEnd;




    /**
     * 0 临时卡    1普通卡
     */
    private int pType= 1;
    private long adminId;
    private String  adminNumber;//操作人员编号、

    //终端号
    private  String terminalNumber;
    //停车场编号
    private String parkingNumber;

    //驶入时间
    private long enterTime;
    private long createTime;
    private long createDayTime;

    //驶出时间
    private long exitTime;
    private long exitCreateTime;
    private long exitCreateDayTime;
    //0
    private int deductionMethod=0;
    private double parkingAmount=0;

    private long parkingTime;
    private int  intTime;
    private double cardAmount=0;
    private double cashAmount=0;




    @Generated(hash = 1828365766)
    public ParkingRecordReportBean(Long id, String serialNumber, int type,
            String UUID, String cardId, String cardNumber, String lastName,
            String firstName, String licensePlateNumber, double preTradeBalance,
            double postTradeBalance, long parkingTimeIsValidEnd, int pType,
            long adminId, String adminNumber, String terminalNumber,
            String parkingNumber, long enterTime, long createTime,
            long createDayTime, long exitTime, long exitCreateTime,
            long exitCreateDayTime, int deductionMethod, double parkingAmount,
            long parkingTime, int intTime, double cardAmount, double cashAmount) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.type = type;
        this.UUID = UUID;
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.licensePlateNumber = licensePlateNumber;
        this.preTradeBalance = preTradeBalance;
        this.postTradeBalance = postTradeBalance;
        this.parkingTimeIsValidEnd = parkingTimeIsValidEnd;
        this.pType = pType;
        this.adminId = adminId;
        this.adminNumber = adminNumber;
        this.terminalNumber = terminalNumber;
        this.parkingNumber = parkingNumber;
        this.enterTime = enterTime;
        this.createTime = createTime;
        this.createDayTime = createDayTime;
        this.exitTime = exitTime;
        this.exitCreateTime = exitCreateTime;
        this.exitCreateDayTime = exitCreateDayTime;
        this.deductionMethod = deductionMethod;
        this.parkingAmount = parkingAmount;
        this.parkingTime = parkingTime;
        this.intTime = intTime;
        this.cardAmount = cardAmount;
        this.cashAmount = cashAmount;
    }
    @Generated(hash = 318527304)
    public ParkingRecordReportBean() {
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
    public int getPType() {
        return this.pType;
    }
    public void setPType(int pType) {
        this.pType = pType;
    }
    public long getAdminId() {
        return this.adminId;
    }
    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }
    public String getAdminNumber() {
        return this.adminNumber;
    }
    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }
    public long getEnterTime() {
        return this.enterTime;
    }
    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
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
    public String getCardNumber() {
        return this.cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public String getTerminalNumber() {
        return this.terminalNumber;
    }
    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }
    public String getParkingNumber() {
        return this.parkingNumber;
    }
    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }
    public String getSerialNumber() {
        return this.serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public String getUUID() {
        return this.UUID;
    }
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }
    public String getCardId() {
        return this.cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getLicensePlateNumber() {
        return this.licensePlateNumber;
    }
    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return this.id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getExitTime() {
        return this.exitTime;
    }
    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }
    public long getExitCreateTime() {
        return this.exitCreateTime;
    }
    public void setExitCreateTime(long exitCreateTime) {
        this.exitCreateTime = exitCreateTime;
    }
    public long getExitCreateDayTime() {
        return this.exitCreateDayTime;
    }
    public void setExitCreateDayTime(long exitCreateDayTime) {
        this.exitCreateDayTime = exitCreateDayTime;
    }
    public long getParkingTime() {
        return this.parkingTime;
    }
    public void setParkingTime(long parkingTime) {
        this.parkingTime = parkingTime;
    }
    public double getCardAmount() {
        return this.cardAmount;
    }
    public void setCardAmount(double cardAmount) {
        this.cardAmount = cardAmount;
    }
    public double getCashAmount() {
        return this.cashAmount;
    }
    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }
    public int getDeductionMethod() {
        return this.deductionMethod;
    }
    public void setDeductionMethod(int deductionMethod) {
        this.deductionMethod = deductionMethod;
    }
    public double getParkingAmount() {
        return this.parkingAmount;
    }
    public void setParkingAmount(double parkingAmount) {
        this.parkingAmount = parkingAmount;
    }
    public int getIntTime() {
        return this.intTime;
    }
    public void setIntTime(int intTime) {
        this.intTime = intTime;
    }
    public double getPreTradeBalance() {
        return this.preTradeBalance;
    }
    public void setPreTradeBalance(double preTradeBalance) {
        this.preTradeBalance = preTradeBalance;
    }
    public double getPostTradeBalance() {
        return this.postTradeBalance;
    }
    public void setPostTradeBalance(double postTradeBalance) {
        this.postTradeBalance = postTradeBalance;
    }
    public long getParkingTimeIsValidEnd() {
        return this.parkingTimeIsValidEnd;
    }
    public void setParkingTimeIsValidEnd(long parkingTimeIsValidEnd) {
        this.parkingTimeIsValidEnd = parkingTimeIsValidEnd;
    }

}
