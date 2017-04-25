package com.example.administrator.picasso;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

public class MainActivity extends Activity implements QuestionsFragment.Contract {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupStrictMode();

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new QuestionsFragment())
                    .commit();
        }
    }

    @Override
    public void onQuestion(Item question) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(question.link)));
    }

    private void setupStrictMode() {
        StrictMode.ThreadPolicy.Builder builder = new StrictMode
                .ThreadPolicy
                .Builder()
                .detectAll()
                .penaltyLog();
        if (BuildConfig.DEBUG) {
            builder.penaltyFlashScreen();
        }
        StrictMode.setThreadPolicy(builder.build());
    }
}
