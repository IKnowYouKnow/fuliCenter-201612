package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.ui.dao.UserDao;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SharedPreferenceUtils;

public class SplashActivity extends AppCompatActivity {
    int mTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userName = SharedPreferenceUtils.getInstance().getUserName();
                if (userName != null) {
                    User userInfo = UserDao.getInstance(SplashActivity.this).getUserInfo(userName);
                    FuLiCenterApplication.setUserLogin(userInfo);
                    Log.i("main", "user = " + userInfo);
                }
                MFGT.gotoMain(SplashActivity.this);
                MFGT.finish(SplashActivity.this);
            }
        }, mTime);
    }
}
