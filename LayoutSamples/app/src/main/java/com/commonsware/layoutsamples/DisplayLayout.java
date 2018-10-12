package com.commonsware.layoutsamples;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Integer layout = getIntent().getIntExtra("layout", R.layout.activity_display_layout);
        setContentView(layout);
    }
}
