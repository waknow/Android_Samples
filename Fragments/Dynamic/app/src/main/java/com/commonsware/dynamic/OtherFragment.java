package com.commonsware.dynamic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by jove on 2017/4/10.
 */

public class OtherFragment extends android.app.ListFragment {
    private static final String[] items = Items.getItems();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items));
    }
}
