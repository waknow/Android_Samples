package com.example.administrator.bundle;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

public class RotationBundleDemoActivity extends Activity implements View.OnClickListener {
    static final int PICK_REQUEST = 1337;
    Uri contact = null;
    Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        viewButton = (Button) findViewById(R.id.view);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (contact != null) {
            outState.putParcelable("contact", contact);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contact = savedInstanceState.getParcelable("contact");
        viewButton.setEnabled(contact != null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                contact = data.getData();
                viewButton.setEnabled(true);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void pickContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_REQUEST);
    }

    private void viewContact() {
        startActivity(new Intent(Intent.ACTION_VIEW, contact));
    }
}
