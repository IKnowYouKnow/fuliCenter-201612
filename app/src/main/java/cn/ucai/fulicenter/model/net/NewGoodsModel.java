package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class NewGoodsModel implements INewGoodsModel {

    @Override
    public void loadData(Context context,int catId, int pageID, OnCompleteListener listener) {
        String request = I.REQUEST_FIND_NEW_BOUTIQUE_GOODS;
        if (catId > 0) {
            request = I.REQUEST_FIND_GOODS_DETAILS;
        }
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(request)
                .addParam(I.NewAndBoutiqueGoods.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageID))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
}
