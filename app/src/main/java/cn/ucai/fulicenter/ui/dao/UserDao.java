package cn.ucai.fulicenter.ui.dao;

import android.content.Context;
import android.icu.text.DateFormat;

import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.ui.view.SharedPreferenceUtils;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class UserDao {
    public static final String TB_NAME = "tb_user";
    public static final String USER_COLUMN_USERNAEM = "m_user_username";
    public static final String USER_COLUMN_NICK = "m_user_nick";
    public static final String USER_COLUMN_AVATAR_ID = "m_user_avatar_id";
    public static final String USER_COLUMN_AVATAR_PATH = "m_user_avatar_path";
    public static final String USER_COLUMN_AVATAR_TYPE = "m_user_avatar_type";
    public static final String USER_COLUMN_AVATAR_SUFFIX = "m_user_avatar_suffix";
    public static final String USER_COLUMN_AVATAR_LAST_UPDATETIME = "m_user_avatar_last_update_time";

    private static UserDao instance;

    public static UserDao getInstance(Context context){
        if (instance == null) {
            instance = new UserDao(context);
        }
        return instance;
    }

    public UserDao(Context context){
        DBManager.getInstance().initDB(context);
    }

    public User getUserInfo(String username) {
        if (username == null) {
            return null;
        } else {
            return DBManager.getInstance().getUserInfo(username);
        }
    }

    public boolean setUserInfo(User user) {
        return DBManager.getInstance().saveUserInfo(user);
    }

    public void logout() {
        FuLiCenterApplication.setUserLogin(null);
        SharedPreferenceUtils.getInstance().removeUser();
        DBManager.getInstance().closeDB();
    }

}
