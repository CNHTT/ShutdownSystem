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
        UserInformation userInFo = null;

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
}
