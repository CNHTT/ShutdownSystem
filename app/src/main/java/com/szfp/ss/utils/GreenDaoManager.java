package com.szfp.ss.utils;

import com.szfp.ss.domain.KEY;
import com.szfp.ss.greendao.DaoMaster;
import com.szfp.ss.greendao.DaoSession;

import static com.szfp.utils.Utils.getContext;

/**
 * author：ct on 2017/9/12 10:27
 * email：cnhttt@163.com
 */

public class GreenDaoManager {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static volatile  GreenDaoManager mInstance= null;

    private GreenDaoManager(){
        if (mInstance == null){
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), KEY.DB_NAME);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }


    public static GreenDaoManager getInstance(){
        if (mInstance == null){
            synchronized (GreenDaoManager.class){
                if (mInstance == null)
                    mInstance = new GreenDaoManager();
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }


    public DaoSession getSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

}

