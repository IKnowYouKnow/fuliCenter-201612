package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class AddressActivity extends AppCompatActivity  {
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
    String URL  = "http://218.244.151.190/demo/charge";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        price = getIntent().getIntExtra(I.ADDRESS_PRICE, 0);
        initView();

        // 设置要使用的支付方式
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb"});
        //提交数据的格式，默认格式为json
        //PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";
        //是否开启日志
        PingppLog.DEBUG = true;
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
        payment();
    }

    private void payment() {
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        // 计算总金额（以分为单位）
        int amount = 0;
        JSONArray billList = new JSONArray();
        amount = 1243;
        // 构建账单json对象
        JSONObject bill = new JSONObject();

        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", amount);
            bill.put("extras", extras);
            Log.d(TAG, "payment: " + bill.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(this, bill.toString(), URL, new PaymentHandler() {
            @Override public void handlePaymentResult(Intent data) {
                if (data != null) {
                    /**
                     * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
                     * error_msg：支付结果信息
                     */
                    int code = data.getExtras().getInt("code");
                    String result = data.getExtras().getString("result");
                    Log.d(TAG, "handlePaymentResult: " + code + ", " + result);
                }
            }
        });
    }



}
