package com.example.administrator.fragmentbundle;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/4/14.
 */

public class RotationFragment extends Fragment implements View.OnClickListener {
    static final int PICK_REQUEST = 1337;
    Uri contact = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.main, container, false);
        result.findViewById(R.id.pick).setOnClickListener(this);
        View v = result.findViewById(R.id.view);
        v.setOnClickListener(this);
        if (savedInstanceState != null) {
            contact = savedInstanceState.getParcelable("contact");
        }
        v.setEnabled(contact != null);
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pick) {
            pickContact();
        } else {
            viewContact();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                contact = data.getData();
                getView().findViewById(R.id.view).setEnabled(contact != null);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (contact != null) {
            outState.putParcelable("contact", contact);
        }
    }


    private void pickContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_REQUEST);
    }

    private void viewContact() {
        startActivity(new Intent(Intent.ACTION_VIEW, contact));
    }
}
