package com.szfp.ss.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.szfp.ss.domain.UserInformation;

import com.szfp.ss.greendao.UserInformationDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userInformationDaoConfig;

    private final UserInformationDao userInformationDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userInformationDaoConfig = daoConfigMap.get(UserInformationDao.class).clone();
        userInformationDaoConfig.initIdentityScope(type);

        userInformationDao = new UserInformationDao(userInformationDaoConfig, this);

        registerDao(UserInformation.class, userInformationDao);
    }
    
    public void clear() {
        userInformationDaoConfig.clearIdentityScope();
    }

    public UserInformationDao getUserInformationDao() {
        return userInformationDao;
    }

}
