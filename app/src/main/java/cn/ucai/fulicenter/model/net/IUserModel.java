package cn.ucai.fulicenter.model.net;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public interface IUserModel {
    void login(Context context,String userName,String password,OnCompleteListener<String> listener);
    void register(Context context,String userName,String nick,String password,
                  OnCompleteListener<String> listener);
    void updateNick(Context context,String username,String newNick,OnCompleteListener<String> listener);
}
