package com.commonsware.layoutsamples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static final Map<Integer, Integer> layoutMap = new LinkedHashMap<>();

    static {
        layoutMap.put(R.id.linear_bottom_then_top, R.layout.linear_bottom_then_top);
        layoutMap.put(R.id.linear_form, R.layout.linear_form);
        layoutMap.put(R.id.linear_stacked_percent, R.layout.linear_stacked_percent);
        layoutMap.put(R.id.linear_url_dialog, R.layout.linear_url_dialog);
        layoutMap.put(R.id.relative_bottom_then_top, R.layout.relative_bottom_then_top);
        layoutMap.put(R.id.relative_overlap, R.layout.relative_overlap);
        layoutMap.put(R.id.relative_url_dialog, R.layout.relative_url_dialog);
        layoutMap.put(R.id.table_form, R.layout.table_form);
        layoutMap.put(R.id.table_url_dialog, R.layout.table_url_dialog);
        layoutMap.put(R.id.constraint_bottom_then_top, R.layout.constraint_bottom_then_top);
        layoutMap.put(R.id.constraint_url_dialog, R.layout.constraint_url_dialog);
        layoutMap.put(R.id.constraint_stacked_percent, R.layout.constraint_stacked_percent);
    }

    public void onClick(View view) {
        Button button = null;
        if (view instanceof Button) {
            button = (Button) view;
        }
        if (button == null) {
            Toast.makeText(this, "The click event is not from Button!", Toast.LENGTH_LONG).show();
            return;
        }
        String name = button.getText().toString();
        Integer layout = layoutMap.get(button.getId());
        if (layout == null) {
            Toast.makeText(this, String.format("Button %s is not have relate layout!", name), Toast.LENGTH_LONG).show();
            return;
        }
        startWithLayout(layout);
    }

    private void startWithLayout(int layout) {
        Intent intent = new Intent(this, DisplayLayout.class);
        intent.putExtra("layout", layout);
        startActivity(intent);
    }
}
