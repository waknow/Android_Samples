package com.jove.demo.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements AMapLocationListener {
    private AMapLocationClient mLocationClient = null;
    public static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String PrefOnce;
    private String PrefOnceRelocate;
    private String PrefOnceLatest;
    private String PrefInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefOnce = getString(R.string.pref_once);
        PrefOnceRelocate = getString(R.string.pref_once_relocate);
        PrefOnceLatest = getString(R.string.pref_once_latest);
        PrefInterval = getString(R.string.pref_interval);

        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                startActivity(new Intent(this, PreferenceActivity.class));
                break;
            default:
                Toast.makeText(this, "Unknown Item", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "start location");
        updateOption();
        checkPermission();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            Log.i(TAG, "stop location");
            mLocationClient.stopLocation();
        }
    }

    public void relocate(View view) {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    private void checkPermission() {
        String[] needGrant = getPermissionNotGranted(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (needGrant != null) {
            ActivityCompat.requestPermissions(this, needGrant,
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
            Log.i(TAG, "request permission");
        } else {
            Log.i(TAG, "init location client");
            if (mLocationClient != null) {
                mLocationClient.startLocation();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String[] needGranted = getPermissionNotGranted(permissions);
        if (needGranted != null) {
            for (String permission : needGranted) {
                Log.w(TAG, "lack permission " + permission);
            }
        } else {
            Log.i(TAG, "init location client");
            if (mLocationClient != null) {
                mLocationClient.startLocation();
            }
        }
    }

    private String[] getPermissionNotGranted(String... permissions) {
        if (permissions == null) {
            return null;
        }
        List<String> notGrantedList = null;
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                if (notGrantedList == null) {
                    notGrantedList = new ArrayList<>();
                }
                notGrantedList.add(permission);
            }
        }
        if (notGrantedList == null) {
            return null;
        }
        return notGrantedList.toArray(new String[notGrantedList.size()]);
    }

    private boolean hasPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void displayLocationInformation(TextView view, AMapLocation loc) {
        if (loc == null) {
            view.setText("null");
            return;
        }
        if (loc.getErrorCode() != 0) {
            view.setText(String.format("Error:\nCode: %d\nMessage: %s", loc.getErrorCode(), loc.getErrorInfo()));
            return;
        }
        Message msg = new Message();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(loc.getTime());
        msg
                .append("Type", loc.getLocationType())
                .append("Lat", loc.getLatitude())
                .append("Lon", loc.getLongitude())
                .append("Accuracy", loc.getAccuracy())
                .append("Address", loc.getAddress())
                .append("Country", loc.getCountry())
                .append("Province", loc.getProvince())
                .append("City", loc.getCity())
                .append("District", loc.getDistrict())
                .append("Street", loc.getDistrict())
                .append("StreetNum", loc.getStreetNum())
                .append("AdCode", loc.getAdCode())
                .append("AoiName", loc.getAoiName())
                .append("BuildingId", loc.getBuildingId())
                .append("floor", loc.getFloor())
                .append("Gps Status", loc.getGpsAccuracyStatus())
                .append("date", sdf.format(date));
        view.setText(msg.toString());
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        Log.d(TAG, "onLocationChanged");
        TextView textView = (TextView) findViewById(R.id.textview);
        displayLocationInformation(textView, aMapLocation);
    }

    private void updateOption() {
        if (mLocationClient == null) {
            return;
        }
        AMapLocationClientOption option = new AMapLocationClientOption();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean once = pref.getBoolean(PrefOnce, false);
        option.setOnceLocation(once);
        if (once) {
            option.setOnceLocationLatest(pref.getBoolean(PrefOnceLatest, false));
        } else {
            option.setOnceLocation(false);
        }
        String interval = pref.getString(PrefInterval, "2");
        option.setInterval(Integer.valueOf(interval) * 1000);
        mLocationClient.setLocationOption(option);
        toggleRelocateButton(pref.getBoolean(PrefOnceRelocate, false) && once);
    }

    private void toggleRelocateButton(boolean show) {
        Button button = (Button) findViewById(R.id.relocate);
        if (show) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }
    }

    private class Message {
        private List<String> lines = new ArrayList<>();

        Message append(String key, Object value) {
            this.lines.add(String.format("%s: %s", key, value));
            return this;
        }

        @Override
        public String toString() {
            return TextUtils.join("\n", this.lines);
        }
    }

}
