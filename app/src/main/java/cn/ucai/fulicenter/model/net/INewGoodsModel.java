package cn.ucai.fulicenter.model.net;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public interface INewGoodsModel {
    void loadData(Context context,int pageID,OnCompleteListener listener);
}
