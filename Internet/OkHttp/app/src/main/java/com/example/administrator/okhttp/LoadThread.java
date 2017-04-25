package com.example.administrator.okhttp;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/4/25.
 */

public class LoadThread extends Thread {
    static final String SO_URL = "https://api.stackexchange.com/2.1/questions?"
            + "order=desc&sort=creation&site=stackoverflow&tagged=android";

    @Override
    public void run() {
        Log.d(getClass().getSimpleName(), "Request data start");
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(SO_URL).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Reader in = response.body().charStream();
                BufferedReader reader = new BufferedReader(in);
                SOQuestions questions = new Gson().fromJson(reader, SOQuestions.class);
                EventBus.getDefault().post(new QuestionsLoadedEvent(questions));
            } else {
                Log.e(getClass().getSimpleName(), response.toString());
            }
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Exception parsing json", e);
        }
        Log.d(getClass().getSimpleName(), "Request data done.");
    }
}
