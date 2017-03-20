package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    ICategoryModel mModel;
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;


    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupList,
                           ArrayList<ArrayList<CategoryChildBean>> childList) {
        mContext = context;
        mGroupList = groupList;
        mChildList = childList;
        mModel = new CategoryModel();
    }


    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList.get(groupPosition) != null
                && mChildList != null ? mChildList.get(groupPosition).size() : 0;

    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.category_group_item, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.mTvGroupTitle.setText(mGroupList.get(groupPosition).getName());
        ImageLoader.downloadImg(mContext, holder.mIvGroupPic, mGroupList.get(groupPosition).getImageUrl());
        if (isExpanded) {
            holder.mIvExpand.setImageResource(R.mipmap.expand_off);
        } else {
            holder.mIvExpand.setImageResource(R.mipmap.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.category_child_item, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.bind(groupPosition,childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

    class GroupHolder {
        @BindView(R.id.ivGroupPic)
        ImageView mIvGroupPic;
        @BindView(R.id.tvGroupTitle)
        TextView mTvGroupTitle;
        @BindView(R.id.ivExpand)
        ImageView mIvExpand;

        GroupHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ChildHolder {
        @BindView(R.id.ivChildPic)
        ImageView mIvChildPic;
        @BindView(R.id.tvChildTitle)
        TextView mTvChildTitle;
        @BindView(R.id.layoutChild)
        LinearLayout mLayoutChild;

        ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(final int groupPosition, int childPosition) {
           mTvChildTitle.setText(mChildList.get(groupPosition).get(childPosition).getName());
            ImageLoader.downloadImg(mContext, mIvChildPic, mChildList.get(groupPosition)
                    .get(childPosition).getImageUrl());
          final int catId = mChildList.get(groupPosition).get(childPosition).getId();
            mLayoutChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoCategoryChildActivity(mContext, catId,getGroup(groupPosition).getName(),mChildList.get(groupPosition));
                }
            });

        }
    }
}
