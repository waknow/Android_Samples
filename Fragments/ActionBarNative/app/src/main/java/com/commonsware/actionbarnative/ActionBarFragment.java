package com.commonsware.actionbarnative;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jove on 2017/4/10.
 */

public class ActionBarFragment extends ListFragment {
    private List<String> words = null;
    private ArrayAdapter<String> adapter;
    private static final String[] items = Items.getItems();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (adapter == null){
            adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,new ArrayList<String>());
        }
        setListAdapter(adapter);
        initAdapter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addWord();
                return true;
            case R.id.reset:
                initAdapter();
                return true;
            case R.id.about:
                Toast.makeText(getActivity(), R.string.about_toast, Toast.LENGTH_LONG)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAdapter() {
        if (words == null) {
            words = new ArrayList<>();
        } else {
            words.clear();
        }
        words.addAll(Arrays.asList(items).subList(0, 5));
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, words);
        setListAdapter(adapter);
    }

    private void addWord() {
        if (adapter.getCount() < items.length) {
            adapter.add(items[adapter.getCount()]);
        }
    }
}
