package com.example.jove.a03intent;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (intent.getType() == null) {
            display("Please send/sendTo picture or text, you can see 03.Intent in the list, then select.");
        } else if (intent.getType().indexOf("image/") != -1) {
            Log.v("onCreate", "Process as image, origin is " + intent.getType());
            display("the type is picture");
        } else if (intent.getType().equals("text/plain")) {
            Log.v("onCreate", "Process as text, origin is " + intent.getType());
            display("this type is text");
        }
    }

    private void display(String text) {
        TextView textView = (TextView) findViewById(R.id.share_display);
        textView.setText(text);
    }
}
