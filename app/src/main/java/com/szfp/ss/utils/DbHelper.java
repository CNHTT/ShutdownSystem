package com.szfp.ss.utils;

import android.util.Log;

import com.szfp.ss.R;
import com.szfp.ss.domain.ParkingRecordReportBean;
import com.szfp.ss.domain.RechargeRecordBean;
import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.greendao.ParkingRecordReportBeanDao;
import com.szfp.ss.greendao.RechargeRecordBeanDao;
import com.szfp.ss.greendao.UserInformationDao;
import com.szfp.ss.inter.OnEntryVehicleListener;
import com.szfp.ss.inter.OnExitVehicleListener;
import com.szfp.ss.inter.OnPrintParkListener;
import com.szfp.ss.inter.OnPrintRechargeListener;
import com.szfp.ss.inter.OnRechargeRecordListener;
import com.szfp.utils.BluetoothService;
import com.szfp.utils.DataUtils;
import com.szfp.utils.TimeUtils;
import com.szfp.utils.ToastUtils;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import static com.szfp.ss.App.logger;
import static com.szfp.utils.Utils.getContext;

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
        recordBean.setPrepaidAmount(userInformation.getBalance());
        recordBean.setUUID(TimeUtils.getUUID());
        recordBean.setCardId(userInformation.getCardId());
        recordBean.setSerialNumber(TimeUtils.generateSequenceNo());
        recordBean.setCardNumber(userInformation.getLicensePlateNumber());
        recordBean.setUserId(userInformation.getId());
        recordBean.setFirstName(userInformation.getFirstName());
        recordBean.setLastName(userInformation.getLastName());
        recordBean.setRechargeAmount(DataUtils.getAmountValue(amount));
        recordBean.setTradeType("1");
        recordBean.setCreateTime(TimeUtils.getCurTimeMills());
        recordBean.setCreateDayTime(TimeUtils.getCrateDayTime());


        userInformation.setBalance(userInformation.getBalance()+Double.valueOf(amount));
        recordBean.setAfterAmount(userInformation.getBalance());
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
            recordBean.setPrepaidAmount(userInformation.getBalance());
            recordBean.setSerialNumber(TimeUtils.generateSequenceNo());
            recordBean.setTradeType("2");
            recordBean.setUUID(TimeUtils.getUUID());
            recordBean.setUserId(userInformation.getId());
            recordBean.setCardId(userInformation.getCardId());
            recordBean.setCardNumber(userInformation.getLicensePlateNumber());
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



        recordBean.setAfterAmount(uInfo.getBalance());
        recordBean.setParkingTimeIsValidEnd(uInfo.getParkingTimeIsValidEnd());



        GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao().insert(recordBean);
        GreenDaoManager.getInstance().getSession().getUserInformationDao().update(uInfo);
    }

    public static void insertEntryVehicle(ParkingRecordReportBean reportBean, OnEntryVehicleListener onEntryVehicleListener) {
        try {
            GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao().insert(reportBean);
            onEntryVehicleListener.success(reportBean);
        }catch (Exception e){
            onEntryVehicleListener.error(e.toString());
        }
    }

    public static void getParkingExitList(UserInformation userInformation, OnExitVehicleListener  listener) {
        try {
            Query<ParkingRecordReportBean> query = null;
            ArrayList count = null;
            query = GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao().queryBuilder()
                    .where(ParkingRecordReportBeanDao.Properties.CardId.eq(userInformation.getCardId())
                            ,ParkingRecordReportBeanDao.Properties.Type.eq(0))
                    .orderDesc(ParkingRecordReportBeanDao.Properties.Id).build();
            if (query == null)
            {
                listener.error(getContext().getString(R.string.no_park_data));
            }
            else
            {
                count = (ArrayList) query.list();
                listener.success(count);
            }
        }catch (Exception e){
            listener.error(e.toString());
        }
    }


    public static void updateExitVehicle(ParkingRecordReportBean reportBean, UserInformation userInformation) {
        try {
            GreenDaoManager.getInstance().getSession().getUserInformationDao().update(userInformation);
            GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao().update(reportBean);
            if (BluetoothService.IsNoConnection()) ToastUtils.error(getContext().getString(R.string.not_connected));
            else PrintUtils.printExitVehicle(userInformation,reportBean);
            ToastUtils.success(getContext().getString(R.string.exit_success));
        }catch (Exception e){
            ToastUtils.error(e.toString());
        }
    }

    public static void getRechargeList(int dataPageNum, OnPrintRechargeListener listener) {

        try {
            List<RechargeRecordBean> list = GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao().queryBuilder()
                    .offset(dataPageNum*10).limit(10).orderDesc(RechargeRecordBeanDao.Properties.Id).list();

            listener.success(list);
        }catch (Exception e){
            listener.success(new ArrayList<RechargeRecordBean>());
        }
    }

    public static void getParkRechargeList(int dataNum, OnPrintParkListener listener) {
        try {
            List<ParkingRecordReportBean> list = GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao().queryBuilder()
                    .offset(dataNum*10).limit(10).orderDesc(ParkingRecordReportBeanDao.Properties.Id).list();

            listener.successPark(list);
        }catch (Exception e){
            listener.successPark(new ArrayList<ParkingRecordReportBean>());
        }
    }

    public static void getSearchRechargeList(UserInformation user, long time, int dataNum,OnPrintRechargeListener listener) {
        Query<RechargeRecordBean> query = null;
        try {
            if (!DataUtils.isEmpty(user)&&time!=0){
                query = GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao()
                       .queryBuilder().where(RechargeRecordBeanDao.Properties.CardId.eq(user.getCardId()),
                                RechargeRecordBeanDao.Properties.CreateDayTime.eq(time)).offset(dataNum*10)
                        .orderDesc(RechargeRecordBeanDao.Properties.Id).limit(10).build();
                logger.debug("1");
                ToastUtils.showToast("1");
                listener.success(query.list());

            }else {
                List<RechargeRecordBean> list = GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao().queryBuilder()
                        .offset(dataNum*10).limit(10).orderDesc(RechargeRecordBeanDao.Properties.Id).list();
                logger.debug("2");
                ToastUtils.showToast("2");
                listener.success(list);

            }
            if (!DataUtils.isEmpty(user)){
                query = GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao()
                        .queryBuilder().where(RechargeRecordBeanDao.Properties.CardId.eq(user.getCardId())).offset(dataNum*10).limit(10)
                        .orderDesc(RechargeRecordBeanDao.Properties.Id).build();

                ToastUtils.showToast("3");
                listener.success(query.list());
            }
            if (time!=0){
                query = GreenDaoManager.getInstance().getSession().getRechargeRecordBeanDao()
                        .queryBuilder().where(RechargeRecordBeanDao.Properties.CreateDayTime.eq(time)).offset(dataNum*10).limit(10)
                        .orderDesc(RechargeRecordBeanDao.Properties.Id).build();

                ToastUtils.showToast("4");
                listener.success(query.list());
            }
        }catch (Exception e){
            Log.d("error",e.toString());

            ToastUtils.showToast("5");
            listener.success(new ArrayList<RechargeRecordBean>());
        }
    }

    public static void getSearchParkList(UserInformation user, long time, int dataNum,OnPrintParkListener listener) {
        Query<ParkingRecordReportBean> query = null;
        try {
            if (!DataUtils.isEmpty(user)&&!(time==0)){
                query = GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao()
                        .queryBuilder().where(ParkingRecordReportBeanDao.Properties.CardId.eq(user.getCardId()),
                                ParkingRecordReportBeanDao.Properties.CreateDayTime.eq(time)).offset(dataNum*10)
                        .orderDesc(ParkingRecordReportBeanDao.Properties.Id).limit(10).build();
                listener.successPark(query.list());
            }else {
                List<ParkingRecordReportBean> list = GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao().queryBuilder()
                        .offset(dataNum*10).limit(10).orderDesc(ParkingRecordReportBeanDao.Properties.Id).list();

                listener.successPark(list);
            }
            if (!DataUtils.isEmpty(user)){
                query = GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao()
                        .queryBuilder().where(ParkingRecordReportBeanDao.Properties.CardId.eq(user.getCardId())).offset(dataNum*10).limit(10)
                        .orderDesc(ParkingRecordReportBeanDao.Properties.Id).build();
                listener.successPark(query.list());
            }
            if (time!=0){
                query = GreenDaoManager.getInstance().getSession().getParkingRecordReportBeanDao()
                        .queryBuilder().where(ParkingRecordReportBeanDao.Properties.CreateDayTime.eq(time)).offset(dataNum*10).limit(10)
                        .orderDesc(ParkingRecordReportBeanDao.Properties.Id).build();
                listener.successPark(query.list());
            }
        }catch (Exception e){

            Log.d("error",e.toString());
            logger.debug(e.toString());
            listener.successPark(new ArrayList<ParkingRecordReportBean>());
        }
    }
}
