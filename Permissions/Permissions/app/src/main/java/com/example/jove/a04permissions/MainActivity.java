package com.example.jove.a04permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int MY_PERMISSION_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkPermission(View view) {
        final Activity thisActivity = this;
        if (ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_CONTACTS)) {
                showToast("READ_CONTACTS you already deny the permission request");
            } else {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSION_REQUEST_READ_CONTACTS);
            }
        } else {
            showToast("READ_CONTACTS is already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToast("READ_CONTACTS is granted");
                } else {
                    showToast("READ_CONTACTS is denied");
                }
                return;
        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
