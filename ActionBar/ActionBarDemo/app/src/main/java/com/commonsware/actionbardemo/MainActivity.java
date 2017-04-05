package com.commonsware.actionbardemo;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ListActivity {
    private List<String> words = null;
    private ArrayAdapter<String> adapter;
    private static final String[] items = Items.getItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
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
                Toast.makeText(this, R.string.about_toast, Toast.LENGTH_LONG)
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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, words);
        setListAdapter(adapter);
    }

    private void addWord() {
        if (adapter.getCount() < items.length) {
            adapter.add(items[adapter.getCount()]);
        }
    }
}
