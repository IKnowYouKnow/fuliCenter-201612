package cn.ucai.fulicenter.ui.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.fulicenter.model.bean.User;


/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class DBManager {
    static DBManager instance = new DBManager();
    DBOpenHelper mHelper;

    public static DBManager getInstance() {
        return instance;
    }

    public synchronized void initDB(Context context) {
        mHelper = new DBOpenHelper(context);
    }

    public boolean saveUserInfo(User user) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(UserDao.USER_COLUMN_USERNAEM,user.getMuserName());
            values.put(UserDao.USER_COLUMN_NICK,user.getMuserNick());
            values.put(UserDao.USER_COLUMN_AVATAR_ID,user.getMavatarId());
            values.put(UserDao.USER_COLUMN_AVATAR_PATH,user.getMavatarPath());
            values.put(UserDao.USER_COLUMN_AVATAR_TYPE,user.getMavatarType());
            values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,user.getMavatarSuffix());
            values.put(UserDao.USER_COLUMN_AVATAR_LAST_UPDATETIME,user.getMavatarLastUpdateTime());

            return db.replace(UserDao.TB_NAME, null, values)!=-1;
        }
        return false;
    }

    public User getUserInfo(String username) {
        String sql = "select * from " + UserDao.TB_NAME + " where "
                + UserDao.USER_COLUMN_USERNAEM + "='" + username + "'";
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            User user = new User();
            user.setMuserName(username);
            user.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LAST_UPDATETIME)));
            return user;
        }
        return null;
    }
    public void closeDB() {
        mHelper.closeDB();
    }
}
