package com.example.jove.a02savedata;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.jove.a02savedata.db.StateContract;
import com.example.jove.a02savedata.db.StateDbHelper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveActivity extends AppCompatActivity {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
    private Gson gson = new Gson();
    private StateDbHelper stateDbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        if (stateDbHelper == null) {
            stateDbHelper = new StateDbHelper(getBaseContext());
        }

        Intent intent = getIntent();
        Object model = intent.getStringExtra("saveModel");
        if (model == null) {
            setText("can not find save mode!");
        } else {
            MainActivity.SaveMode saveMode = MainActivity.SaveMode.valueOf((String) model);
            switch (saveMode) {
                case Shared_Preferences:
                    withSharedPreference();
                    break;
                case File_Inner_File:
                    withFileInnerFileDir();
                    break;
                case File_Inner_Cache:
                    withFileInnerCacheDir();
                    break;
                case File_External_File:
                    withFileExternalFile();
                    break;
                case File_External_Cache:
                    withFileExternalCache();
                    break;
                case Sql:
                    withSql();
                    break;
                default:
                    setText("unsupported model " + model);
            }
        }
    }

    private void withSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.key_preference), Context.MODE_PRIVATE);
        String saveDate = sharedPreferences.getString(getString(R.string.key_preference_save_date), "");
        Integer count = sharedPreferences.getInt(getString(R.string.key_preference_open_count), 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if ("".equals(saveDate)) {
            saveDate = sdf.format(new Date());
            editor.putString(getString(R.string.key_preference_save_date), saveDate);
        }
        editor.putInt(getString(R.string.key_preference_open_count), count + 1);
        editor.apply();

        setText(saveDate, count);
    }

    private void withFileInnerFileDir() {
        withFile(getFilesDir());
    }

    private void withFileInnerCacheDir() {
        withFile(getCacheDir());
    }

    private void withFileExternalFile() {
        boolean canRead = isExternalStorageReadable();
        boolean canWrite = isExternalStorageWriteable();
        if (!canRead || !canWrite) {
            setText("External Storage is Not Available! read:" + canRead + " write:" + canWrite);
            return;
        }
        withFile(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
    }

    private void withFileExternalCache() {
        boolean canRead = isExternalStorageReadable();
        boolean canWrite = isExternalStorageWriteable();
        if (!canRead || !canWrite) {
            setText("External Storage is Not Available! read:" + canRead + " write:" + canWrite);
            return;
        }
        withFile(getExternalCacheDir());
    }

    private void withSql() {
        SQLiteDatabase db = stateDbHelper.getReadableDatabase();

        String[] projections = {
                StateContract.StateEntry.COLUMN_NAME_SAVE_DATE,
                StateContract.StateEntry.TABLE_NAME_COUNT
        };

        String selection = StateContract.StateEntry._ID + " = ?";
        String[] selectArgs = {"1"};

        Cursor cursor = db.query(StateContract.StateEntry.TABLE_NAME, projections, selection, selectArgs, null, null, null);
        int colCount = cursor.getCount();
        String saveDate = "";
        long count = 0;
        if (cursor.moveToFirst()) {
            saveDate = cursor.getString(cursor.getColumnIndexOrThrow(StateContract.StateEntry.COLUMN_NAME_SAVE_DATE));
            count = cursor.getLong(cursor.getColumnIndexOrThrow(StateContract.StateEntry.TABLE_NAME_COUNT));
            cursor.close();
        }
        setText(saveDate, (int) count);

        if ("".equals(saveDate)) {
            saveDate = sdf.format(new Date());
        }
        count++;
        ContentValues values = new ContentValues();
        values.put(StateContract.StateEntry._ID, 1);
        values.put(StateContract.StateEntry.COLUMN_NAME_SAVE_DATE, saveDate);
        values.put(StateContract.StateEntry.TABLE_NAME_COUNT, count);

        db = stateDbHelper.getWritableDatabase();
        if (colCount == 0) {
            db.insert(StateContract.StateEntry.TABLE_NAME, null, values);
        } else {
            selection = StateContract.StateEntry._ID + " = ?";
            selectArgs[0] = "1";
            db.update(StateContract.StateEntry.TABLE_NAME, values, selection, selectArgs);
        }
    }

    private boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_BAD_REMOVAL.equals(state);
    }

    private void withFile(File fileDir) {
        File file = new File(fileDir, "state.json");
        Log.v(this.getLocalClassName(), file.getPath());
        String content = null;
        try {
            content = readAllAsString(file);
        } catch (IOException e) {
            setText("Read File Failed: " + e.getClass().getName() + "\n -> " + e.getMessage());
        } catch (Exception e) {
            setText("Other exception With File Read: " + e.getClass().getName() + "\n ->" + e.getMessage());
        }
        if (content == null) {
            return;
        }
        FileContent fileContent;
        if ("".equals(content)) {
            Log.v(this.getLocalClassName(), "new file content");
            fileContent = new FileContent();
            fileContent.setSaveDate(sdf.format(new Date()));
        } else {
            Log.v(this.getLocalClassName(), "parse file content");
            fileContent = gson.fromJson(content, FileContent.class);
        }
        setText(fileContent.getSaveDate(), fileContent.getCount());
        fileContent.increaseCount();
        try {
            writeToString(file, fileContent);
        } catch (IOException e) {
            setText("Write File Failed: " + e.getClass().getName() + "\n -> " + e.getMessage());
        } catch (Exception e) {
            setText("Other exception With File Write: " + e.getClass().getName() + "\n ->" + e.getMessage());
        }
    }

    private String readAllAsString(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        }
    }

    private void writeToString(File file, FileContent fileContent) throws IOException {
        String json = gson.toJson(fileContent);
        Log.v(this.getLocalClassName(), "write file " + json);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(json);
            bw.flush();
        }
    }

    private void setText(String saveDate, int count) {
        String text = String.format(Locale.US, "Current Date:%s\nSave Date:%s\nView Count:%d", sdf.format(new Date()), saveDate, count);
        TextView textView = (TextView) findViewById(R.id.save_activity_intro);
        textView.setText(text, TextView.BufferType.NORMAL);
    }

    private void setText(String text) {
        TextView textView = (TextView) findViewById(R.id.save_activity_intro);
        textView.setText(text, TextView.BufferType.NORMAL);
    }

    private class FileContent {
        private String saveDate;
        private Integer count;

        public String getSaveDate() {
            if (saveDate == null) {
                saveDate = "";
            }
            return saveDate;
        }

        public void setSaveDate(String saveDate) {
            this.saveDate = saveDate;
        }

        public Integer getCount() {
            if (count == null) {
                count = 0;
            }
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public void increaseCount() {
            if (this.count == null) {
                count = 0;
            }
            this.count++;
        }
    }
}
