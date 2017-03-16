package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public class BoutiqueChildActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    Unbinder bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boutique_child);
        bind = ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, new NewGoodsFragment())
                .commit();

        String title = getIntent().getStringExtra("title");
        mTvTitle.setText(title);
    }

    public void onBack(View view) {
        MFGT.finish(BoutiqueChildActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
