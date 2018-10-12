package com.example.jove.a02savedata.db;

import android.provider.BaseColumns;

/**
 * Created by jove on 2017/2/8.
 */

public class StateContract {
    private StateContract() {
    }

    public static class StateEntry implements BaseColumns {
        public final static String TABLE_NAME = "entry";
        public final static String COLUMN_NAME_SAVE_DATE = "save_date";
        public final static String TABLE_NAME_COUNT = "count";
    }
}
