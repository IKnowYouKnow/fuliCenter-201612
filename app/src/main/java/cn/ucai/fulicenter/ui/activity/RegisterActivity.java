package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initTitle();
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

    }
}
