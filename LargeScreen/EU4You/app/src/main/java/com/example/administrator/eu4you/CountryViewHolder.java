package com.example.administrator.eu4you;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class CountryViewHolder {
    private TextView name = null;
    private ImageView flag = null;

    CountryViewHolder(View row) {
        name = (TextView) row.findViewById(R.id.name);
        flag = (ImageView) row.findViewById(R.id.flag);
    }

    public TextView getName() {
        return name;
    }

    public ImageView getFlag() {
        return flag;
    }

    void populateFrom(Country nation) {
        getName().setText(nation.name);
        getFlag().setImageResource(nation.flag);
    }
}
