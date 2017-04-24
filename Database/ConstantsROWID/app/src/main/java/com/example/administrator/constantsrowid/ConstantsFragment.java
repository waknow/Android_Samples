package com.example.administrator.constantsrowid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Administrator on 2017/4/24.
 */

public class ConstantsFragment extends ListFragment implements DialogInterface.OnClickListener {
    private DatabaseHelper db = null;
    private Cursor current = null;
    private AsyncTask task = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.rouw,
                current, new String[]{
                DatabaseHelper.TITLE,
                DatabaseHelper.VALUE},
                new int[]{R.id.title, R.id.value},
                0);
        setListAdapter(adapter);
        if (current == null) {
            db = new DatabaseHelper(getActivity());
            task = new LoadCursorTask().execute();
        }
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.cancel(false);
        }
        Cursor cursor = ((CursorAdapter) getListAdapter()).getCursor();
        if (cursor != null) {
            cursor.close();
        }
        if (db != null) {
            db.close();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                add();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View addView = inflater.inflate(R.layout.add_edit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_title)
                .setView(addView)
                .setPositiveButton(R.string.add_buton, this)
                .setNegativeButton(R.string.cancel, this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentValues values = new ContentValues(2);
        AlertDialog dlg = (AlertDialog) dialog;
        String title = ((EditText) dlg.findViewById(R.id.title)).getText().toString();
        String value = ((EditText) dlg.findViewById(R.id.value)).getText().toString();

        if (isEmptyString(title) || isEmptyString(value)) {
            Log.w(getClass().getSimpleName(), "Title of value from  the dialog is empty");
            return;
        }

        values.put(DatabaseHelper.TITLE, title);
        values.put(DatabaseHelper.VALUE, value);

        task = new InsertTask().execute(values);
    }

    private boolean isEmptyString(String value) {
        return value == null || "".equals(value);
    }

    abstract class BaseTask<T> extends AsyncTask<T, Void, Cursor> {
        @Override
        protected void onPostExecute(Cursor cursor) {
            Log.d(getClass().getSimpleName(), "change cursor on post execute");
            ((CursorAdapter) getListAdapter()).changeCursor(cursor);
            current = cursor;
            task = null;
        }

        Cursor doQuery() {
            Cursor result = db.getReadableDatabase()
                    .query(DatabaseHelper.TABLE,
                            new String[]{"ROWID as _id",
                                    DatabaseHelper.TITLE,
                                    DatabaseHelper.VALUE},
                            null, null, null, null, DatabaseHelper.TITLE);
            Log.d(getClass().getSimpleName(), String.format("got %d results on do query", result.getCount()));
            return result;
        }

        void test() {
            Cursor result = doQuery();
            while (result.moveToNext()) {
                Log.d(getClass().getSimpleName(), String.format("result: %d %s %d", result.getInt(0), result.getString(1), result.getInt(0)));
            }
        }
    }

    private class LoadCursorTask extends BaseTask<ContentValues> {

        @Override
        protected Cursor doInBackground(ContentValues... params) {
            test();
            return doQuery();
        }
    }

    private class InsertTask extends BaseTask<ContentValues> {

        @Override
        protected Cursor doInBackground(ContentValues... params) {
            db.getWritableDatabase()
                    .insert(DatabaseHelper.TABLE,
                            DatabaseHelper.VALUE, params[0]);
            test();
            return doQuery();
        }
    }
}
