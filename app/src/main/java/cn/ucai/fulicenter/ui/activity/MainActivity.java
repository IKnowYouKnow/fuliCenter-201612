package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.fragment.CategoryFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.ui.fragment.PersonCenterFragment;
import cn.ucai.fulicenter.ui.view.MFGT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rbNewGoods)
    RadioButton mrbNewGoods;
    @BindView(R.id.rbBoutique)
    RadioButton mrbBoutique;
    @BindView(R.id.rbCategory)
    RadioButton mrbCategory;
    @BindView(R.id.rbCart)
    RadioButton mrbCart;
    @BindView(R.id.rbPersonal)
    RadioButton mrbPersonal;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;
    Unbinder bind;
    RadioButton[] mRadioButtons;
    int index;
    int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, new NewGoodsFragment())
                .commit();
        initRadioButton();
    }

    private void initRadioButton() {
        mRadioButtons = new RadioButton[5];
        mRadioButtons[0] = mrbNewGoods;
        mRadioButtons[1] = mrbBoutique;
        mRadioButtons[2] = mrbCategory;
        mRadioButtons[3] = mrbCart;
        mRadioButtons[4] = mrbPersonal;
    }

    public void onCheckedChange(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                ft.replace(R.id.content_layout, new NewGoodsFragment()).commit();
                break;
            case R.id.rbBoutique:
                index = 1;
                ft.replace(R.id.content_layout, new BoutiqueFragment()).commit();
                break;
            case R.id.rbCategory:
                index = 2;
                ft.replace(R.id.content_layout, new CategoryFragment()).commit();
                break;
            case R.id.rbCart:
                if (FuLiCenterApplication.getUserLogin() == null) {
                    MFGT.gotoLoginActivity(MainActivity.this);
                } else {
                    index = 3;
                }
                break;
            case R.id.rbPersonal:
                if (FuLiCenterApplication.getUserLogin() == null) {
                    MFGT.gotoLoginActivity(MainActivity.this);
                } else {
                    index = 4;
                    ft.replace(R.id.content_layout, new PersonCenterFragment()).commit();
                }
                break;
        }
        currentIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRadioButton();
    }

    private void setRadioButton() {
        for (int i=0;i<mRadioButtons.length;i++) {
            if (i == index) {
                mRadioButtons[i].setChecked(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
