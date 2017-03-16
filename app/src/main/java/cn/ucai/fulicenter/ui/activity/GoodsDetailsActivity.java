package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class GoodsDetailsActivity extends AppCompatActivity {
    int goodsId = 0;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.tvGoodsNameEN)
    TextView mTvGoodsNameEN;
    @BindView(R.id.tvGoodsNameCH)
    TextView mTvGoodsNameCH;
    @BindView(R.id.tvGoodsPrice)
    TextView mTvGoodsPrice;
    IGoodsModel mModel;
    Unbinder bind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details_layout);
        bind = ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            MFGT.finish(GoodsDetailsActivity.this);
        } else {
            mModel = new GoodsModel();
            initData();
        }
    }

    private void initData() {
        mModel.loadData(this, goodsId, new OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                mTvGoodsNameEN.setText(result.getGoodsEnglishName());
                mTvGoodsNameCH.setText(result.getGoodsName());
                mTvGoodsPrice.setText(result.getCurrencyPrice());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick(R.id.back)
    public void onClick() {
        MFGT.finish(GoodsDetailsActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
