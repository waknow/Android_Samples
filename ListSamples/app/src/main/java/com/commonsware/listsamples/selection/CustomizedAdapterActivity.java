package com.commonsware.listsamples.selection;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.listsamples.R;

public class CustomizedAdapterActivity extends android.app.ListActivity {
    private static final String[] items = Items.getItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customized_adapter);
        setListAdapter(new IconicAdapter());
    }

    class IconicAdapter extends ArrayAdapter<String> {
        IconicAdapter() {
            super(CustomizedAdapterActivity.this, R.layout.row, R.id.label, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            long start = System.currentTimeMillis();
            View row = super.getView(position, convertView, parent);
            ImageView icon = (ImageView) row.findViewById(R.id.icon);
            if (items[position].length() > 4) {
                icon.setImageResource(R.drawable.delete);
            } else {
                icon.setImageResource(R.drawable.ok);
            }
            TextView size = (TextView) row.findViewById(R.id.size);
            size.setText(String.format(getString(R.string.size_template), items[position].length()));
            Log.d(IconicAdapter.class.getSimpleName(), String.format("%dms", System.currentTimeMillis() - start));
            return row;
        }
    }
}
