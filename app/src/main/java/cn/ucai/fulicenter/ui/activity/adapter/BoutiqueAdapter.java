package cn.ucai.fulicenter.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<BoutiqueBean> mBoutiqueBeenList;

    boolean isMore;



    public void setMore(boolean more) {
        isMore = more;
    }

    public boolean isMore() {

        return isMore;
    }


    public BoutiqueAdapter(Context mContext, ArrayList<BoutiqueBean> BoutiqueList) {
        this.mContext = mContext;
        this.mBoutiqueBeenList = BoutiqueList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.boutique_layout, null);
                return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        ItemHolder holder = (ItemHolder) parent;
        BoutiqueBean bean = mBoutiqueBeenList.get(position);
        ImageLoader.downloadImg(mContext, holder.mIvPic, bean.getImageurl());
        holder.mIvName.setText(bean.getName());
        holder.mTvTitle.setText(bean.getTitle());
        holder.mTvDescription.setText(bean.getDescription());

    }

    @Override
    public int getItemCount() {
        return mBoutiqueBeenList != null ? mBoutiqueBeenList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return I.TYPE_ITEM;
    }



    static class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPic)
        ImageView mIvPic;
        @BindView(R.id.tvTitle)
        TextView mTvTitle;
        @BindView(R.id.ivName)
        TextView mIvName;
        @BindView(R.id.tvDescription)
        TextView mTvDescription;

        ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
