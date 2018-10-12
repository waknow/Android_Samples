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

public class ViewHolderActivity extends android.app.ListActivity {
    private static final String[] items = Items.getItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_holder);
        setListAdapter(new IconicAdapter());
    }

    private class IconicAdapter extends ArrayAdapter<String> {
        IconicAdapter() {
            super(ViewHolderActivity.this, R.layout.row, R.id.label, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            long start = System.currentTimeMillis();
            View row = super.getView(position, convertView, parent);
            ViewHolder holder = (ViewHolder) row.getTag();
            if (holder == null) {
                holder = new ViewHolder(row);
                row.setTag(holder);
            }
            if (getModel(position).length() > 4) {
                holder.icon.setImageResource(R.drawable.delete);
            } else {
                holder.icon.setImageResource(R.drawable.ok);
            }
            holder.size.setText(String.format(getString(R.string.size_template), items[position].length()));
            Log.d(IconicAdapter.class.getSimpleName(), String.format("%dms", System.currentTimeMillis() - start));
            return row;
        }

        private String getModel(int position) {
            return ((IconicAdapter) getListAdapter()).getItem(position);
        }
    }

    private class ViewHolder {
        ImageView icon = null;
        TextView size = null;

        ViewHolder(View view) {
            this.icon = (ImageView) view.findViewById(R.id.icon);
            this.size = (TextView) view.findViewById(R.id.size);
        }
    }
}
