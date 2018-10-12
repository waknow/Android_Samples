package com.commonsware.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class NoteActivity extends Activity implements NoteFragment.Contract {
    public static final String EXTRA_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            int position = getIntent().getIntExtra(EXTRA_POSITION, -1);
            if (position >= 0) {
                Fragment frag = NoteFragment.newInstance(position);
                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, frag)
                        .commit();
            }
        }
    }

    @Override
    public void closeNotes() {
        finish();
    }
}
