package com.example.administrator.eu4you;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class DetailsActivity extends Activity {
    public static final String EXTRA_URL = "com.example.administrator.eu4you.EXTRA_URL";
    private String url = null;
    private DetailsFragment details = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();
        details = (DetailsFragment) fm.findFragmentById(android.R.id.content);
        if (details == null) {
            details = new DetailsFragment();
            fm.beginTransaction()
                    .add(android.R.id.content, details)
                    .commit();
        }
        url = getIntent().getStringExtra(EXTRA_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        details.loadUrl(url);
    }
}
