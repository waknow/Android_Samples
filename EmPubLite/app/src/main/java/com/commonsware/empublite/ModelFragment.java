package com.commonsware.empublite;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Administrator on 2017/4/20.
 */

public class ModelFragment extends Fragment {
    private final AtomicReference<BookContents> contents = new AtomicReference<>();
    private final AtomicReference<SharedPreferences> prefs = new AtomicReference<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        if (contents.get() == null) {
            new LoadThread(context).start();
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onBookUpdated(BookUpdatedEvent event) {
        if (getActivity() != null) {
            Log.d(getClass().getSimpleName(), "got BookUpdateInfoEvent, reloading content...");
            new LoadThread(getActivity()).start();
        }
    }

    public BookContents getBook() {
        return contents.get();
    }

    public SharedPreferences getPrefs() {
        return prefs.get();
    }

    private class LoadThread extends Thread {
        private Context ctx = null;

        LoadThread(Context ctx) {
            this.ctx = ctx.getApplicationContext();
        }

        @Override
        public void run() {
            prefs.set(PreferenceManager.getDefaultSharedPreferences(this.ctx));

            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Gson gson = new Gson();
            File baseDir = new File(ctx.getFilesDir(), DownloadCheckService.UPDATE_BASEDIR);

            try {
                InputStream is;
                if (baseDir.exists()) {
                    is = new FileInputStream(new File(baseDir, "contents.json"));
                } else {
                    is = this.ctx.getAssets().open("book/contents.json");
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                contents.set(gson.fromJson(reader, BookContents.class));
                if (baseDir.exists()) {
                    contents.get().setBaseDir(baseDir);
                }
                EventBus.getDefault().post(new BookLoadedEvent(getBook()));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing json", e);
            }
        }
    }
}
