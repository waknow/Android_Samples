package com.jove.demo.cryptpbenchmark;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jove.demo.cryptpbenchmark.algorithm.Aes;
import com.jove.demo.cryptpbenchmark.algorithm.IAlgorithm;
import com.jove.demo.cryptpbenchmark.algorithm.TripDes;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        Button button = (Button) findViewById(R.id.start);
        button.setEnabled(false);
        TextView display = (TextView) findViewById(R.id.display);

        new TestTask(display, button).execute(new Aes(), new TripDes());
    }


    private class TestTask extends AsyncTask<IAlgorithm, String, String> {
        private final TextView display;
        private final Button button;

        private Random random;

        public TestTask(TextView view, Button button) {
            this.display = view;
            this.button = button;

            random = new Random(System.currentTimeMillis());
        }

        @Override
        protected void onPreExecute() {
            display.setText("");
        }

        @Override
        protected String doInBackground(IAlgorithm... params) {
            if (params == null) {
                return "No Algorithm to test";
            }
            byte[] data = randomData(130716);
            int times = 100;
            for (IAlgorithm algorithm : params) {
                try {
                    publishProgress(algorithm.name() + " running...");
                    algorithm.init();
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < times; i++) {
                        algorithm.decrypt(algorithm.encrypt(data));
                    }
                    publishProgress(algorithm.name() + " completed, avg: " + (System.currentTimeMillis() - start) / times + "ms");
                } catch (Exception e) {
                    publishProgress(String.format("[Exception] %s got exception: %s", algorithm.name(), e.getMessage()));
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values == null || values.length == 0) {
                return;
            }
            for (String value : values) {
                display.append(new Date().getTime() + ": " + value + "\n");
            }
        }

        @Override
        protected void onPostExecute(String s) {
            display.append("Completed");
            button.setEnabled(true);
        }

        private byte[] randomData(int len) {
            byte[] data = new byte[len];
            random.nextBytes(data);
            return data;
        }
    }
}
