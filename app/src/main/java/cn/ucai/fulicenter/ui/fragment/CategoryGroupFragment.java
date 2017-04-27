package cn.ucai.fulicenter.ui.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class CategoryGroupFragment extends ListFragment {

    List<CategoryGroupBean> mCategoryGroupBeanList;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        new WorkThread().start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGroupList(ArrayList<CategoryGroupBean> list) {
        if (list != null && list.size() > 0) {
            mCategoryGroupBeanList = list;
            setListAdapter(new ArrayAdapter<CategoryGroupBean>(
                    getContext(),
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    list));

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        EventBus.getDefault().post(mCategoryGroupBeanList.get(position));
    }

    class WorkThread extends Thread{
        @Override
        public void run() {
            super.run();
            ICategoryModel model = new CategoryModel();
            model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
                @Override
                public void onSuccess(CategoryGroupBean[] result) {
                    ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    EventBus.getDefault().post(list);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
}
