package com.szfp.ss.utils;

import com.szfp.ss.App;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;

/**
 * author：ct on 2017/9/15 16:35
 * email：cnhttt@163.com
 */

public class PrintUtils {
    private static String print_ticket_line="--------------------------------";
    public static void printRechargeRecord(UserInformation uInfo, RechargeRecordBean recordBean) {
        BluetoothService.Begin();
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte) 1);
        BluetoothService.SetLineSpacing((byte)40);
        BluetoothService.SetFontEnlarge((byte) 0x01);
        BluetoothService.BT_Write("CASH TOP-UP");
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte)0);//左对齐
        BluetoothService.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
        BluetoothService.BT_Write("USER NAME:"+uInfo.getFirstName()+uInfo.getLastName()+"\r");
        BluetoothService.BT_Write("RECHARGE AMOUNT:" +recordBean.getRechargeAmount()+"\r");
        BluetoothService.BT_Write("BALANCE: " +DataUtils.getAmountValue(uInfo.getBalance())+"\r");
        BluetoothService.BT_Write("TIME:" + TimeUtils.milliseconds2String(TimeUtils.getCurTimeMills())+"\r");
        BluetoothService.BT_Write("SERVED BY: " +"ADMIN"+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(print_ticket_line+"\r");
    }

    public static void printPurchaseTime(String str) {
        BluetoothService.Begin();
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte) 1);
        BluetoothService.SetLineSpacing((byte)40);
        BluetoothService.SetFontEnlarge((byte) 0x01);
        BluetoothService.BT_Write("Purchase Time");

        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte)0);//左对齐
        BluetoothService.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
        BluetoothService.BT_Write(str+"\r");
        BluetoothService.BT_Write("SERVED BY: " +"ADMIN"+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(print_ticket_line+"\r");
    }

    public static void printEntryVehicle(ParkingRecordReportBean reportBean) {
        BluetoothService.Begin();
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte) 1);
        BluetoothService.SetLineSpacing((byte)40);
        BluetoothService.BT_Write(BluetoothService.DOUBLE_HEIGHT_WIDTH);
        BluetoothService.BT_Write(App.companyName);
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte)0);//左对齐
        BluetoothService.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
        BluetoothService.BT_Write("Serial number:"+ reportBean.getSerialNumber()+"\r");
        BluetoothService.BT_Write("Parking number:" +App.parkingNumber+"\r");
        BluetoothService.BT_Write("Card number:" +reportBean.getCardNumber()+"\r");
        BluetoothService.BT_Write("license Plate Number:"+reportBean.getLicensePlateNumber()+"\r");
        BluetoothService.BT_Write("Name:" + reportBean.getFirstName()+" "+reportBean.getLastName()+"\r");
        BluetoothService.BT_Write("entry time:"+TimeUtils.milliseconds2String(reportBean.getEnterTime())+"\r");

        BluetoothService.SetFontEnlarge((byte) 0x01);
        BluetoothService.BT_Write("advance:"+"0"+"\r" );
        BluetoothService.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
        BluetoothService.BT_Write("Transaction hour:"+TimeUtils.milliseconds2String(reportBean.getCreateTime())+"\r");
        BluetoothService.BT_Write("Operator Number:"+App.operator+"\r");
        BluetoothService.BT_Write("Terminal Number:" +App.terminalNumber+"\r");
        BluetoothService.BT_Write("TEL:"+App.tel+"\r");
        BluetoothService.BT_Write("Address:" +App.address+"\r");
        BluetoothService.BT_Write("Website:"+App.website+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(print_ticket_line+"\r");




    }

    /**
     *
     * @param user
     * @param reportBean
     */
    public static void printExitVehicle(UserInformation user, ParkingRecordReportBean reportBean) {
        BluetoothService.Begin();
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte) 1);
        BluetoothService.SetLineSpacing((byte)40);
        BluetoothService.BT_Write(BluetoothService.DOUBLE_HEIGHT_WIDTH);
        BluetoothService.BT_Write(App.companyName);
        BluetoothService.LF();
        BluetoothService.SetAlignMode((byte)0);//左对齐
        BluetoothService.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
        BluetoothService.BT_Write("Serial number:"+ reportBean.getSerialNumber()+"\r");
        BluetoothService.BT_Write("Parking number:" +reportBean.getParkingNumber()+"\r");
        BluetoothService.BT_Write("Card number:" +reportBean.getCardNumber()+"\r");
        BluetoothService.BT_Write("license Plate Number:"+reportBean.getLicensePlateNumber()+"\r");
        BluetoothService.BT_Write("Name:" + reportBean.getFirstName()+" "+reportBean.getLastName()+"\r");
        BluetoothService.BT_Write("Entry Time:" +TimeUtils.milliseconds2String(reportBean.getEnterTime())+"\r");
        BluetoothService.BT_Write("EXit Time:" +TimeUtils.milliseconds2String(reportBean.getExitTime())+"\r");
        BluetoothService.BT_Write("Stopping Time:"+TimeUtils.formatParkTime(reportBean.getParkingTime())+"\r");
        BluetoothService.BT_Write("Pre - trade balance:"+DataUtils.getAmountValue(reportBean.getPreTradeBalance())+"\r");
        BluetoothService.BT_Write("Post - trade balance:"+DataUtils.getAmountValue(reportBean.getPostTradeBalance())+"\r");
        if (reportBean.getDeductionMethod() ==0){
            BluetoothService.BT_Write("Should pay the amount:0"+"\r");
            BluetoothService.BT_Write("Parking expiry time:"+TimeUtils.milliseconds2String(user.getParkingTimeIsValidEnd())+"\r");
        }else {

            BluetoothService.BT_Write("Should pay the amount:"+DataUtils.getAmountValue(reportBean.getParkingAmount())+"\r");
            BluetoothService.BT_Write("Parking expiry time:"+"0000-00-00 00:00:00"+"\r");

            BluetoothService.BT_Write(BluetoothService.DOUBLE_HEIGHT_WIDTH);
            BluetoothService.BT_Write("Card spending amount:" + DataUtils.getAmountValue(reportBean.getCardAmount())+"\r");
            BluetoothService.BT_Write("Cash spending amount:"+DataUtils.getAmountValue(reportBean.getCashAmount())+"\r");
            BluetoothService.SetFontEnlarge((byte)0x00);//默认宽度、默认高度
        }
        BluetoothService.BT_Write("Transaction hour:"+TimeUtils.milliseconds2String(reportBean.getCreateTime())+"\r");
        BluetoothService.BT_Write("Operator Number:"+App.operator+"\r");
        BluetoothService.BT_Write("Terminal Number:" +App.terminalNumber+"\r");
        BluetoothService.BT_Write("TEL:"+App.tel+"\r");
        BluetoothService.BT_Write("Address:" +App.address+"\r");
        BluetoothService.BT_Write("Website:"+App.website+"\r");
        BluetoothService.BT_Write(" "+"\r");
        BluetoothService.BT_Write(" "+"\r");
    }
}
