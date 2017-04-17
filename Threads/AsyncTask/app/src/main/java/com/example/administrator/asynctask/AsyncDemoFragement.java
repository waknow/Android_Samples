package com.example.administrator.asynctask;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/17.
 */

public class AsyncDemoFragement extends ListFragment {
    private static final String[] items = Items.getItems();
    private ArrayList<String> model = new ArrayList<>();
    private ArrayAdapter<String> adapter = null;
    private AddStringTask task = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        task = new AddStringTask();
        task.execute();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, model);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setScrollbarFadingEnabled(false);
        setListAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        if (task != null) {
            task.cancel(false);
        }
        super.onDestroyView();
    }

    private class AddStringTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (String item : items) {
                if (isCancelled()) {
                    break;
                }
                publishProgress(item);
                SystemClock.sleep(400);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values != null && !isCancelled()) {
                for (String value : values) {
                    adapter.add(value);
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getActivity(), R.string.done, Toast.LENGTH_LONG).show();
            task = null;
        }
    }

}
