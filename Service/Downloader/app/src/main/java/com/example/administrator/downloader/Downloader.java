package com.example.administrator.downloader;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/5/2.
 */

public class Downloader extends IntentService {
    public static final String ACTION_COMPLETE = "com.example.administrator.downloader.action.COMPLETE";

    public Downloader() {
        super("Downloader");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            Log.d(getClass().getSimpleName(), "onHandleIntent");
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!root.exists()) {
                Log.d(getClass().getSimpleName(), "mkdirs");
                root.mkdirs();
            }

            File output = new File(root, intent.getData().getLastPathSegment());
            if (output.exists()) {
                Log.d(getClass().getSimpleName(), "delete file");
                output.delete();
            }

            URL url = new URL(intent.getData().toString());
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            Integer contentLen = Integer.valueOf(c.getHeaderField("Content-Length"));
            if (contentLen == null) {
                contentLen = -1;
            }
            Log.d(getClass().getSimpleName(), String.format("Content Length: %d", contentLen));

            FileOutputStream fos = new FileOutputStream(output.getPath());
            BufferedOutputStream out = new BufferedOutputStream(fos);
            int total = 0;
            int len = 0;
            try {
                InputStream in = c.getInputStream();
                byte[] buffer = new byte[8192];
                while ((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                    total += len;
                    printProgress(len, contentLen);
                }
                out.flush();
            } finally {
                Log.d(getClass().getSimpleName(), "closing resources");
                fos.getFD().sync();
                out.close();
                c.disconnect();
            }
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "Exception in downloading", e);
        } finally {
            Log.d(getClass().getSimpleName(), "sending broadcasts");
            Intent i = new Intent(ACTION_COMPLETE);
            i.addFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
            boolean status = LocalBroadcastManager.getInstance(this).sendBroadcast(i);
            Log.d(getClass().getSimpleName(), String.format("broadcast send, %b", status));
        }
    }

    int total;
    float lastPercent = 0.0f;

    private void printProgress(int len, int contentLen) {
        total += len;
        float percent = total / (float) contentLen;
        if (percent - lastPercent >= 0.01) {
            Log.d(getClass().getSimpleName(), String.format("Downloading... %.2f%%", percent * 100));
            lastPercent = percent;
        }
    }
}

