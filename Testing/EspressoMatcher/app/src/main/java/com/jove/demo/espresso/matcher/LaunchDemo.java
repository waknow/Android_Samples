package com.jove.demo.espresso.matcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public class LaunchDemo extends Activity {

    private TextInputLayout til;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        til=(TextInputLayout)findViewById(R.id.til);
        til.setErrorEnabled(true);
    }

    public void showMe(View v) {
        EditText urlField=(EditText)findViewById(R.id.url);
        String url=urlField.getText().toString();

        if (Patterns.WEB_URL.matcher(url).matches()) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        else {
            til.setError(getString(R.string.til_error));
        }
    }
}
