package com.commonsware.empublite;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.commonsware.cwac.security.ZipUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/2.
 */

public class DownloadCheckService extends IntentService {
    private static final String OUR_BOOK_DATE = "20120418";
    private static final String UPDATE_FILENAME = "book.zip";
    public static final String UPDATE_BASEDIR = "updates";

    private static final AtomicBoolean isChecking = new AtomicBoolean(false);


    public DownloadCheckService() {
        super("DownloadCheckService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!isChecking.compareAndSet(false, true)) {
            Log.d(getClass().getSimpleName(), "is already checking...");
            return;
        }
        try {
            Log.d(getClass().getSimpleName(), "trying get update url...");
            String url = getUpdateUrl();
            if (url != null) {
                Log.d(getClass().getSimpleName(), "downloading from url " + url);
                File book = download(url);
                File updateDir = new File(getFilesDir(), UPDATE_BASEDIR);

                updateDir.mkdirs();
                Log.d(getClass().getSimpleName(), "unzipping file...");
                ZipUtils.unzip(book, updateDir);
                book.delete();

                Log.d(getClass().getSimpleName(), "firing BookUpdateEvent...");
                EventBus.getDefault().post(new BookUpdatedEvent());
            } else {
                Log.d(getClass().getSimpleName(), "got no url, no need to update");
            }
        } catch (IOException | ZipUtils.UnzipException e) {
            Log.e(getClass().getSimpleName(), "Exception downloading update", e);
        } finally {
            isChecking.set(false);
        }
    }

    private String getUpdateUrl() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://commonsware.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookUpdateInterface updateInterface = retrofit.create(BookUpdateInterface.class);
        BookUpdateInfo info = updateInterface.update().execute().body();
        if (info.updatedOn.compareTo(OUR_BOOK_DATE) > 0) {
            return info.updateUrl;
        }
        return null;
    }

    private File download(String url) throws IOException {
        File output = new File(getFilesDir(), UPDATE_FILENAME);
        if (output.exists()) {
            output.delete();
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        BufferedSink sink = Okio.buffer(Okio.sink(output));

        sink.writeAll(response.body().source());
        sink.close();

        return output;
    }
}
