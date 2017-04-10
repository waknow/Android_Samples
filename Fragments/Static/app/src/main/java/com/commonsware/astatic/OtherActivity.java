package com.commonsware.astatic;

import android.os.Bundle;

public class OtherActivity extends LifecycleLoggingActivity {
    public static final String EXTRA_MESSAGE = "msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }
}
