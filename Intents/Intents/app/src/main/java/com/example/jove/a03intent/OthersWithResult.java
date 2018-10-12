package com.example.jove.a03intent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class OthersWithResult extends AppCompatActivity {
    private final int PICK_CONTACT_REQUEST = 1;

    private Uri contact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_with_result);
    }

    public void onPickContact(View view) {
        Intent pickContent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.v("Pick Contact", "get result");
                contact = data.getData();
                if (getPermission(Manifest.permission.READ_CONTACTS)) {
                    Log.v("Pick Contact", "already get permission of READ_CONTACTS, display");
                    displayContact();
                } else {
                    Log.v("Pick Contact", "try to get permission of READ_CONTACTS");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission Result", "Permission is granted for " + permissions[0]);
                displayContact();
            } else {
                Log.v("Permission Result", "Permission is not granted");
            }
        }
    }

    private void displayContact() {
        if (contact == null) {
            return;
        }
        Cursor cursor = null;
        try {
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
            cursor = getContentResolver().query(contact, projection, null, null, null);
        } catch (SecurityException e) {
            Toast.makeText(this, e.getClass().getName(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                display("Phone Number: " + number);
            }
            cursor.close();
        }
    }

    private void display(String text) {
        TextView textView = (TextView) findViewById(R.id.others_with_result_display);
        textView.setText(text);
    }

    private boolean getPermission(String permission) {
        switch (permission) {
            case Manifest.permission.READ_CONTACTS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{permission}, PICK_CONTACT_REQUEST);
                        return false;
                    } else {
                        Log.v("Get Permission", "already get permission of " + permission);
                        return true;
                    }
                } else {
                    Log.v("Get Permission", "current version no need to get permission of " + permission);
                    return true;
                }
            default:
                Toast.makeText(this, "No Support for permission: " + permission, Toast.LENGTH_LONG).show();
                return false;
        }
    }
}
