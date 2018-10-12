package com.commonsware.android.perm.tutorial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.VideoRecorderActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final int RESULT_PICTURE_TAKEN = 1337;
    private static final int RESULT_VIDEO_RECORDED = 1338;
    private static final String PREF_IS_FIRST_RUN = "firstRun";
    private static final String[] PERMS_TAKE_PICTURE = {
            CAMERA,
            WRITE_EXTERNAL_STORAGE
    };
    private static final String[] PERMS_ALL = {
            CAMERA,
            WRITE_EXTERNAL_STORAGE,
            RECORD_AUDIO
    };
    private static final int RESULT_PERMS_INITIAL = 1339;
    private static final int RESULT_PERMS_TAKE_PICTURE = 1340;
    private static final int RESULT_PERMS_RECORD_VIDEO = 1341;
    private static final String STATE_BREADCURST = "com.commonsware.android.perm.tutorial.breadcurst";
    private static final String STATE_IN_PERMISSION = "com.commonsware.android.perm.tutorial.inPermission";
    private File rootDir;
    private SharedPreferences prefs;
    private TextView breadcurst;
    private boolean isInPermission = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState != null) {
            isInPermission = savedInstanceState.getBoolean(STATE_IN_PERMISSION, false);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        breadcurst = (TextView) findViewById(R.id.breadcrust);

        File downloads = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        rootDir = new File(downloads, "RuntimePermTutorial");
        rootDir.mkdirs();

        if (isFirstRun() && !isInPermission) {
            isInPermission = true;
            ActivityCompat.requestPermissions(this, PERMS_TAKE_PICTURE, RESULT_PERMS_INITIAL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Toast t = null;

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_PICTURE_TAKEN) {
                t = Toast.makeText(this, R.string.msg_pic_taken,
                        Toast.LENGTH_LONG);
            } else if (requestCode == RESULT_VIDEO_RECORDED) {
                t = Toast.makeText(this, R.string.msg_vid_recorded,
                        Toast.LENGTH_LONG);
            }

            t.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean sadTrombone = false;
        isInPermission = false;

        switch (requestCode) {
            case RESULT_PERMS_TAKE_PICTURE:
                if (canTakePicture()) {
                    takePictureForRealz();
                } else if (!shouldShowTakePictureRationale()) {
                    sadTrombone = true;
                }
                break;
            case RESULT_PERMS_RECORD_VIDEO:
                if (canRecordVideo()) {
                    recordVideoForRealz();
                } else if (!shouldShowRecordVideoRationale()) {
                    sadTrombone = true;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (sadTrombone) {
            Toast.makeText(this, R.string.msg_no_perm, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IN_PERMISSION, isInPermission);
        if (breadcurst.getVisibility() == View.VISIBLE) {
            outState.putCharSequence(STATE_BREADCURST, breadcurst.getText());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence cs = savedInstanceState.getCharSequence(STATE_BREADCURST);
        if (cs != null) {
            breadcurst.setText(cs);
            breadcurst.setVisibility(View.VISIBLE);
        }
    }

    public void takePicture(View v) {
        if (canTakePicture()) {
            takePictureForRealz();
        } else if (breadcurst.getVisibility() == View.GONE && shouldShowTakePictureRationale()) {
            breadcurst.setText(R.string.msg_take_picture);
            breadcurst.setVisibility(View.VISIBLE);
        } else {
            breadcurst.setVisibility(View.GONE);
            ActivityCompat.requestPermissions(this, netPermission(PERMS_TAKE_PICTURE), RESULT_PERMS_TAKE_PICTURE);
        }
    }

    public void recordVideo(View v) {
        if (canRecordVideo()) {
            recordVideoForRealz();
        } else if (breadcurst.getVisibility() == View.GONE && shouldShowRecordVideoRationale()) {
            breadcurst.setText(R.string.msg_record_video);
            breadcurst.setVisibility(View.VISIBLE);
        } else {
            breadcurst.setVisibility(View.GONE);
            ActivityCompat.requestPermissions(this, netPermission(PERMS_ALL), RESULT_PERMS_RECORD_VIDEO);
        }
    }

    private void takePictureForRealz() {
        Intent i = new CameraActivity.IntentBuilder(MainActivity.this)
                .to(new File(rootDir, "test.jpg"))
                .updateMediaStore()
                .build();

        startActivityForResult(i, RESULT_PICTURE_TAKEN);
    }

    private void recordVideoForRealz() {
        Intent i = new VideoRecorderActivity.IntentBuilder(MainActivity.this)
                .quality(VideoRecorderActivity.Quality.HIGH)
                .sizeLimit(5000000)
                .to(new File(rootDir, "test.mp4"))
                .updateMediaStore()
                .forceClassic()
                .build();

        startActivityForResult(i, RESULT_VIDEO_RECORDED);
    }

    private boolean isFirstRun() {
        boolean result = prefs.getBoolean(PREF_IS_FIRST_RUN, true);
        if (result) {
            prefs.edit().putBoolean(PREF_IS_FIRST_RUN, false).apply();
        }
        return result;
    }

    private boolean hasPermission(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean canTakePicture() {
        return hasPermission(CAMERA) && hasPermission(WRITE_EXTERNAL_STORAGE);
    }

    private boolean canRecordVideo() {
        return canTakePicture() && hasPermission(RECORD_AUDIO);
    }

    private boolean shouldShowTakePictureRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE);
    }

    private boolean shouldShowRecordVideoRationale() {
        return shouldShowTakePictureRationale()
                || ActivityCompat.shouldShowRequestPermissionRationale(this, RECORD_AUDIO);
    }

    private String[] netPermission(String[] wanted) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result.toArray(new String[result.size()]);
    }

}
