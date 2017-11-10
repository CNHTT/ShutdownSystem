package com.szfp.ss.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.szfp.ss.domain.model.MemberBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MEMBER_BEAN".
*/
public class MemberBeanDao extends AbstractDao<MemberBean, Long> {

    public static final String TABLENAME = "MEMBER_BEAN";

    /**
     * Properties of entity MemberBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Sex = new Property(2, int.class, "sex", false, "SEX");
        public final static Property Number = new Property(3, String.class, "number", false, "NUMBER");
        public final static Property Phone = new Property(4, String.class, "phone", false, "PHONE");
        public final static Property Email = new Property(5, String.class, "email", false, "EMAIL");
        public final static Property ContactAddress = new Property(6, String.class, "contactAddress", false, "CONTACT_ADDRESS");
        public final static Property Status = new Property(7, int.class, "status", false, "STATUS");
        public final static Property Lpm = new Property(8, String.class, "lpm", false, "LPM");
        public final static Property Is_del = new Property(9, int.class, "is_del", false, "IS_DEL");
        public final static Property Balance = new Property(10, double.class, "balance", false, "BALANCE");
        public final static Property CompanyUuid = new Property(11, String.class, "companyUuid", false, "COMPANY_UUID");
        public final static Property Uuid = new Property(12, String.class, "uuid", false, "UUID");
        public final static Property AddManagerUuid = new Property(13, String.class, "addManagerUuid", false, "ADD_MANAGER_UUID");
        public final static Property CreateTime = new Property(14, java.util.Date.class, "createTime", false, "CREATE_TIME");
        public final static Property CardId = new Property(15, String.class, "cardId", false, "CARD_ID");
        public final static Property CacheType = new Property(16, int.class, "cacheType", false, "CACHE_TYPE");
    }


    public MemberBeanDao(DaoConfig config) {
        super(config);
    }
    
    public MemberBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MEMBER_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NAME\" TEXT," + // 1: name
                "\"SEX\" INTEGER NOT NULL ," + // 2: sex
                "\"NUMBER\" TEXT," + // 3: number
                "\"PHONE\" TEXT," + // 4: phone
                "\"EMAIL\" TEXT," + // 5: email
                "\"CONTACT_ADDRESS\" TEXT," + // 6: contactAddress
                "\"STATUS\" INTEGER NOT NULL ," + // 7: status
                "\"LPM\" TEXT," + // 8: lpm
                "\"IS_DEL\" INTEGER NOT NULL ," + // 9: is_del
                "\"BALANCE\" REAL NOT NULL ," + // 10: balance
                "\"COMPANY_UUID\" TEXT," + // 11: companyUuid
                "\"UUID\" TEXT," + // 12: uuid
                "\"ADD_MANAGER_UUID\" TEXT," + // 13: addManagerUuid
                "\"CREATE_TIME\" INTEGER," + // 14: createTime
                "\"CARD_ID\" TEXT," + // 15: cardId
                "\"CACHE_TYPE\" INTEGER NOT NULL );"); // 16: cacheType
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MEMBER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MemberBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getSex());
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(5, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(6, email);
        }
 
        String contactAddress = entity.getContactAddress();
        if (contactAddress != null) {
            stmt.bindString(7, contactAddress);
        }
        stmt.bindLong(8, entity.getStatus());
 
        String lpm = entity.getLpm();
        if (lpm != null) {
            stmt.bindString(9, lpm);
        }
        stmt.bindLong(10, entity.getIs_del());
        stmt.bindDouble(11, entity.getBalance());
 
        String companyUuid = entity.getCompanyUuid();
        if (companyUuid != null) {
            stmt.bindString(12, companyUuid);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(13, uuid);
        }
 
        String addManagerUuid = entity.getAddManagerUuid();
        if (addManagerUuid != null) {
            stmt.bindString(14, addManagerUuid);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(15, createTime.getTime());
        }
 
        String cardId = entity.getCardId();
        if (cardId != null) {
            stmt.bindString(16, cardId);
        }
        stmt.bindLong(17, entity.getCacheType());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MemberBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getSex());
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(5, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(6, email);
        }
 
        String contactAddress = entity.getContactAddress();
        if (contactAddress != null) {
            stmt.bindString(7, contactAddress);
        }
        stmt.bindLong(8, entity.getStatus());
 
        String lpm = entity.getLpm();
        if (lpm != null) {
            stmt.bindString(9, lpm);
        }
        stmt.bindLong(10, entity.getIs_del());
        stmt.bindDouble(11, entity.getBalance());
 
        String companyUuid = entity.getCompanyUuid();
        if (companyUuid != null) {
            stmt.bindString(12, companyUuid);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(13, uuid);
        }
 
        String addManagerUuid = entity.getAddManagerUuid();
        if (addManagerUuid != null) {
            stmt.bindString(14, addManagerUuid);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(15, createTime.getTime());
        }
 
        String cardId = entity.getCardId();
        if (cardId != null) {
            stmt.bindString(16, cardId);
        }
        stmt.bindLong(17, entity.getCacheType());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MemberBean readEntity(Cursor cursor, int offset) {
        MemberBean entity = new MemberBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // sex
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // number
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // phone
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // email
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // contactAddress
            cursor.getInt(offset + 7), // status
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // lpm
            cursor.getInt(offset + 9), // is_del
            cursor.getDouble(offset + 10), // balance
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // companyUuid
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // uuid
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // addManagerUuid
            cursor.isNull(offset + 14) ? null : new java.util.Date(cursor.getLong(offset + 14)), // createTime
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // cardId
            cursor.getInt(offset + 16) // cacheType
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MemberBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSex(cursor.getInt(offset + 2));
        entity.setNumber(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPhone(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEmail(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setContactAddress(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setStatus(cursor.getInt(offset + 7));
        entity.setLpm(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIs_del(cursor.getInt(offset + 9));
        entity.setBalance(cursor.getDouble(offset + 10));
        entity.setCompanyUuid(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setUuid(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setAddManagerUuid(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setCreateTime(cursor.isNull(offset + 14) ? null : new java.util.Date(cursor.getLong(offset + 14)));
        entity.setCardId(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setCacheType(cursor.getInt(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MemberBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MemberBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MemberBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}