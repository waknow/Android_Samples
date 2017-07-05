package com.jove.demo.staticizer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setText(R.id.name, Test.NAME_OF_USER);
        setText(R.id.age, String.valueOf(Test.AGE));
    }

    private void setText(int id, String content) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(content);
    }
}
