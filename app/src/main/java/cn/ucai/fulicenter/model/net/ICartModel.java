package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public interface ICartModel {
    void loadCart(Context context, String username, OnCompleteListener<CartBean[]> listener);

    void CartAction(Context context, int action,String username, String cartId, String goodsId, int count,
                    OnCompleteListener<MessageBean> listener);
}
