package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    @BindView(R.id.elv)
    ExpandableListView mElv;
    Unbinder bind;

    ICategoryModel mModel;
    CategoryAdapter mAdapter;

    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;


    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new CategoryModel();
        initView();
        initData();
    }

    private void initView() {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();

        mAdapter = new CategoryAdapter(getActivity(), mGroupList, mChildList);
        mElv.setAdapter(mAdapter);

    }
    private void initData() {
        mModel.loadGroupData(getActivity(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                final ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                mGroupList.addAll(list);
                mAdapter.notifyDataSetChanged();
                initChild(list);
            }
            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
            }
        });





    }

    int i;
    private void initChild(final ArrayList<CategoryGroupBean> list) {
        if (i >= list.size()) {
            return;
        }
        final int parentId = list.get(i++).getId();
        mModel.loadChildData(getActivity(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
                @Override
                public void onSuccess(CategoryChildBean[] result) {
                    ArrayList<CategoryChildBean> bean = ConvertUtils.array2List(result);
                    mChildList.add(bean);
                    mAdapter.notifyDataSetChanged();
                    initChild(list);
                }
                @Override
                public void onError(String error) {
                    CommonUtils.showLongToast(error);
                }
            });
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
