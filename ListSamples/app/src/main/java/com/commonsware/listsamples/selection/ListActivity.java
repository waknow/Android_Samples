package com.commonsware.listsamples.selection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.listsamples.R;

public class ListActivity extends android.app.ListActivity {
    private TextView selection;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(ListActivity.class.getName(), "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        selection = (TextView) findViewById(R.id.selection);
        items = Items.getItems();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selection.setText(items[position]);
    }
}
