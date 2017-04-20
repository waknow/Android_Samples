package com.example.administrator.fileeditor;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Abstract Permission Activity
 * Created by Administrator on 2017/4/19.
 */

public abstract class AbstractPermissionActivity extends Activity {
    abstract protected String[] getDesiredPermission();

    abstract protected void onPermissionDenied();

    abstract protected void onReady(Bundle state);

    private static final int REQUESTE_PERMISSION = 61125;
    private static final String STATE_IN_PERMISSION = "inPermission";
    private boolean isInPermission = false;
    private Bundle state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.state = savedInstanceState;

        if (state != null) {
            isInPermission = state.getBoolean(STATE_IN_PERMISSION, false);
        }

        if (hasAllPermissions(getDesiredPermission())) {
            onReady(state);
        } else if (!isInPermission) {
            ActivityCompat.requestPermissions(this, netPermission(getDesiredPermission()), REQUESTE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUESTE_PERMISSION:
                if (hasAllPermissions(getDesiredPermission())) {
                    onReady(state);
                } else {
                    onPermissionDenied();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_IN_PERMISSION, isInPermission);
    }

    protected boolean hasAllPermissions(String[] perms) {
        for (String perm : perms) {
            if (!hasPermission(perm)) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasPermission(String perm) {
        return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
    }

    protected String[] netPermission(String[] wanted) {
        ArrayList<String> result = new ArrayList<>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result.toArray(new String[result.size()]);
    }
}
