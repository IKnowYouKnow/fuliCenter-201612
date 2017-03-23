package cn.ucai.fulicenter.model.net;

import android.content.Context;
import android.content.Intent;

import java.io.File;

import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public interface IUserModel {
    void login(Context context,String userName,String password,OnCompleteListener<String> listener);
    void register(Context context,String userName,String nick,String password,
                  OnCompleteListener<String> listener);
    void updateNick(Context context,String username,String newNick,OnCompleteListener<String> listener);

    void uploadAvatar(Context context, String username, File file, OnCompleteListener<String> listener);

    void loadCollectCount(Context context, String username, OnCompleteListener<MessageBean> listener);
}
