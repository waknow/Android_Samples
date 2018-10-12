package com.example.jove.a03intent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickStartOther(View view) {
        startActivity(new Intent(this, OthersActivity.class));
    }

    public void onClickStartOthersWithResult(View view) {
        startActivity(new Intent(this, OthersWithResult.class));
    }

    public void onClickShare(View view) {
        startActivity(new Intent(this, ShareActivity.class));
    }
}
