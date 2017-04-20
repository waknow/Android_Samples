package com.example.administrator.editorfragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Environment;
import android.support.v13.app.FragmentPagerAdapter;

import java.io.File;

/**
 * Created by Administrator on 2017/4/20.
 */

public class SampleAdapter extends FragmentPagerAdapter {
    private static final int[] TITLES = new int[]{R.string.internal, R.string.external, R.string.pub};
    private static final int TAB_INTERNAL = 0;
    private static final int TAB_EXTERNAL = 1;
    private static final String FILENAME = "test.txt";
    private final Context ctx;

    public SampleAdapter(Context ctx, FragmentManager mgr) {
        super(mgr);
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        File fileToEdit;
        switch (position) {
            case TAB_INTERNAL:
                fileToEdit = new File(ctx.getFilesDir(), FILENAME);
                break;
            case TAB_EXTERNAL:
                fileToEdit = new File(ctx.getExternalFilesDir(null), FILENAME);
                break;
            default:
                fileToEdit = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILENAME);
        }
        return EditorFragment.newInstance(fileToEdit);
    }

    @Override
    public String getPageTitle(int position) {
        return ctx.getString(TITLES[position]);
    }
}
