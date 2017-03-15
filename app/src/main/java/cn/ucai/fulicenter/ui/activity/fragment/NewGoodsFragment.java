package cn.ucai.fulicenter.ui.activity.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.NewGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.activity.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.activity.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {


    @BindView(R.id.rvNewGoods)
    RecyclerView mrvNewGoods;
    Unbinder mBind;
    INewGoodsModel mModel;
    GridLayoutManager mLayoutManager;
    ArrayList<NewGoodsBean> mGoodsList;
    int mPageId = 1;
    GoodsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        mBind = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new NewGoodsModel();
        initView();
        initData();
    }

    private void initView() {
        mLayoutManager = new GridLayoutManager(getContext(), I.COLUM_NUM);
        mrvNewGoods.setLayoutManager(mLayoutManager);
        mrvNewGoods.setHasFixedSize(true);

        mGoodsList = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(), mGoodsList);
        mrvNewGoods.setAdapter(mAdapter);
        mrvNewGoods.addItemDecoration(new SpaceItemDecoration(20));

    }

    private void initData() {
        mModel.loadData(getContext(), mPageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    mGoodsList.clear();
                    mGoodsList.addAll(list);
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }
}
