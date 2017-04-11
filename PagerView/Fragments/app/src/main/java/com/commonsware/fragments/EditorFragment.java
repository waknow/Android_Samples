package com.commonsware.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by jove on 2017/4/11.
 */

public class EditorFragment extends Fragment {
    private static final String KEY_POSITION = "key_position";

    static EditorFragment newInstance(int position) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.editor, container, false);
        EditText editor = (EditText) result.findViewById(R.id.editor);
        int position = getArguments().getInt(KEY_POSITION, -1);
        editor.setHint(String.format(getString(R.string.hint), position + 1));
        return result;
    }
}
