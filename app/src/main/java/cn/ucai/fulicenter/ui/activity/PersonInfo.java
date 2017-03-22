package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
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

    OnSetAvatarListener mSetAvatarListener;
    IUserModel mModel;
    String avatarName;

    ProgressDialog pd;

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
            ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user),
                    PersonInfo.this,mIvAvatar);
        }else {
            onBack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
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

    @OnClick({R.id.avatar, R.id.name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avatar:
                mSetAvatarListener = new OnSetAvatarListener(PersonInfo.this,
                        R.id.ivAvatar,getAvatarName(),I.AVATAR_TYPE_USER_PATH);
                break;
            case R.id.name:
                CommonUtils.showShortToast(R.string.username_connot_be_modify);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mSetAvatarListener.setAvatar(requestCode, data, mIvAvatar);
            if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
                File file = mSetAvatarListener.getAvatarFile(PersonInfo.this, avatarName);
                uploadAvatar(file);
            }
        }
    }

    public void showDialog() {
        pd = new ProgressDialog(PersonInfo.this);
        pd.setMessage(getString(R.string.update_user_avatar));
        pd.show();
    }

    private void uploadAvatar(File file) {
        showDialog();
        mModel.uploadAvatar(PersonInfo.this, user.getMuserName(), file, new OnCompleteListener<String>() {
            @Override
            public void onSuccess(String r) {
                Result result = ResultUtils.getResultFromJson(r, User.class);
                if (result != null) {
                    if (result.isRetMsg()) {
                        User u = (User) result.getRetData();
                        if (u != null) {
                            updateSuccess(u);
                        }

                    } else {
                        if (result.getRetCode() == I.MSG_UPLOAD_AVATAR_FAIL) {
                            CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                pd.dismiss();
            }
        });
    }

    private void updateSuccess(final User u) {
        CommonUtils.showShortToast(R.string.update_user_avatar_success);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao.getInstance(PersonInfo.this).setUserInfo(u);
            }
        }).start();
        initData();
        MFGT.finish(PersonInfo.this);
    }

    private String getAvatarName() {
        avatarName = user.getMuserName()+System.currentTimeMillis()+I.AVATAR_SUFFIX_JPG;
        return avatarName;
    }
}
