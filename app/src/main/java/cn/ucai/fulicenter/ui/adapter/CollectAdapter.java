package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class CollectAdapter extends RecyclerView.Adapter {
    ArrayList<CollectBean> mCollectList;
    Context mContext;
    boolean isMore;

    private String textFooter;
    IGoodsModel mModel;


    public void setTextFooter(String textFooter) {
        this.textFooter = textFooter;
        notifyDataSetChanged();
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isMore() {

        return isMore;
    }

    public CollectAdapter(Context mContext, ArrayList<CollectBean> collectList) {
        this.mContext = mContext;
        this.mCollectList = collectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                view = View.inflate(mContext, R.layout.footer_layout, null);
                return new FooterHolder(view);
            case I.TYPE_ITEM:
                view = View.inflate(mContext, R.layout.item_collect, null);
                return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        if (getItemCount() - 1 == position) {
            FooterHolder holder = (FooterHolder) parent;
            holder.mTvFooter.setText(textFooter);
            return;
        }
        ItemHolder holder = (ItemHolder) parent;
        final CollectBean user = mCollectList.get(position);
        holder.bind(position,user);

    }

    @Override
    public int getItemCount() {
        return mCollectList != null ? mCollectList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFooter)
        TextView mTvFooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPic)
        ImageView mTvPic;
        @BindView(R.id.tvNewGoodsName)
        TextView mTvNewGoodsName;
        @BindView(R.id.layout_goods)
        LinearLayout mLayoutGoods;
        @BindView(R.id.btnRemoveCollect)
        ImageView mRemoveCollect;

        ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        public void bind(final int position, final CollectBean user) {
            mTvNewGoodsName.setText(user.getGoodsName());
            ImageLoader.downloadImg(mContext, mTvPic, user.getGoodsThumb());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoGoodsDetailActivity(mContext, user.getGoodsId());
                }
            });
            mRemoveCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mModel = new GoodsModel();
                    String username = FuLiCenterApplication.getUserLogin().getMuserName();
                    mModel.collectAction(mContext, I.ACTION_DELETE_COLLECT, user.getGoodsId(), username,
                            new OnCompleteListener<MessageBean>() {
                                @Override
                                public void onSuccess(MessageBean result) {
                                    if (result != null && result.isSuccess()) {
                                        mCollectList.remove(position);
                                        notifyDataSetChanged();
                                        CommonUtils.showShortToast(result.getMsg());
                                    }
                                }
                                @Override
                                public void onError(String error) {
                                    CommonUtils.showShortToast(R.string.delete_collect_fail);
                                }
                            });
                }
            });
        }
    }
}
