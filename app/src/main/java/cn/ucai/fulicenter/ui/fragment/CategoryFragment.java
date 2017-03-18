package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.layout_tips)
    LinearLayout mLayoutTips;

    View loadView;
    View loadFail;


    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        bind = ButterKnife.bind(this, view);
        loadView = LayoutInflater.from(getContext()).inflate(R.layout.loading, mLayoutTips,false);
        loadFail = LayoutInflater.from(getContext()).inflate(R.layout.load_fail, mLayoutTips, false);
        mLayoutTips.addView(loadView);
        mLayoutTips.addView(loadFail);
        showDialog(true,false);
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
        mElv.setGroupIndicator(null);

    }

    private void initData() {
        mModel.loadGroupData(getActivity(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                final ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                mGroupList.addAll(list);
                mAdapter.notifyDataSetChanged();
                for (int i = 0; i < list.size(); i++) {
                    mChildList.add(new ArrayList<CategoryChildBean>());
                    initChild(list, i);
                }
            }
            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
                loadView.setVisibility(View.GONE);
                loadFail.setVisibility(View.VISIBLE);
                showDialog(false,false);
            }
        });
    }

    private void initChild(final ArrayList<CategoryGroupBean> list, final int index) {
        final int parentId = list.get(index).getId();
        mModel.loadChildData(getActivity(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                if (result != null) {
                    ArrayList<CategoryChildBean> bean = ConvertUtils.array2List(result);
                    mChildList.set(index, bean);
                    loadView.setVisibility(View.GONE);
                    showDialog(false,true);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                CommonUtils.showLongToast(error);
                loadView.setVisibility(View.GONE);
                loadFail.setVisibility(View.VISIBLE);
                showDialog(false,false);
            }
        });
    }
    @OnClick(R.id.layout_tips)
    public void onClick(){
        if (loadFail.getVisibility() == View.VISIBLE) {
            initData();
            showDialog(true,false);
        }
    }
    private void showDialog(boolean dialog, boolean success) {
        loadView.setVisibility(dialog?View.VISIBLE:View.GONE);
        if (dialog) {
            loadFail.setVisibility(View.GONE);
        } else {
            mLayoutTips.setVisibility(success ? View.GONE : View.VISIBLE);
            loadFail.setVisibility(success ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
