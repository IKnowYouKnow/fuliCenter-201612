package cn.ucai.fulicenter.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonCenterFragment extends Fragment {


    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    User user;
    @BindView(R.id.tv_collect_count)
    TextView mTvCollectCount;
    IUserModel mModel;

    public PersonCenterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_center, container, false);
        ButterKnife.bind(this, view);
        mModel = new UserModel();
        return view;
    }

    private void showUserInfo() {
        mTvUserName.setText(user.getMuserNick());
        ImageLoader.downloadImg(getActivity(), mIvUserAvatar, user.getAvatarUrl());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUserLogin();
        if (user != null) {
            showUserInfo();
            loadCollectCount();
        }
    }

    private void loadCollectCount() {
        mModel.loadCollectCount(getContext(),user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean msg) {
                mTvCollectCount.setText("0");
                if (msg != null && msg.isSuccess()) {
                    mTvCollectCount.setText(msg.getMsg());
                }
            }

            @Override
            public void onError(String error) {
                mTvCollectCount.setText("0");
            }
        });
    }

    @OnClick({R.id.tv_center_settings, R.id.center_user_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_center_settings:
            case R.id.center_user_info:
                MFGT.gotoPersonInfo(getActivity());
        }
    }
}
