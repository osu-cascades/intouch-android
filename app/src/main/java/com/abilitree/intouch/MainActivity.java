package com.abilitree.intouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.pusher.pushnotifications.PushNotifications;

public class MainActivity extends AppCompatActivity {

    private static final String receivedNotificationsFragmentTag = "receivedNotifications";
    private static final String sentNotificationsFragmentTag = "sentNotifications";
    private static final String createNotificationFragmentTag = "createNotification";
    private static final String optionsFragmentTag = "options";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment;
            //TODO refactor each case into method(string fragtype, callback);
            switch (item.getItemId()) {
                case R.id.navigation_received:
                    fragment = fm.findFragmentByTag(receivedNotificationsFragmentTag);
                    if (fragment == null) {
                        fragment = DisplayNotificationsFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, receivedNotificationsFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, receivedNotificationsFragmentTag).commit();
                    return true;

                case R.id.navigation_sent:
                    fragment = fm.findFragmentByTag(sentNotificationsFragmentTag);
                    if (fragment == null) {
                        fragment = DisplayNotificationsFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, sentNotificationsFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, sentNotificationsFragmentTag).commit();
                    return true;

                case R.id.navigation_create:
                    fragment = fm.findFragmentByTag(createNotificationFragmentTag);
                    if (fragment == null) {
                        fragment = CreateNotificationFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, createNotificationFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, createNotificationFragmentTag).commit();
                    return true;

            }
            return false;
        }
    };

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This is for FCM
        PushNotifications.start(getApplicationContext(), "420e9921-9c12-47ed-a62c-d4de75534b91");
        PushNotifications.subscribe("hello");

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_main_fragment_container);
        if (fragment == null) {
            fragment = DisplayNotificationsFragment();
            fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, receivedNotificationsFragmentTag).commit();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected Fragment DisplayNotificationsFragment() {

        return new DisplayNotificationsFragment();
    }

    protected Fragment CreateNotificationFragment() {

        return new CreateNotificationFragment();
    }

//    protected Fragment OptionsFragment() {
//
//        return new OptionsFragment();
//    }

}
