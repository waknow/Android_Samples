package com.example.administrator.asyncdemo;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/18.
 */

public class AsyncDemoFragment extends ListFragment {
    private ArrayAdapter<String> adapter = null;
    private ArrayList<String> model = null;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, model);
        getListView().setScrollbarFadingEnabled(false);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WordReadyEvent event) {
        adapter.add(event.getWord());
    }

    public void setModel(ArrayList<String> model) {
        this.model = model;
    }
}
