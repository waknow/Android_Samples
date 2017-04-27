package com.example.administrator.onbattery;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by Administrator on 2017/4/27.
 */

public class BatteryFragment extends Fragment {
    private ProgressBar bar = null;
    private TextView level = null;
    private ImageView status = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.batt, container, false);
        bar = (ProgressBar) result.findViewById(R.id.bar);
        level = (TextView) result.findViewById(R.id.level);
        status = (ImageView) result.findViewById(R.id.status);
        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(onBattery, filter);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(onBattery);
        super.onStop();
    }

    BroadcastReceiver onBattery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pct = 100 * intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 1)
                    / intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            bar.setProgress(pct);
            level.setText(String.format(Locale.US, "%d%%", pct));

            switch (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    status.setImageResource(R.drawable.charging);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                    if (plugged == BatteryManager.BATTERY_PLUGGED_AC
                            || plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                        status.setImageResource(R.drawable.full);
                    } else {
                        status.setImageResource(R.drawable.unplugged);
                    }
                    break;
                default:
                    status.setImageResource(R.drawable.unplugged);
                    break;
            }
        }
    };
}
