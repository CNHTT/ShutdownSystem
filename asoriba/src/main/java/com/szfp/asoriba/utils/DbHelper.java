package com.szfp.asoriba.utils;

import com.szfp.asoriba.bean.FingerprintBean;
import com.szfp.asoriba.bean.MemberBean;
import com.szfp.asoriba.greendao.MemberBeanDao;
import com.szfp.asoriba.inter.OnSuccessCallBack;

import java.util.List;

import static com.szfp.asoriba.App.logger;

/**
 * author：ct on 2017/10/12 17:35
 * email：cnhttt@163.com
 */

public class DbHelper {
    public static long getCount() {
        long  num = GreenDaoManager.getInstance().getSession().getMemberBeanDao().queryBuilder().count();
        return num+1;
    }

    public static void saveInfo(MemberBean memberBean, FingerprintBean fingerprintBean, OnSuccessCallBack onSuccessCallBack) {
        try {

            GreenDaoManager.getInstance().getSession().getMemberBeanDao().insert(memberBean);
            GreenDaoManager.getInstance().getSession().getFingerprintBeanDao().insert(fingerprintBean);
            onSuccessCallBack.success();


        }catch (Exception e){
            logger.debug(e.toString());
            onSuccessCallBack.error();
        }
    }

    public static List<MemberBean> getList() {

        try {
            return  GreenDaoManager.getInstance().getSession().getMemberBeanDao().loadAll();

        }catch (Exception e){
            return null;
        }
    }

    public static MemberBean getMember(String string) {
        try {
            return  GreenDaoManager.getInstance().getSession().getMemberBeanDao().queryBuilder()
                    .where(MemberBeanDao.Properties.UHF_ID.eq(string)).build().unique();

        }catch (Exception e){
            return null;
        }
    } public static MemberBean getMemberUUID(String string) {
        try {
            return  GreenDaoManager.getInstance().getSession().getMemberBeanDao().queryBuilder()
                    .where(MemberBeanDao.Properties.UUID.eq(string)).build().unique();

        }catch (Exception e){
            return null;
        }
    }

    public static void upMember(MemberBean memberBean) {
        try {
              GreenDaoManager.getInstance().getSession().getMemberBeanDao().update(memberBean);

        }catch (Exception e){
        }
    }

    public static MemberBean getMemberEpc(String data) {
        try {
            return  GreenDaoManager.getInstance().getSession().getMemberBeanDao().queryBuilder()
                    .where(MemberBeanDao.Properties.UUID.eq(data)).build().unique();

        }catch (Exception e){
            return null;
        }
    }

    public static MemberBean selectCardIdForUserList(String cardId) {
        MemberBean userInFo = new MemberBean();

        try {
            userInFo = GreenDaoManager.getInstance()
                    .getSession()
                    .getMemberBeanDao()
                    .queryBuilder()
                    .where(MemberBeanDao.Properties.UHF_ID.eq(cardId))
                    .build().unique();
        }catch (Exception e){
            return null;
        }
        return userInFo;
    }
}
