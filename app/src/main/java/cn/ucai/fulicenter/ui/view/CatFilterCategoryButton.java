package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class CatFilterCategoryButton extends Button {
    boolean isExpand = false;
    Context mContext;

    PopupWindow mPopupWindow;
    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        showArrow();

    }

    private void initPop() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        TextView tv = new TextView(mContext);
        tv.setText("CatFilterCategoryButton");
        mPopupWindow.setContentView(tv);
        mPopupWindow.showAsDropDown(this);
    }

    private void showArrow() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand) {
                    initPop();
                } else {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                Drawable end = getResources().getDrawable(isExpand ?
                        R.drawable.arrow2_down : R.drawable.arrow2_up);
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                isExpand = !isExpand;
            }
        });
    }
}
