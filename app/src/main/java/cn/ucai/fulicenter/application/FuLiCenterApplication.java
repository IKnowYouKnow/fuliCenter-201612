package cn.ucai.fulicenter.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.model.bean.User;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class FuLiCenterApplication extends Application {
    static FuLiCenterApplication instance;

    public static User getUserLogin() {
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
