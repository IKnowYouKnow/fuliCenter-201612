package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public  class CategoryChildActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_child);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, new NewGoodsFragment()).commit();
    }
}
