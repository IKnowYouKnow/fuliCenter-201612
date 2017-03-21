package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.content.SharedPreferences;

import cn.ucai.fulicenter.application.FuLiCenterApplication;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class SharedPreferenceUtils {
    static SharedPreferenceUtils instance;
    private static final String SHARE_PREFERENCE_NAME = "cn.ucai.fulicenter_save_userinfo";
    private static final String SAVE_USERINFO_USERNAME = "m_user_username";
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPreferenceUtils() {
        mSharedPreferences = FuLiCenterApplication.getInstance().getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static SharedPreferenceUtils getInstance() {
        if (instance == null) {
            instance = new SharedPreferenceUtils();
        }
        return instance;
    }

    // 保存用户名
    public void setUserName(String name) {
        editor.putString(SAVE_USERINFO_USERNAME, name).commit();
    }

    //获取用户名
    public String getUserName() {
       return mSharedPreferences.getString(SAVE_USERINFO_USERNAME, null);
    }
    // 清除用户信息
    public void removeUser() {
        editor.remove(SAVE_USERINFO_USERNAME).commit();
    }

}
