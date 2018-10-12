package com.commonsware.empublite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Process;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/4/24.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "emuplite.db";
    private static final int SCHEMA_VERSION = 1;
    private static DatabaseHelper singleton = null;

    static synchronized DatabaseHelper getInstance(Context ctx) {
        if (singleton == null) {
            singleton = new DatabaseHelper(ctx);
        }
        return singleton;
    }

    private DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes (position integer primary key, prose text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new RuntimeException("This should not be called!");
    }

    void loadNote(int position) {
        new LoadThread(position).start();
    }

    void updateNote(int position, String prose) {
        new SaveThread(position, prose).start();
    }

    private class LoadThread extends Thread {
        private int position = -1;

        LoadThread(int position) {
            this.position = position;
        }

        @Override
        public void run() {
            if (this.position < 0) {
                return;
            }
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            String[] args = {String.valueOf(this.position)};
            Cursor c = getReadableDatabase()
                    .rawQuery("select prose from notes where position = ?", args);
            if (c.getCount() > 0) {
                c.moveToFirst();
                EventBus.getDefault().post(new NoteLoadedEvent(this.position, c.getString(0)));
            }
            c.close();
        }
    }

    private class SaveThread extends Thread {
        private int position = -1;
        private String prose = null;

        SaveThread(int position, String prose) {
            this.position = position;
            this.prose = prose;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            String[] args = {String.valueOf(this.position), this.prose};
            getWritableDatabase().execSQL("insert or replace into notes(position, prose) values(?,?);", args);
        }
    }
}
