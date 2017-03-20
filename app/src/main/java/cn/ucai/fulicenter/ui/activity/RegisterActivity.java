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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.etUserName)
    EditText mEtUserName;
    @BindView(R.id.etNick)
    EditText mEtNick;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.etPassword2)
    EditText mEtPassword2;
    @BindView(R.id.btnRegister)
    Button mBtnRegister;

    String mUserName;
    String mNick;
    String mPassword;

    UserModel mModel;
    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initTitle();
        mModel = new UserModel();
    }

    private void initTitle() {
        mTvTitle.setText("账户注册");
    }

    @OnClick({R.id.ivBack, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                MFGT.finish(RegisterActivity.this);
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {
        if (checkInput()) {
            showDialog();
            mModel.register(RegisterActivity.this, mUserName, mNick,
                    MD5.getMessageDigest(mPassword), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String result) {
                    Result user = ResultUtils.getResultFromJson(result, User.class);
                    Log.i("main", user.getRetCode() + "");
                    if (user.isRetMsg()) {
                        if (user.getRetCode() == I.MSG_REGISTER_SUCCESS) {
                            registerSuccess();
                        }
                    }else {
                        if (user.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                            CommonUtils.showShortToast(getString(R.string.register_fail_exists));
                        } else {
                            CommonUtils.showShortToast(getString(R.string.register_fail));
                        }
                    }
                    mDialog.dismiss();
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(getString(R.string.register_fail));
                    Log.i("main", "Error" + R.string.register_fail);
                    mDialog.dismiss();
                }
            });
        }

    }

    private void registerSuccess() {
        setResult(RESULT_OK,new Intent().putExtra(I.User.USER_NAME,mUserName));
        CommonUtils.showShortToast(R.string.register_success);
        MFGT.finish(RegisterActivity.this);
    }

    private void showDialog() {
        mDialog = new ProgressDialog(RegisterActivity.this);
        mDialog.setMessage(getString(R.string.registering));
        mDialog.show();
    }

    private boolean checkInput() {
        mUserName = mEtUserName.getText().toString().trim();
        mNick = mEtNick.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        String password2 = mEtPassword2.getText().toString().trim();
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
        if (TextUtils.isEmpty(mNick)) {
            mEtNick.requestFocus();
            mEtNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(mPassword)) {
            mEtPassword.requestFocus();
            mEtPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(password2)) {
            mEtPassword2.requestFocus();
            mEtPassword2.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }
        if (!mPassword.equals(password2)) {
            mEtPassword2.requestFocus();
            mEtPassword2.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }
}
