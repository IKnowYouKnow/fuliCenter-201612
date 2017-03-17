package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.NewGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {


    Unbinder mBind;
    INewGoodsModel mModel;
    GridLayoutManager mLayoutManager;
    ArrayList<NewGoodsBean> mGoodsList;
    int mPageId = 1;
    GoodsAdapter mAdapter;
    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvNewGoods)
    RecyclerView mrvNewGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout msrl;

    int catID = 0;
    private int mSortBy;


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
        mModel = new NewGoodsModel();
        initView();
        catID = getActivity().getIntent().getIntExtra(I.NewAndBoutiqueGoods.CAT_ID, 0);
        initData(mPageId, I.ACTION_DOWNLOAD);
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPulUpListener();

    }

    private void setPulUpListener() {
        mrvNewGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (mAdapter.isMore() && lastPosition == mAdapter.getItemCount() - 1 &&
                        RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mPageId++;
                    initData(mPageId, I.ACTION_PULL_UP);
                }

            }
        });
    }

    private void setPullDownListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mGoodsList.clear();
                mPageId = 1;
                initData(mPageId, I.ACTION_PULL_DOWN);
                mtvRefreshHint.setVisibility(View.VISIBLE);
            }
        });
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

    public void initData(int pageId, final int action) {
        mModel.loadData(getContext(),catID, pageId, new OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                mAdapter.setMore(result != null && result.length > 0);
                if (!mAdapter.isMore()) {
                    if (action == I.ACTION_PULL_UP) {
                        mAdapter.setTextFooter("没有更多数据了");
                    }
                    return;
                }
                ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);

                switch (action) {
                    case I.ACTION_DOWNLOAD:
                        mGoodsList.clear();
                        mGoodsList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setTextFooter("加载更多数据。。。");
                        break;
                    case I.ACTION_PULL_DOWN:
                        mGoodsList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                        mtvRefreshHint.setVisibility(View.GONE);
                        msrl.setRefreshing(false);
                        mAdapter.setTextFooter("加载更多数据。。。");
                        break;
                    case I.ACTION_PULL_UP:
                        mGoodsList.addAll(list);
                        mAdapter.notifyDataSetChanged();
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

    public void setSortBy(int sortBy) {
        mAdapter.setSortBy(sortBy);
    }
}
