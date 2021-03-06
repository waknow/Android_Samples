package com.commonsware.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by jove on 2017/4/11.
 */

public class SimpleAdapter extends FragmentPagerAdapter {
    private static final int count = 10;
    private static final EditorFragment[] cache = new EditorFragment[count];

    public SimpleAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= 0 && position < count) {
            EditorFragment fragment = cache[position];
            if (fragment == null) {
                fragment = EditorFragment.newInstance(position);
                cache[position] = fragment;
            }
            return fragment;
        }
        return EditorFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return count;
    }
}
