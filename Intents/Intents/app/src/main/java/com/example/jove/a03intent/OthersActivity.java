package com.example.jove.a03intent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class OthersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others);
    }

    public void onClickMap(View view) {
        Uri location = Uri.parse("geo:31.2781461,120.5339057?z=14");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void onClickWebPage(View view) {
        Uri webpage = Uri.parse("https://github.com/waknow");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    public void onClickEmail(View view) {
        onclickEmail(false);
    }

    public void onClickEmailChooser(View view) {
        onclickEmail(true);
    }

    private void onclickEmail(boolean userChooser) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"idot@exmaple.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/mail/attachment"));
        if (userChooser) {
            emailIntent = Intent.createChooser(emailIntent, "Open with");
        }
        startActivity(emailIntent);
    }

    public void onClickCalendar(View view) {
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.HOUR_OF_DAY, 1);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, "Kongfu");
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "China");
        startActivity(calendarIntent);
    }

    @Override
    public void startActivity(Intent intent) {
        if (!hasAvailablActivity(intent)) {
            Toast.makeText(this, "no activity for this op!", Toast.LENGTH_LONG).show();
            return;
        }
        super.startActivity(intent);
    }

    private boolean hasAvailablActivity(Intent intent) {
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return activities.size() > 0;
    }
}
