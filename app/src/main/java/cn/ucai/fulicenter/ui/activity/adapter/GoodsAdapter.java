package cn.ucai.fulicenter.ui.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<NewGoodsBean> mNewGoodsList;

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> mNewGoodsList) {
        this.mContext = mContext;
        this.mNewGoodsList = mNewGoodsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_layout, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        ViewHolder holder = (ViewHolder) parent;
        NewGoodsBean bean = mNewGoodsList.get(position);
        holder.mtvNewGoodsName.setText(bean.getGoodsName());
        holder.mtvNewGoodsPrice.setText(bean.getPromotePrice());

    }

    @Override
    public int getItemCount() {
        return mNewGoodsList != null ? mNewGoodsList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPic)
        TextView mtvPic;
        @BindView(R.id.tvNewGoodsName)
        TextView mtvNewGoodsName;
        @BindView(R.id.tvNewGoodsPrice)
        TextView mtvNewGoodsPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
