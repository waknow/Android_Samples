package com.example.administrator.hurl;

import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            HttpURLConnection c = (HttpURLConnection) new URL(SO_URL).openConnection();
            try {
                InputStream in = c.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                SOQuestions questions = new Gson().fromJson(reader, SOQuestions.class);
                reader.close();
                EventBus.getDefault().post(new QuestionsLoadedEvent(questions));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing json", e);
            } finally {
                c.disconnect();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Exception parsing json", e);
        }
        Log.d(getClass().getSimpleName(), "Request data done.");
    }
}
