package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.IBoutiqueModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.activity.LoginActivity;
import cn.ucai.fulicenter.ui.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {


    @BindView(R.id.tv_cart_sum_price)
    TextView mTvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView mTvCartSavePrice;
    @BindView(R.id.tv_cart_buy)
    TextView mTvCartBuy;
    @BindView(R.id.tvHint)
    TextView mTvHint;
    @BindView(R.id.tv_nothing)
    TextView mTvNothing;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    ICartModel mModel;
    LinearLayoutManager mLayoutManager;
    ArrayList<CartBean> mCartList;
    CartAdapter mAdapter;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRv.setLayoutManager(mLayoutManager);
        mRv.setHasFixedSize(true);

        mCartList = new ArrayList<>();
        mAdapter = new CartAdapter(getContext(), mCartList);
        mRv.setAdapter(mAdapter);

    }

    private void setListener() {
        setPullDownListener();
        mAdapter.setListener(addCart);
        mAdapter.setCbListener(cbListener);

    }

    CheckBox.OnCheckedChangeListener cbListener = new CheckBox.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();
            mCartList.get(position).setChecked(isChecked);
            setPrice();
        }
    };
    private static final String TAG = "CartFragment";

    private void setPrice() {
        int sumPrice = 0;
        int rankPrice = 0;
        Log.i(TAG, "mCartList= " + mCartList);
        for (CartBean bean : mCartList) {
            if (bean.isChecked()) {
                GoodsDetailsBean goods = bean.getGoods();
                if (goods != null) {
                    sumPrice += getPrice(goods.getCurrencyPrice()) * bean.getCount();
                    rankPrice += getPrice(goods.getRankPrice()) * bean.getCount();
                }
            }
        }
        mTvCartSumPrice.setText("合计：¥" + sumPrice);
        mTvCartSavePrice.setText("节省：¥" + rankPrice);
    }

    View.OnClickListener addCart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            int count = 0;
            Log.i(TAG,"addCart"+v.getTag(R.id.action_add_cart));
            Log.i(TAG,"delCart"+v.getTag(R.id.action_del_cart));
            if (v.getTag(R.id.action_add_cart) != null) {
                    count = (int) v.getTag(R.id.action_add_cart);
            }else if (v.getTag(R.id.action_del_cart) != null) {
                count = (int) v.getTag(R.id.action_del_cart);
            }
            updateCart(position, count);
        }
    };

    private void updateCart(final int position, final int count) {
        final CartBean bean = mCartList.get(position);
        int action = bean.getCount() <=0 ? I.ACTION_CART_DEL : I.ACTION_CART_UPDATA;
        mModel.CartAction(getContext(), action, FuLiCenterApplication.getUserLogin().getMuserName(),
                String.valueOf(bean.getId()), String.valueOf(bean.getGoodsId()), bean.getCount() + count,
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            updateCartList(position, count);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });

    }

    private void updateCartList(int position, int count) {
        if (mCartList.get(position).getCount() + count == 0) {
            mCartList.remove(position);
        } else {
            mCartList.get(position).setCount(mCartList.get(position).getCount() + count);
        }
        setPrice();
        mAdapter.notifyDataSetChanged();
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mTvHint.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData() {
        mModel = new CartModel();
        user = FuLiCenterApplication.getUserLogin();
        if (user != null) {
            loadCart();
        }
    }

    private void loadCart() {
        mModel.loadCart(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                if (result != null) {
                    mCartList.clear();
                    if (result.length > 0) {
                        mTvNothing.setVisibility(View.GONE);
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        for (CartBean been:list) {
                            if (been.getCount() > 0 )
                                mCartList.add(been);
                        }
//                        mCartList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                mTvHint.setVisibility(View.GONE);
                mSrl.setRefreshing(false);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private int getPrice(String p) {
        String str = p.substring(p.lastIndexOf("￥") + 1);
        return Integer.valueOf(str);
    }
}
