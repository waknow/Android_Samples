package com.commonsware.listsamples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String packageName = MainActivity.class.getPackage().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        if (view instanceof Button) {
            onButtonClick((Button) view);
            return;
        }
        Toast.makeText(this, "Unexpected source of this click event", Toast.LENGTH_LONG).show();
    }

    private void onButtonClick(Button button) {
        Object obj = button.getTag();
        if (obj == null || !(obj instanceof String)) {
            showToast(String.format("Button '%s' has no string tag!", button.getText()));
            return;
        }
        String tag = (String) obj;
        if ("".equals(tag)) {
            showToast(String.format("Button '%s' has a empty tag!", button.getText()));
            return;
        }
        String cls = packageName + button.getTag();
        try {
            Log.d("main.click", cls);
            Class<?> activityClass = getClassLoader().loadClass(cls);
            startActivityWithClass((Class<Activity>) activityClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Can not find a activity under " + cls, Toast.LENGTH_LONG).show();
        } catch (ClassCastException e) {
            showToast(String.format("The class found with %s is can not cast to Activity", cls));
        }
    }

    private void startActivityWithClass(Class<Activity> clz) {
        startActivity(new Intent(this, clz));
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
