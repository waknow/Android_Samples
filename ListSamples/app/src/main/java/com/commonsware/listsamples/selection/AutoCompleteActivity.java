package com.commonsware.listsamples.selection;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.commonsware.listsamples.R;

public class AutoCompleteActivity extends Activity implements TextWatcher {
    private AutoCompleteTextView edit;
    private TextView selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete);
        String[] items = Items.getItems();
        selection = (TextView) findViewById(R.id.auto_complete_selection);
        edit = (AutoCompleteTextView) findViewById(R.id.auto_complete);
        edit.addTextChangedListener(this);
        edit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        selection.setText(edit.getText());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
