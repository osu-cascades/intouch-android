package com.bakeaaro.intouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DisplaySingleNotificationActivity extends AppCompatActivity {

    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    public static Intent newIntent(Context packageContext, int notificationId) {
        Intent intent = new Intent(packageContext, DisplaySingleNotificationActivity.class);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_single_notification);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_main_fragment_container);
        if (fragment == null) {
            fragment = new DisplaySingleNotificationFragment();
            fm.beginTransaction()
                    .add(R.id.single_notification_fragment_container, fragment)
                    .commit();
        }
    }
}
