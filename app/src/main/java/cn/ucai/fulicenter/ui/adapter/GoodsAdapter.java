package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class GoodsAdapter extends RecyclerView.Adapter {

    Context mContext;
    ArrayList<NewGoodsBean> mNewGoodsList;
    int sortBy = I.SORT_BY_ADDTIME_DESC;

    boolean isMore;

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sortBy();
        notifyDataSetChanged();
    }

    private void sortBy() {
            Collections.sort(mNewGoodsList, new Comparator<NewGoodsBean>() {
                @Override
                public int compare(NewGoodsBean l, NewGoodsBean r) {
                    switch (sortBy) {
                        case I.SORT_BY_ADDTIME_DESC:
                            sortBy = (int) (l.getAddTime() - r.getAddTime());
                            break;
                        case I.SORT_BY_ADDTIME_ASC:
                            sortBy = (int) (r.getAddTime() - l.getAddTime());
                            break;
                        case I.SORT_BY_PRICE_DESC:
                            sortBy = price(l.getCurrencyPrice()) - price(r.getCurrencyPrice());
                            break;
                        case I.SORT_BY_PRICE_ASC:
                            sortBy = price(r.getCurrencyPrice()) - price(l.getCurrencyPrice());
                            break;
                    }
                    return sortBy;
                }
            });
    }

    private int price(String pri) {
        return Integer.parseInt(pri.substring(pri.indexOf("￥")+1));
    }


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

    String textFooter;

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> mNewGoodsList) {
        this.mContext = mContext;
        this.mNewGoodsList = mNewGoodsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case I.TYPE_FOOTER:
                view = View.inflate(mContext, R.layout.footer_layout, null);
                return new FooterHolder(view);
            case I.TYPE_ITEM:
                view = View.inflate(mContext, R.layout.item_layout, null);
                return new ItemHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parent, int position) {
        if (getItemCount() - 1 == position) {
            FooterHolder holder = (FooterHolder) parent;
            holder.tvFooter.setText(textFooter);
            return;
        }
        ItemHolder holder = (ItemHolder) parent;
        final NewGoodsBean bean = mNewGoodsList.get(position);
        holder.mtvNewGoodsName.setText(bean.getGoodsName());
        holder.mtvNewGoodsPrice.setText(bean.getCurrencyPrice());
        ImageLoader.downloadImg(mContext, holder.mtvPic, bean.getGoodsThumb());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoGoodsDetailActivity(mContext, bean.getGoodsId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewGoodsList != null ? mNewGoodsList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvPic)
        ImageView mtvPic;
        @BindView(R.id.tvNewGoodsName)
        TextView mtvNewGoodsName;
        @BindView(R.id.tvNewGoodsPrice)
        TextView mtvNewGoodsPrice;

        ItemHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
