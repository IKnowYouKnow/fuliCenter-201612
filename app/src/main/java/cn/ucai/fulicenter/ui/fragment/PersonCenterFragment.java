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
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.dao.UserDao;
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

    public PersonCenterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_center, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void showUserInfo() {
        mTvUserName.setText(user.getMuserNick());
        ImageLoader.downloadImg(getActivity(),mIvUserAvatar,user.getAvatarUrl());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        user = FuLiCenterApplication.getUserLogin();
        if (user == null) {
            MFGT.gotoLoginActivity(getActivity());
        } else {
            showUserInfo();
        }
    }

}
