package com.szfp.ss.utils;

import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.greendao.UserInformationDao;
import com.szfp.ss.inter.OnRechargeRecordListener;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;

/**
 * author：ct on 2017/9/12 10:47
 * email：cnhttt@163.com
 */

public class DbHelper {

    /**
     * 插入用户信息
     * @param userInformation
     * @return
     */
    public static boolean insertUserInfo(UserInformation userInformation) {

        try {
            GreenDaoManager.getInstance().getSession().getUserInformationDao().insert(userInformation);
        }catch (Exception e){
            return  false;
        }
        return true;
    }

    /**
     * 修改用户的信息
     * @param userInfo
     * @return
     */
    public static boolean updateUserInfo(UserInformation userInfo){
        try{
             GreenDaoManager.getInstance().getSession().getUserInformationDao().update(userInfo);
        }catch (Exception e){
            return false;
        }
        return true;
    }


    /**
     * 根据ID 卡号查询是否注册过
     * @param cardId
     * @return
     */
    public static UserInformation selectCardIdForUserList(String cardId) {
        UserInformation userInFo = new UserInformation();

        try {
            userInFo = GreenDaoManager.getInstance()
                    .getSession()
                    .getUserInformationDao()
                    .queryBuilder()
                    .where(UserInformationDao.Properties.CardId.eq(cardId))
                    .build().unique();
        }catch (Exception e){
            return null;
        }
        return userInFo;
    }


    public static void insertRechargeRecordBean(UserInformation userInformation, String amount, OnRechargeRecordListener listener) {
        RechargeRecordBean recordBean = new RechargeRecordBean();
        recordBean.setUUID(TimeUtils.getUUID());
        recordBean.setUserId(userInformation.getId());
        recordBean.setFirstName(userInformation.getFirstName());
        recordBean.setLastName(userInformation.getLastName());
        recordBean.setRechargeAmount(DataUtils.getAmountValue(amount));
        recordBean.setTradeType("1");
        recordBean.setCreateTime(TimeUtils.getCurTimeMills());
        recordBean.setCreateDayTime(TimeUtils.getCrateDayTime());

        userInformation.setBalance(userInformation.getBalance()+Double.valueOf(amount));
        try {

            GreenDaoManager.getInstance().getSession().getUserInformationDao().update(userInformation);
            GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao().insert(recordBean);
            listener.success(userInformation,recordBean);

        }catch (Exception e){
            listener.error("Top-up failure");
        }



    }

    public static void insertPurchaseTime(String card,
                                          int num, int totalAmount,
                                          int type, OnRechargeRecordListener listener) {
        UserInformation userInformation= new UserInformation();
        userInformation = selectCardIdForUserList(card);
        RechargeRecordBean recordBean = new RechargeRecordBean();
        try {
            recordBean.setTradeType("2");
            recordBean.setUUID(TimeUtils.getUUID());
            recordBean.setUserId(userInformation.getId());
            recordBean.setFirstName(userInformation.getFirstName());
            recordBean.setLastName(userInformation.getLastName());
            recordBean.setCreateTime(TimeUtils.getCurTimeMills());
            recordBean.setCreateDayTime(TimeUtils.getCrateDayTime());
            //添加充值记录

            double amount =Double.valueOf(totalAmount);

            recordBean.setTwoBuyNum(num);
            recordBean.setTwoBuyType(type);
            switch (type){
                case 0:
                    if (userInformation.getParkingTimeIsValidEnd() ==0||userInformation.getParkingTimeIsValidEnd()<TimeUtils.getCurTimeMills()){
                        userInformation.setParkingTimeIsValidEnd(TimeUtils.getCurTimeMills()+num*60*60*1000L);
                    }else {
                        userInformation.setParkingTimeIsValidEnd(userInformation.getParkingTimeIsValidEnd()+num*60*60*1000L);
                    }
                    recordBean.setTwoBuyName("DAY");
                    break;
                case 1:

                    if (userInformation.getParkingTimeIsValidEnd() ==0||userInformation.getParkingTimeIsValidEnd()< TimeUtils.getCurTimeMills()){
                        userInformation.setParkingTimeIsValidEnd(TimeUtils.getCurTimeMills()+num*86400000L);
                    }else {
                        userInformation.setParkingTimeIsValidEnd(userInformation.getParkingTimeIsValidEnd()+num*86400000L);
                    }
                    recordBean.setTwoBuyName("HOUR");
                    break;
                case 2:

                    if (userInformation.getParkingTimeIsValidEnd() ==0||userInformation.getParkingTimeIsValidEnd()<TimeUtils.getCurTimeMills()){
                        userInformation.setParkingTimeIsValidEnd(TimeUtils.subMonth(TimeUtils.getCurTimeMills(),num));
                    }else {
                        userInformation.setParkingTimeIsValidEnd(TimeUtils.subMonth(userInformation.getParkingTimeIsValidEnd(),num));
                    }
                    recordBean.setTwoBuyName("MONTH");
                    break;
            }
            double balance =  userInformation.getBalance();
            if (balance >=amount){ //余额大于卡内金额直接扣钱
                recordBean.setTwoCardAmount(String.valueOf(amount));
                recordBean.setTwoAmount(amount);
                recordBean.setTwoCashAmount("0");
            }else {//
                double cashAmount = amount - balance;
                recordBean.setTwoCashAmount(String.valueOf(cashAmount));
                recordBean.setTwoCardAmount(String.valueOf(balance));
                userInformation.setBalance(0);
            }


            listener.success(userInformation,recordBean);

        }catch (Exception e){
            listener.error("Top-up failure");
        }
    }

    public static void insertSure(UserInformation uInfo, RechargeRecordBean recordBean) {
        double balance =  uInfo.getBalance();
        double amount =  recordBean.getTwoAmount();
        if (balance >=amount){ //余额大于卡内金额直接扣钱
            uInfo.setBalance(balance-amount);
            recordBean.setTwoCashAmount("0");
        }else {//
            double cashAmount = amount - balance;
            recordBean.setTwoCashAmount(String.valueOf(cashAmount));
            recordBean.setTwoCardAmount(String.valueOf(balance));
            uInfo.setBalance(0);
        }





        GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao().insert(recordBean);
        GreenDaoManager.getInstance().getSession().getUserInformationDao().update(uInfo);
    }
}
