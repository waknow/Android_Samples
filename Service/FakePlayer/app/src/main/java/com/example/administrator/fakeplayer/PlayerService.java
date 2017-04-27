package com.example.administrator.fakeplayer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/27.
 */

public class PlayerService extends Service {
    public static final String EXTRA_PLAYLIST = "EXTRA_PLAYLIST";
    public static final String EXTRA_SHUFFLE = "EXTRA_SHUFFLE";
    private boolean isPlaying = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getSimpleName(), "onStartCommand");

        String playList = intent.getStringExtra(EXTRA_PLAYLIST);
        boolean useShuffle = intent.getBooleanExtra(EXTRA_SHUFFLE, false);

        play(playList, useShuffle);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "onDestroy");
        stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void play(String playList, boolean useShuffle) {
        if (!isPlaying) {
            Log.i(getClass().getSimpleName(), "Got to play()");
            isPlaying = true;
        }
    }

    private void stop() {
        if (isPlaying) {
            Log.i(getClass().getSimpleName(), "Got to stop()");
            isPlaying = false;
        }
    }
}
