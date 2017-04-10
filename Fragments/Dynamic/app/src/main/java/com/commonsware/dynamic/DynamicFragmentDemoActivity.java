package com.commonsware.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DynamicFragmentDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showOther(View view) {
        startActivity(new Intent(this, OtherActivity.class));
    }
}
