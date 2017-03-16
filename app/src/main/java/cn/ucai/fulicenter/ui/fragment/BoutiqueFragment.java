package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.BoutiqueModel;
import cn.ucai.fulicenter.model.net.IBoutiqueModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.ui.adapter.BoutiqueAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {


    Unbinder mBind;
    IBoutiqueModel mModel;
    LinearLayoutManager mLayoutManager;
    ArrayList<BoutiqueBean> mBoutiqueList;
    BoutiqueAdapter mAdapter;
    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvNewGoods)
    RecyclerView mrvNewGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout msrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = new BoutiqueModel();
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
    }

    private void setListener() {
        setPullDownListener();
    }


    private void setPullDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBoutiqueList.clear();
                initData(I.ACTION_PULL_DOWN);
                mtvRefreshHint.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mrvNewGoods.setLayoutManager(mLayoutManager);
        mrvNewGoods.setHasFixedSize(true);

        mBoutiqueList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(getContext(), mBoutiqueList);
        mrvNewGoods.setAdapter(mAdapter);


    }

    private void initData(final int action) {
        mModel.loadData(getContext(), new OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);

                switch (action) {
                    case I.ACTION_DOWNLOAD:
                        mBoutiqueList.clear();
                        mBoutiqueList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case I.ACTION_PULL_DOWN:
                        mBoutiqueList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mtvRefreshHint.setVisibility(View.GONE);
                        msrl.setRefreshing(false);
                        break;
                }
            }

            @Override
            public void onError(String error) {
                mAdapter.notifyDataSetChanged();
                mtvRefreshHint.setVisibility(View.GONE);
                Toast.makeText(getContext(), "出问题啦", Toast.LENGTH_SHORT).show();
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
