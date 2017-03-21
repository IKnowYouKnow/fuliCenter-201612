package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.dao.DBManager;
import cn.ucai.fulicenter.ui.dao.UserDao;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SharedPreferenceUtils;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;

    String mUserName,mPassword;
    IUserModel mModel;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mModel = new UserModel();
    }

    @OnClick({R.id.ivBack, R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(LoginActivity.this);
                break;
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnRegister:
                MFGT.gotoRegisterActivity(LoginActivity.this);
                break;
        }
    }

    private void login() {
        if (checkInput()) {
            showDialog();
            mModel.login(this, mUserName, MD5.getMessageDigest(mPassword), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null ) {
                        if (result.isRetMsg()) {
                            User user = (User) result.getRetData();
                            if (user != null) {
                                loginSuccess(user);
                            }
                        } else {
                            if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                                CommonUtils.showShortToast(R.string.login_fail_unknow_user);
                            }
                            if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                                CommonUtils.showShortToast(R.string.login_fail_error_password);
                            }
                        }
                    }
                    pd.dismiss();
                }
                @Override
                public void onError(String error) {
                    pd.dismiss();
                    CommonUtils.showShortToast(R.string.login_fail);
                }
            });
        }
    }

    private void showDialog() {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage(getString(R.string.logining));
        pd.show();
    }

    private void loginSuccess(final User user) {
        FuLiCenterApplication.setUserLogin(user);
        SharedPreferenceUtils.getInstance().setUserName(user.getMuserName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(LoginActivity.this).setUserInfo(user);
                Log.i("main","loginSuccess , b = "+ b);

            }
        }).start();
        UserDao.getInstance(LoginActivity.this).setUserInfo(user);
        MFGT.finish(LoginActivity.this);
    }

    private boolean checkInput() {
        mUserName = mEtUserName.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName)) {
            mEtUserName.requestFocus();
            mEtUserName.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if (!mUserName.matches("[a-zA-Z]\\w{5,15}")) {
            mEtUserName.requestFocus();
            mEtUserName.setError(getString(R.string.illegal_user_name));
            return false;
        }
        if (TextUtils.isEmpty(mPassword)) {
            mEtPassword.requestFocus();
            mEtPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER) {
            String name = data.getStringExtra(I.User.USER_NAME);
            mEtUserName.setText(name);
        }
    }
}
