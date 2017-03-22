package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

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
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.dao.UserDao;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SharedPreferenceUtils;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class UpdateNickActivity extends AppCompatActivity {
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.etNewNick)
    EditText mEtNewNick;
    User user;
    IUserModel mModel;
    String nick;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mModel = new UserModel();
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUserLogin();
        mEtNewNick.setText(user.getMuserNick());
        mEtNewNick.selectAll();
        mTvTitle.setText(R.string.update_user_nick);
        mEtNewNick.setFocusable(true);
        mEtNewNick.setFocusableInTouchMode(true);
        mEtNewNick.requestFocus();

        Timer timer = new Timer();

        timer.schedule(new TimerTask()
                       {
                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager)mEtNewNick.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(mEtNewNick, 0);
                           }
                       },
                998);
    }

    @OnClick({R.id.ivBack, R.id.btnSave})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(UpdateNickActivity.this);
                break;
            case R.id.btnSave:
                if (checkInput()) {
                    showDialog();
                    mModel.updateNick(UpdateNickActivity.this, user.getMuserName(), nick,
                            new OnCompleteListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    Result result = ResultUtils.getResultFromJson(s, User.class);
                                    if (result != null) {
                                        if (result.isRetMsg()) {
                                            User u = (User) result.getRetData();
                                            if (u != null) {
                                                updateSuccess(u);
                                            }

                                        } else {
                                            if (result.getRetCode() == I.MSG_USER_SAME_NICK) {
                                                CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
                                            }
                                            if (result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                                                CommonUtils.showShortToast(R.string.update_fail);
                                            }
                                        }
                                    }
                                    pd.dismiss();
                                }

                                @Override
                                public void onError(String error) {
                                    CommonUtils.showShortToast(R.string.update_fail);
                                    pd.dismiss();
                                }
                            });
                }
                break;
        }
    }

    private void showDialog() {
        pd = new ProgressDialog(UpdateNickActivity.this);
        pd.setMessage("保存中...");
        pd.show();
    }

    private void updateSuccess(final User u) {
        FuLiCenterApplication.setUserLogin(u);
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserDao.getInstance(UpdateNickActivity.this).setUserInfo(u);
            }
        }).start();
        MFGT.finish(UpdateNickActivity.this);
    }

    private boolean checkInput() {
        nick = mEtNewNick.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            mEtNewNick.requestFocus();
            mEtNewNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (nick.equals(user.getMuserNick())) {
            mEtNewNick.requestFocus();
            mEtNewNick.setError(getString(R.string.update_nick_fail_unmodify));
            return false;
        }
        return true;
    }
}
