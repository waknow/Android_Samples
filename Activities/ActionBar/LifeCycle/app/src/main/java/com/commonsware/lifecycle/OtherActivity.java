package com.commonsware.lifecycle;

import android.app.ListActivity;
import android.os.Bundle;

public class OtherActivity extends LifecycleLoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }
}
