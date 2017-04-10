package com.commonsware.astatic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jove on 2017/4/10.
 */

public class ContentFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.mainfrag, container, false);
        result.findViewById(R.id.showOther).setOnClickListener(this);
        return result;
    }

    @Override
    public void onClick(View v) {
        ((StaticFragmentDemoActivity) getActivity()).showOther(v);
    }
}
