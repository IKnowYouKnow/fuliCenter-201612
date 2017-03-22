package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import cn.ucai.fulicenter.application.I;
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
    Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initRadioButton();
        initFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_layout, fragments[0]).commit();
    }

    private void initFragment() {
        fragments = new Fragment[5];
        fragments[0] = new NewGoodsFragment();
        fragments[1] = new BoutiqueFragment();
        fragments[2] = new CategoryFragment();
        fragments[3] = new NewGoodsFragment();
        fragments[4] = new PersonCenterFragment();
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

        switch (view.getId()) {
            case R.id.rbNewGoods:
                index = 0;
                break;
            case R.id.rbBoutique:
                index = 1;
                break;
            case R.id.rbCategory:
                index = 2;
                break;
            case R.id.rbCart:
                if (FuLiCenterApplication.getUserLogin() == null) {
                    MFGT.gotoLoginActivity(MainActivity.this, I.REQUEST_CODE_LOGIN_FROM_CART);
                } else {
                    index = 3;
                }
                break;
            case R.id.rbPersonal:
                if (FuLiCenterApplication.getUserLogin() == null) {
                    MFGT.gotoLoginActivity(MainActivity.this, I.REQUEST_CODE_LOGIN);
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();
        currentIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (index == 4) {
            if (FuLiCenterApplication.getUserLogin() == null) {
                index = 0;
            }
            setFragment();
        }
        setRadioButton();
    }

    private void setRadioButton() {
        for (int i = 0; i < mRadioButtons.length; i++) {
            if (i == index) {
                mRadioButtons[i].setChecked(true);
            }
        }
    }
    private void setFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (currentIndex != index) {
            ft.hide(fragments[currentIndex]);
            if (!fragments[index].isAdded()) {
                ft.add(R.id.content_layout, fragments[index]);
            }
            ft.show(fragments[index]).commitAllowingStateLoss();
            currentIndex = index;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == I.REQUEST_CODE_LOGIN) {
                index = 4;
            }
            if (requestCode == I.REQUEST_CODE_LOGIN_FROM_CART) {
                index = 3;
            }
            setFragment();
            setRadioButton();
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
