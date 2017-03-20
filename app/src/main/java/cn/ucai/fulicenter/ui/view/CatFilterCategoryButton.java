package cn.ucai.fulicenter.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

public class CatFilterCategoryButton extends Button {
    boolean isExpand = false;
    Context mContext;

    PopupWindow mPopupWindow;
    GridView gv;
    CatFilterAdapter mAdapter;
    ArrayList<CategoryChildBean> mChildList = new ArrayList<>();

    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        showArrow();

    }

    private void initPop() {
        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mContext);
            mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            mPopupWindow.setContentView(gv);
        }
        mPopupWindow.showAsDropDown(this);
    }

    private void showArrow() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExpand) {
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

    public void initView(String groupName, ArrayList<CategoryChildBean> list) {
        if (groupName == null || list == null) {
            CommonUtils.showShortToast("小类加载失败！");
            return;
        }
        this.setText(groupName);
        mChildList = list;
        gv = new GridView(mContext);
        mAdapter = new CatFilterAdapter(mContext, mChildList);

        gv.setAdapter(mAdapter);
    }

    class CatFilterAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<CategoryChildBean> mChildBeen;

        public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> childBeen) {
            mContext = context;
            mChildBeen = childBeen;
        }

        @Override
        public int getCount() {
            return mChildBeen != null ? mChildBeen.size() : 0;
        }

        @Override
        public CategoryChildBean getItem(int position) {
            return mChildBeen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CatFilterViewHolder vh = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_filter_category, null);
                vh = new CatFilterViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (CatFilterViewHolder) convertView.getTag();
            }
            L.e("main","vh="+vh);
            vh.bind(position);
            return convertView;
        }

        class CatFilterViewHolder {
            @BindView(R.id.ivCatFilterPic)
            ImageView mIvCatFilterPic;
            @BindView(R.id.tvCategoryName)
            TextView mTvCategoryName;

            CatFilterViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            public void bind(int position) {
                mTvCategoryName.setText(mChildBeen.get(position).getName());
                ImageLoader.downloadImg(mContext, mIvCatFilterPic,
                        mChildBeen.get(position).getImageUrl());
            }
        }
    }
}
