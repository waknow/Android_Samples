package com.example.jove.a02savedata.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jove on 2017/2/8.
 */

public class StateDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "state.db";

    public StateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYP = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "create table " + StateContract.StateEntry.TABLE_NAME + " (" +
                    StateContract.StateEntry._ID + "  INTEGER PRIMARY KEY," +
                    StateContract.StateEntry.COLUMN_NAME_SAVE_DATE + TEXT_TYPE + COMMA_SEP +
                    StateContract.StateEntry.TABLE_NAME_COUNT + INTEGER_TYP + ")";
    private static final String SQL_DELETE_ENTRIES =
            "drop table if exists " + StateContract.StateEntry.TABLE_NAME;
}
