package com.example.administrator.eu4youslidingpane;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;

public class MainActivity extends Activity implements CountriesFragment.Contract {
    private DetailsFragment details = null;
    private SlidingPaneLayout panes = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        details = (DetailsFragment) getFragmentManager().findFragmentById(R.id.details);
        panes = (SlidingPaneLayout) findViewById(R.id.pane);
        panes.openPane();
    }

    @Override
    public void onBackPressed() {
        if (panes.isOpen()) {
            super.onBackPressed();
        } else {
            panes.openPane();
        }
    }

    @Override
    public void onCountrySelected(Country c) {
        String url = getString(c.url);
        details.loadUrl(url);
        panes.closePane();
    }

    @Override
    public boolean isPersistentSelection() {
        return details != null && details.isVisible();
    }
}
