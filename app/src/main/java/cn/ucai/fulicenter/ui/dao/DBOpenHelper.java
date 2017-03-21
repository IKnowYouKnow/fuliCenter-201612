package cn.ucai.fulicenter.ui.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    String CREATE_TABLE_SQL = "create table "+UserDao.TB_NAME+" ("
            +UserDao.USER_COLUMN_USERNAEM +" text primary key,"
            +UserDao.USER_COLUMN_AVATAR_ID +" integer,"
            +UserDao.USER_COLUMN_AVATAR_PATH+" text,"
            +UserDao.USER_COLUMN_AVATAR_SUFFIX+" text,"
            +UserDao.USER_COLUMN_AVATAR_TYPE+" integer,"
            +UserDao.USER_COLUMN_AVATAR_LAST_UPDATETIME+" text" +
            ")";
    DBOpenHelper instance;

    public DBOpenHelper(Context context) {
        super(context, getDatabaseNames(), null, VERSION);
    }

    private static String getDatabaseNames() {
        return "cn.ucai.fulicenter.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized DBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBOpenHelper(context);
        }
        return instance;
    }

    public void closeDB() {
        if (instance != null) {
            SQLiteDatabase database = getWritableDatabase();
            if (database.isOpen()) {
                database.close();
            }
        }
    }

}
