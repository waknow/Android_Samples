package com.example.administrator.eu4you;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity implements CountriesFragment.Contract {
    private CountriesFragment countries = null;
    private DetailsFragment details = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FragmentManager fm = getFragmentManager();
        countries = (CountriesFragment) fm.findFragmentById(R.id.countries);
        if (countries == null) {
            countries = new CountriesFragment();
            fm.beginTransaction()
                    .add(R.id.countries, countries)
                    .commit();
        }

        details = (DetailsFragment) fm.findFragmentById(R.id.details);
        if (details == null && findViewById(R.id.details) != null) {
            details = new DetailsFragment();
            fm.beginTransaction()
                    .add(R.id.details, details)
                    .commit();
        }
    }

    @Override
    public void onCountrySelected(Country c) {
        String url = getString(c.url);
        if (details != null && details.isVisible()) {
            details.loadUrl(url);
        } else {
            Intent i = new Intent(this, DetailsActivity.class);
            i.putExtra(DetailsActivity.EXTRA_URL, url);
            startActivity(i);
        }
    }

    @Override
    public boolean isPersistentSelection() {
        return details != null && details.isVisible();
    }
}
