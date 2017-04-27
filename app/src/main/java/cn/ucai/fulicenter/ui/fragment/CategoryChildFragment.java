package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class CategoryChildFragment extends Fragment {
    ICategoryModel mModel;
    int parentId = 344;
    List<CategoryChildBean> mList;

    RecyclerView mRv;

    CategoryChildAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_child, container, false);
        mRv = (RecyclerView) view.findViewById(R.id.rvCategory);
        GridLayoutManager gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        mRv.setLayoutManager(gm);
        mRv.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new CategoryModel();
        new WorkThread().start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showChildList(ArrayList<CategoryChildBean> list) {
        if (list != null && list.size() > 0) {
            mList = list;
            if (mAdapter == null) {
                mAdapter = new CategoryChildAdapter();
                mRv.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void loadChildList(CategoryGroupBean bean) {
        if (bean != null) {
            parentId = bean.getId();
            new WorkThread().start();
        }
    }

    class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();

            mModel.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
                @Override
                public void onSuccess(CategoryChildBean[] result) {
                    ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                    EventBus.getDefault().post(list);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    class CategoryChildAdapter extends RecyclerView.Adapter<ChildHolder> {

        @Override
        public ChildHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(getContext(), R.layout.adapter_child, null);
            return new ChildHolder(view);
        }

        @Override
        public void onBindViewHolder(ChildHolder holder, int position) {
            holder.tvChild.setText(mList.get(position).getName());
            ImageLoader.downloadImg(getContext(), holder.ivChild, mList.get(position).getImageUrl());
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }
    }

    class ChildHolder extends RecyclerView.ViewHolder {

        ImageView ivChild;
        TextView tvChild;

        public ChildHolder(View itemView) {
            super(itemView);
            ivChild = (ImageView) itemView.findViewById(R.id.iv_child);
            tvChild = (TextView) itemView.findViewById(R.id.tv_child);
        }
    }
}
