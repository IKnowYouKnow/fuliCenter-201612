package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.NewGoodsBean;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public interface INewGoodsModel {
    void loadData(Context context,int pageID,OnCompleteListener<NewGoodsBean[]> listener);
}
