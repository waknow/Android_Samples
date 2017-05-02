package com.example.administrator.downloader;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Administrator on 2017/5/2.
 */

public class DownloadFragment extends Fragment implements View.OnClickListener {
    private Button b;
    private final static int REQUEST_STORAGE = 1;

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter f = new IntentFilter(Downloader.ACTION_COMPLETE);
        LocalBroadcastManager
                .getInstance(getActivity())
                .registerReceiver(onEvent, f);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager
                .getInstance(getActivity())
                .unregisterReceiver(onEvent);
        super.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.main, container, false);
        b = (Button) result.findViewById(R.id.button);
        b.setOnClickListener(this);
        return result;
    }


    @Override
    public void onClick(View v) {
        if (hasPermission(WRITE_EXTERNAL_STORAGE)) {
            Log.d(getClass().getSimpleName(), "do download");
            doDownload();
        } else {
            Log.d(getClass().getSimpleName(), "request permission");
            FragmentCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (hasPermission(WRITE_EXTERNAL_STORAGE)) {
            Log.d(getClass().getSimpleName(), "request permission success, do download");
            doDownload();
        } else {
            Log.d(getClass().getSimpleName(), "request permission failed");
        }
    }

    private void doDownload() {
        b.setEnabled(false);
        Intent i = new Intent(getActivity(), Downloader.class);
        i.setData(Uri.parse("https://commonsware.com/Android/Android-1_0-CC.pdf"));
        getActivity().startService(i);
        Log.d(getClass().getSimpleName(), "service started");
    }

    private boolean hasPermission(String perm) {
        return ContextCompat.checkSelfPermission(getActivity(), perm) == PackageManager.PERMISSION_GRANTED;
    }

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(getClass().getSimpleName(), "got broadcast, download is complete");
            b.setEnabled(true);
            Toast.makeText(getActivity(), R.string.download_completed, Toast.LENGTH_LONG).show();
        }
    };
}
