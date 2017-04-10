package com.commonsware.astatic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by jove on 2017/4/6.
 */

public class LifecycleLoggingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(),"onCreate()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(getClass().getSimpleName(),"onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(),"onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(),"onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getClass().getSimpleName(),"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(),"onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(),"onDestroy()");
    }
}
