package com.jove.demo.location;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jove.zhu on 2017/6/29.
 */

public class PreferencesFrag extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = PreferencesFrag.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference == null) {
            Log.w(TAG, "found no Preference with key " + key);
            return;
        }
        Object value = null;
        if (preference instanceof ListPreference) {
            ListPreference p = (ListPreference) preference;
            value = p.getEntry();
        }
        if (preference instanceof EditTextPreference) {
            value = sharedPreferences.getInt(key, 1);
        }
        if (value != null) {
            Log.i(TAG, "set summary for " + preference.getTitle() + " with value " + value);
            if (value instanceof CharSequence) {
                preference.setSummary((CharSequence) value);
            } else {
                preference.setSummary(String.valueOf(value));
            }
        }
    }
}
