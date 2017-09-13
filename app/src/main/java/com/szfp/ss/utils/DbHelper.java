package com.szfp.ss.utils;

import com.szfp.ss.domain.UserInformation;
import com.szfp.ss.greendao.UserInformationDao;

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
}
