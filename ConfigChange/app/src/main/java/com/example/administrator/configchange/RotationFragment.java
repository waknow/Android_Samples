package com.example.administrator.configchange;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/13.
 */

public class RotationFragment extends Fragment implements View.OnClickListener {
    static final int PICK_REQUEST = 1337;
    Uri contact = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View result = inflater.inflate(R.layout.main, container, false);
        result.findViewById(R.id.pick).setOnClickListener(this);
        View v = result.findViewById(R.id.view);
        v.setOnClickListener(this);
        v.setEnabled(contact != null);
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                contact = data.getData();
                //noinspection ConstantConditions
                getView().findViewById(R.id.view).setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pick) {
            pickContact(v);
        } else {
            viewContact(v);
        }
    }

    public void pickContact(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_REQUEST);
    }

    public void viewContact(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, contact));
    }
}
