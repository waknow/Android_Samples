package com.commonsware.listsamples.selection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.commonsware.listsamples.R;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private TextView selection;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        selection = (TextView) findViewById(R.id.spinner_selection);
        items = Items.getItems();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selection.setText(String.format("Selection: %s",items[position]));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selection.setText("Selection:");
    }
}
