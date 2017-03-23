package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.INewGoodsModel;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.ui.adapter.CollectAdapter;
import cn.ucai.fulicenter.ui.adapter.GoodsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class CollectActivity extends AppCompatActivity {
    Unbinder mBind;
    IUserModel mModel;
    GridLayoutManager mLayoutManager;
    ArrayList<CollectBean> mGoodsList;
    int mPageId = 1;
    CollectAdapter mAdapter;
    @BindView(R.id.tvRefreshHint)
    TextView mtvRefreshHint;
    @BindView(R.id.rvNewGoods)
    RecyclerView mrvNewGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout msrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_goods);
        mBind= ButterKnife.bind(this);
        mModel = new UserModel();
        initView();
        initData(mPageId,I.ACTION_DOWNLOAD);
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(mPageId,I.ACTION_DOWNLOAD);
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
        mLayoutManager = new GridLayoutManager(CollectActivity.this, I.COLUM_NUM);
        mrvNewGoods.setLayoutManager(mLayoutManager);
        mrvNewGoods.setHasFixedSize(true);

        mGoodsList = new ArrayList<>();
        mAdapter = new CollectAdapter(CollectActivity.this, mGoodsList);
        mrvNewGoods.setAdapter(mAdapter);
        mrvNewGoods.addItemDecoration(new SpaceItemDecoration(20));
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position) {
                int viewType = mAdapter.getItemViewType(position);
                if (viewType == I.TYPE_FOOTER) {
                    return 2;
                }
                return 1;
            }
        });
    }

    private void initData(int pageId,final int action) {
        User user = FuLiCenterApplication.getUserLogin();
        if (user != null) {
            mModel.loadCollects(CollectActivity.this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT,
                    new OnCompleteListener<CollectBean[]>() {
                        @Override
                        public void onSuccess(CollectBean[] result) {

                            mAdapter.setMore(result != null && result.length > 0);
                            if (!mAdapter.isMore()) {
                                if (action == I.ACTION_PULL_UP) {
                                    mAdapter.setTextFooter("没有更多数据了");
                                }
                                return;
                            }
                            ArrayList<CollectBean> list = ConvertUtils.array2List(result);

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
                            Toast.makeText(CollectActivity.this, "出问题啦", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }
}
