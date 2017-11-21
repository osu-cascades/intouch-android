package com.bakeaaro.intouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class DisplaySingleNotificationActivity extends AppCompatActivity {

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
