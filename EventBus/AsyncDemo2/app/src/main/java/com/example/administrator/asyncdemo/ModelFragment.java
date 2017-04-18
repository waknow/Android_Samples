package com.example.administrator.asyncdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ModelFragment extends Fragment {
    private static final String[] items = Items.getItems();
    private List<String> model = Collections.synchronizedList(new ArrayList<String>());
    private boolean isStart = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if (!isStart) {
            isStart = true;
            new LoadingWordThread().start();
        }
    }

    public ArrayList<String> getModel() {
        return new ArrayList<>(model);
    }

    private class LoadingWordThread extends Thread {
        @Override
        public void run() {
            for (String item : items) {
                if (isInterrupted()) {
                    break;
                }
                model.add(item);
                EventBus.getDefault().post(new WordReadyEvent(item));
                SystemClock.sleep(400);
            }
        }
    }
}
