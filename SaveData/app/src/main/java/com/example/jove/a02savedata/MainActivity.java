package com.example.jove.a02savedata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public enum SaveMode {
        Shared_Preferences,
        File_Inner_File,
        File_Inner_Cache,
        File_External_File,
        File_External_Cache,
        Sql
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSaveWithSharedPreferences(View view) {
        start(SaveMode.Shared_Preferences);
    }

    public void startSaveWithFileInnerFileDir(View view) {
        start(SaveMode.File_Inner_File);
    }

    public void startSaveWithFileInnerCacheDir(View view) {
        start(SaveMode.File_Inner_Cache);
    }

    public void startSaveWithFileExternalFile(View view) {
        start(SaveMode.File_External_File);
    }

    public void startSaveWithFileExternalCache(View view) {
        start(SaveMode.File_External_Cache);
    }

    public void startSaveWithSql(View view) {
        start(SaveMode.Sql);
    }

    private void start(SaveMode model) {
        Intent intent = new Intent(this, SaveActivity.class);
        intent.putExtra("saveModel", model.name());
        startActivity(intent);
    }
}
