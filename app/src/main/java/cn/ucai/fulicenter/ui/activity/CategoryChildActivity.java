package cn.ucai.fulicenter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.CatFilterCategoryButton;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class CategoryChildActivity extends AppCompatActivity {

    @BindView(R.id.tvPrice)
    TextView mTvPrice;
    @BindView(R.id.tvTime)
    TextView mTvTime;
    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.btnFilter)
    CatFilterCategoryButton mBtnFilter;

    NewGoodsFragment mFragment;
    boolean price;
    boolean time;
    int sort;

    String groupName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_child);
        ButterKnife.bind(this);
        mFragment = new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, mFragment).commit();
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        initView();
    }

    private void initView() {
        mBtnFilter.setText(groupName);
    }

    Drawable end;

    @OnClick({R.id.tvPrice, R.id.tvTime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvPrice:
                sort = price ? I.SORT_BY_PRICE_DESC : I.SORT_BY_PRICE_ASC;
                end = getResources().getDrawable(price ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                mTvPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                price = !price;
                break;
            case R.id.tvTime:
                sort = time ? I.SORT_BY_ADDTIME_DESC : I.SORT_BY_ADDTIME_ASC;
                end = getResources().getDrawable(time ? R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                mTvTime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                time = !time;
                break;
        }
        mFragment.setSortBy(sort);
    }

    @OnClick(R.id.ivBack)
    public void onClick() {
        MFGT.finish(CategoryChildActivity.this);
    }
}
