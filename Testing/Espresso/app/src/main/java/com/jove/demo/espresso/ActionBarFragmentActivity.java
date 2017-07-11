package com.jove.demo.espresso;

import android.app.Activity;
import android.os.Bundle;

public class ActionBarFragmentActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,
                            new ActionBarFragment()).commit();
        }
    }
}
