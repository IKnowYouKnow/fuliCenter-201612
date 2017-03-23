package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public interface IGoodsModel {
    void loadData(Context context,int goodsId, OnCompleteListener<GoodsDetailsBean> listener);

    void loadCollectStatus(Context context, int goodsId,
                           String username, OnCompleteListener<MessageBean> listener);
}
