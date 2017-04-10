package com.commonsware.dynamic;

import android.os.Bundle;

public class OtherActivity extends LifecycleLoggingActivity {
    public static final String EXTRA_MESSAGE = "msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,
                            new OtherFragment())
                    .commit();
        }
    }
}
