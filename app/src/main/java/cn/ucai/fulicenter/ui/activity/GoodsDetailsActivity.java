package cn.ucai.fulicenter.ui.activity;

import android.net.rtp.RtpStream;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.ui.util.AntiShake;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

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
    @BindView(R.id.salv)
    SlideAutoLoopView mSalv;
    @BindView(R.id.indicator)
    FlowIndicator mIndicator;
    @BindView(R.id.wvDetails)
    WebView mWvDetails;
    @BindView(R.id.ivCollect)
    ImageView mIvCollect;
    boolean isCollect = false;
    User user;
    AntiShake util = new AntiShake();
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
                if (result != null) {
                    showDetails(result);
                    loadCollectStatus();
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });
    }

    private void loadCollectStatus() {
        user = FuLiCenterApplication.getUserLogin();
        if (user != null) {
            setCollectAction(I.ACTION_IS_COLLECT, user);

        }
    }

    private void setCollectAction(final int action, User user) {
        mModel.collectAction(GoodsDetailsActivity.this, action, goodsId, user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        if (msg != null && msg.isSuccess()) {
                            isCollect = action==I.ACTION_DELETE_COLLECT?false:true;
                            if (action != I.ACTION_IS_COLLECT) {
                                CommonUtils.showShortToast(msg.getMsg());
                            }
                        } else {
                            isCollect = action==I.ACTION_IS_COLLECT?false:isCollect;
                        }
                        setCollectStatus();
                    }

                    @Override
                    public void onError(String error) {
                        isCollect = action==I.ACTION_DELETE_COLLECT?true:false;
                        setCollectStatus();
                    }
                });
    }

    private void setCollectStatus() {
        mIvCollect.setImageResource(isCollect ? R.mipmap.bg_collect_out : R.mipmap.bg_collect_in);
    }

    private void showDetails(GoodsDetailsBean bean) {
        mTvGoodsNameEN.setText(bean.getGoodsEnglishName());
        mTvGoodsNameCH.setText(bean.getGoodsName());
        mTvGoodsPrice.setText(bean.getCurrencyPrice());
        mSalv.startPlayLoop(mIndicator, getAblumUrl(bean), getAblumCount(bean));
        mWvDetails.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private int getAblumCount(GoodsDetailsBean bean) {
        return bean.getProperties().length;
    }

    private String[] getAblumUrl(GoodsDetailsBean bean) {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            AlbumsBean[] albums = bean.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[0].getImgUrl();
                }
                return urls;
            }
        }

        return null;
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

    @OnClick(R.id.ivCollect)
    public void onCollect() {
        if (util.check())
            return;
        if (isCollect) {
            setCollectAction(I.ACTION_DELETE_COLLECT, user);
        } else {
            setCollectAction(I.ACTION_ADD_COLLECT,user);
        }
    }
}
