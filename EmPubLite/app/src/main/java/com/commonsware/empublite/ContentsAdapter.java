package com.commonsware.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Created by jove on 2017/4/11.
 */

public class ContentsAdapter extends FragmentStatePagerAdapter {
    final BookContents contents;

    public ContentsAdapter(Activity ctx, BookContents contents) {
        super(ctx.getFragmentManager());
        this.contents = contents;
    }

    @Override
    public Fragment getItem(int position) {
        return SimpleContentFragment.newInstance(contents.getChapterPath(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return contents.getChapterTitle(position);
    }

    @Override
    public int getCount() {
        return contents.getChapterCount();
    }
}
