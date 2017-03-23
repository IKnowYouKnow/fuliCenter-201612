package cn.ucai.fulicenter.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.ui.dao.UserDao;
import cn.ucai.fulicenter.ui.view.SharedPreferenceUtils;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class FuLiCenterApplication extends Application {
    static FuLiCenterApplication instance;

    public static User getUserLogin() {
        if (userLogin == null) {
            String userName = SharedPreferenceUtils.getInstance().getUserName();
            userLogin = UserDao.getInstance(instance).getUserInfo(userName);
        }
        return userLogin;
    }

    public static void setUserLogin(User userLogin) {
        FuLiCenterApplication.userLogin = userLogin;
    }

    static User userLogin;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    public static Context getInstance() {
        return instance;
    }
}
