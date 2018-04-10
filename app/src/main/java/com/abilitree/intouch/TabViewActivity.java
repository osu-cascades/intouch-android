package com.abilitree.intouch;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.iid.FirebaseInstanceId;
import com.pusher.pushnotifications.PushNotifications;

public class TabViewActivity extends AppCompatActivity {

    private static final String viewNotificationsFragmentTag = "viewNotifications";
    private static final String createNotificationFragmentTag = "createNotification";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment;
            //TODO refactor each case into method(string fragtype, callback);
            switch (item.getItemId()) {
                case R.id.navigation_view:
                    fragment = fm.findFragmentByTag(viewNotificationsFragmentTag);
                    if (fragment == null) {
                        fragment = DisplayNotificationsFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, viewNotificationsFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, viewNotificationsFragmentTag).commit();
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

    //on create view
    //This is for FCM
        PushNotifications.start(getApplicationContext(), "9313976c-3ca4-4a1c-9538-1627280923f4");
    //PushNotifications.subscribe("abilitree");
        PushNotifications.subscribe("test_abilitree");

    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("TabViewActivity", "Token: " + refreshedToken);

    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.activity_main_fragment_container);
        if (fragment == null) {
        fragment = DisplayNotificationsFragment();
        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, viewNotificationsFragmentTag).commit();
    }

    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    protected Fragment DisplayNotificationsFragment() {

        return new DisplayNotificationsFragment();
    }

    protected Fragment CreateNotificationFragment() {

        return new CreateNotificationFragment();
    }

}
