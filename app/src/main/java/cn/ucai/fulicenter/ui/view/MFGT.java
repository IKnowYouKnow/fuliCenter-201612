package cn.ucai.fulicenter.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.ui.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.ui.activity.CategoryChildActivity;
import cn.ucai.fulicenter.ui.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.ui.activity.MainActivity;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class MFGT {
    public static void startActivity(Activity activity,Class cla) {
        activity.startActivity(new Intent(activity,cla));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }

    public static void gotoMain(Activity activity) {
        startActivity(activity, MainActivity.class);
    }

    public static void gotoBoutiqueChild(Context activity, BoutiqueBean bean) {
        startActivity((Activity)activity,new Intent(activity,BoutiqueChildActivity.class)
        .putExtra(I.NewAndBoutiqueGoods.CAT_ID,bean.getId())
        .putExtra(I.Boutique.TITLE,bean.getTitle()));
    }

    public static void gotoGoodsDetailActivity(Context activity, NewGoodsBean bean) {
        startActivity((Activity) activity, new Intent(activity, GoodsDetailsActivity.class)
                .putExtra(I.Goods.KEY_GOODS_ID, bean.getGoodsId()));
    }

    public static void gotoCategoryChildActivity(Context context, int catId) {
        startActivity((Activity)context,new Intent(context,CategoryChildActivity.class)
                .putExtra(I.NewAndBoutiqueGoods.CAT_ID,catId));
    }
}