package com.example.administrator.constantsrowid;

import android.app.Activity;
import android.os.Bundle;

public class ContentsBrowserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new ConstantsFragment())
                    .commit();
        }
    }
}
