package com.example.administrator.postdelayed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PostDelayedDemoActivity extends Activity implements Runnable {
    private static final int PERIOD = 5000;
    private View root = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_delayed_demo);
        root = findViewById(android.R.id.content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        run();
    }

    @Override
    protected void onStop() {
        root.removeCallbacks(this);
        super.onStop();
    }

    @Override
    public void run() {
        Toast.makeText(PostDelayedDemoActivity.this, "Who-hoo!", Toast.LENGTH_LONG).show();
        root.postDelayed(this, PERIOD);
    }
}
