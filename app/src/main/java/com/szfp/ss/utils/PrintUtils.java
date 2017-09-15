package com.szfp.ss.utils;

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
}
