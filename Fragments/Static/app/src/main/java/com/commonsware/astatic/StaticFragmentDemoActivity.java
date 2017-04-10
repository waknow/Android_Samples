package com.commonsware.astatic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StaticFragmentDemoActivity extends LifecycleLoggingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showOther(View view) {
        Intent other = new Intent(this, OtherActivity.class);
        other.putExtra(OtherActivity.EXTRA_MESSAGE, getString(R.string.other));
        startActivity(other);
    }
}
