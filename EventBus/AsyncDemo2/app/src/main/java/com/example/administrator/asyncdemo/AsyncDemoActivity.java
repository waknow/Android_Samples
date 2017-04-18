package com.example.administrator.asyncdemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class AsyncDemoActivity extends Activity {
    private static final String MODEL_TAG = "model";
    private ModelFragment mFrag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager mgr = getFragmentManager();
        FragmentTransaction trans = mgr.beginTransaction();

        mFrag = (ModelFragment) mgr.findFragmentByTag(MODEL_TAG);
        if (mFrag == null) {
            mFrag = new ModelFragment();
            trans.add(mFrag, MODEL_TAG);
        }

        AsyncDemoFragment demo = (AsyncDemoFragment) mgr.findFragmentById(android.R.id.content);
        if (demo == null) {
            demo = new AsyncDemoFragment();
            trans.add(android.R.id.content, demo);
        }

        demo.setModel(mFrag.getModel());

        if (!trans.isEmpty()) {
            trans.commit();
        }
    }
}
