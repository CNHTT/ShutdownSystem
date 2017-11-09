package com.szfp.asoriba.utils;

import com.szfp.asoriba.greendao.DaoMaster;
import com.szfp.asoriba.greendao.DaoSession;

import static com.szfp.utils.Utils.getContext;

/**
 * author：ct on 2017/10/12 17:32
 * email：cnhttt@163.com
 */

public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private  static volatile GreenDaoManager mInstance=null;
    private GreenDaoManager(){
        if (mInstance == null){
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), "asoriba");
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }


    public static  GreenDaoManager getInstance(){
        if (mInstance ==null)
        {
            synchronized (GreenDaoManager.class){
                if (mInstance==null)
                    mInstance = new GreenDaoManager();
            }
        }
        return  mInstance;
    }
    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
