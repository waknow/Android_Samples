package com.commonsware.listsamples.selection;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.commonsware.listsamples.R;

public class CheckListActivity extends android.app.ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, Items.getItems()));
    }
}
