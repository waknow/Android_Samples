package com.example.administrator.configchange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RotationFragmentDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new RotationFragment())
                    .commit();
        }
    }
}
