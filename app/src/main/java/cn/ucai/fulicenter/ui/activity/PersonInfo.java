package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.dao.UserDao;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class PersonInfo extends AppCompatActivity {
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivAvatar)
    ImageView mIvAvatar;
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
    @BindView(R.id.tvNick)
    TextView mTvNick;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUserLogin();
        if (user != null) {
            mTvTitle.setText(R.string.user_profile);
            mTvUserName.setText(user.getMuserName());
            mTvNick.setText(user.getMuserNick());
            ImageLoader.downloadImg(this, mIvAvatar, user.getAvatarUrl());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @OnClick(R.id.ivBack)
    public void onBack(View view) {
        MFGT.finish(PersonInfo.this);
    }

    @OnClick(R.id.btnExit)
    public void onClick() {
        UserDao.getInstance(PersonInfo.this).logout();
        MFGT.gotoLoginActivity(PersonInfo.this, I.REQUEST_CODE_LOGIN);
        MFGT.finish(PersonInfo.this);
    }

    @OnClick(R.id.nick)
    public void updateNick() {
        MFGT.gotoUpdateNickActivity(PersonInfo.this);
    }
}
