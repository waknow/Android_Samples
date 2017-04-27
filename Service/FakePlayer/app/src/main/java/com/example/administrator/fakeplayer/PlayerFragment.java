package com.example.administrator.fakeplayer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/27.
 */

public class PlayerFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.main, container, false);

        result.findViewById(R.id.start).setOnClickListener(this);
        result.findViewById(R.id.stop).setOnClickListener(this);

        return result;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PlayerService.class);
        switch (v.getId()) {
            case R.id.start:
                Log.d(getClass().getSimpleName(), "click start");
                intent.putExtra(PlayerService.EXTRA_PLAYLIST, "main");
                intent.putExtra(PlayerService.EXTRA_SHUFFLE, true);
                getActivity().startService(intent);
                break;
            case R.id.stop:
                Log.d(getClass().getSimpleName(), "click stop");
                getActivity().stopService(intent);
                break;
        }
    }
}
