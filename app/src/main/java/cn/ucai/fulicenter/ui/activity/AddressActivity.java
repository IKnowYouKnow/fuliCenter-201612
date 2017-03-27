package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class AddressActivity extends AppCompatActivity {
    private static final String TAG = "AddressActivity";
    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.etTelNum)
    EditText mEtTelNum;
    @BindView(R.id.etCity)
    Spinner mEtCity;
    @BindView(R.id.etStreet)
    EditText mEtStreet;
    @BindView(R.id.price)
    TextView mPrice;
    int price = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        price = getIntent().getIntExtra(I.ADDRESS_PRICE, 0);
        initView();
    }

    private void initView() {
        mTvTitle.setText("填写收货人地址");
        mPrice.setText("需支付："+price+"元");
    }

    @OnClick(R.id.ivBack)
    public void backArea() {
        MFGT.finish(AddressActivity.this);
    }

    @OnClick(R.id.tvBuy)
    public void toBuy() {
       commitOrder();
    }

    private void commitOrder() {
        String username = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mEtName.requestFocus();
            mEtName.setError("收货人姓名不能为空");
            return;
        }
        String telPhone = mEtTelNum.getText().toString().trim();
        if (TextUtils.isEmpty(telPhone)) {
            mEtTelNum.requestFocus();
            mEtTelNum.setError("手机号码不能为空");
            return;
        }
        if (!telPhone.matches("[\\d]{11}")) {
            mEtTelNum.requestFocus();
            mEtTelNum.setError("手机号码格式不对");
            return;
        }
        String street = mEtStreet.getText().toString().trim();
        if (TextUtils.isEmpty(street)) {
            mEtStreet.requestFocus();
            mEtStreet.setError("街道不能为空");
            return;
        }
        String city = mEtCity.getSelectedItem().toString();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "所在地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
