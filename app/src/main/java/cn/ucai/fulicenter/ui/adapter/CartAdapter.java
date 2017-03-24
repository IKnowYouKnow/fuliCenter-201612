package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class CartAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<CartBean> mCartList;

    boolean isMore;
    ICartModel mModel;


    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isMore() {

        return isMore;
    }

    public CartAdapter(Context mContext, ArrayList<CartBean> cartList) {
        this.mContext = mContext;
        this.mCartList = cartList;
        mModel = new CartModel();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(View.inflate(mContext, R.layout.item_cart, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        ItemHolder holder = (ItemHolder) parent;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mCartList != null ? mCartList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return I.TYPE_ITEM;
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cb_cart_selected)
        CheckBox mCbCartSelected;
        @BindView(R.id.iv_cart_thumb)
        ImageView mIvCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView mTvCartGoodName;
        @BindView(R.id.tv_cart_count)
        TextView mTvCartCount;
        @BindView(R.id.tv_cart_price)
        TextView mTvCartPrice;
        @BindView(R.id.iv_cart_add)
        TextView addCart;
        int count = 1;

        ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final int position) {
            if (mCartList != null) {
                final CartBean bean = mCartList.get(position);
                count = bean.getCount();
                mTvCartCount.setText("(" + count + ")");
                GoodsDetailsBean goods = bean.getGoods();
                if (goods != null) {
                    mCbCartSelected.setChecked(bean.isChecked());
                    mTvCartGoodName.setText(goods.getGoodsName());
                    mTvCartPrice.setText(goods.getCurrencyPrice());
                    ImageLoader.downloadImg(mContext, mIvCartThumb, goods.getGoodsThumb());
                }
                addCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count++;
                        User user = FuLiCenterApplication.getUserLogin();
                        mModel.CartAction(mContext, I.ACTION_CART_ADD, user.getMuserName(),
                                String.valueOf(bean.getId()), String.valueOf(bean.getGoodsId()), count,
                                new OnCompleteListener<MessageBean>() {
                                    @Override
                                    public void onSuccess(MessageBean result) {

                                    }

                                    @Override
                                    public void onError(String error) {

                                    }
                                });
                    }
                });
            }
        }
    }
}
