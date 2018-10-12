package com.commonsware.listsamples.selection;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.commonsware.listsamples.R;

public class GridActivity extends Activity implements AdapterView.OnItemClickListener {
    private TextView selection;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        selection = (TextView) findViewById(R.id.grid_selection);
        items = Items.getItems();
        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new ArrayAdapter<>(this, R.layout.cell, items));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selection.setText(items[position]);
    }
}
