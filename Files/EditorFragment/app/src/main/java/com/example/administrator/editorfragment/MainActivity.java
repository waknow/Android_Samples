package com.example.administrator.editorfragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import io.karim.MaterialTabs;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AbstractPermissionActivity {

    @Override
    protected String[] getDesiredPermission() {
        return new String[]{WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void onPermissionDenied() {
        Toast.makeText(this, R.string.msg_sorry, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onReady(Bundle state) {
        setContentView(R.layout.main);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SampleAdapter(this, getFragmentManager()));
        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
    }
}
