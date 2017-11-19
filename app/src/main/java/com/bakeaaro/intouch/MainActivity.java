package com.bakeaaro.intouch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String receivedMessagesFragmentTag = "rmf";
    private static final String sentMessagesFragmentTag = "smf";
    private static final String createMessageFragmentTag = "dmf";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_received:
                    fragment = fm.findFragmentByTag(receivedMessagesFragmentTag);
                    if (fragment == null) {
                        fragment = receivedMessagesFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, receivedMessagesFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, receivedMessagesFragmentTag).addToBackStack(null).commit();
                    return true;

                    case R.id.navigation_sent:
                    fragment = fm.findFragmentByTag(sentMessagesFragmentTag);
                    if (fragment == null) {
                        fragment = sentMessagesFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, sentMessagesFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, sentMessagesFragmentTag).addToBackStack(null).commit();
                    return true;

                    case R.id.navigation_create:
                    fragment = fm.findFragmentByTag(createMessageFragmentTag);
                    if (fragment == null) {
                        fragment = createMessageFragment();
                        fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, createMessageFragmentTag).commit();
                    }
                    fm.beginTransaction().replace(R.id.activity_main_fragment_container, fragment, createMessageFragmentTag).addToBackStack(null).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_main_fragment_container);
        if (fragment == null) {
            fragment = receivedMessagesFragment();
            fm.beginTransaction().add(R.id.activity_main_fragment_container, fragment, receivedMessagesFragmentTag).commit();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    protected Fragment receivedMessagesFragment() {

        return new DisplayNotificationsFragment();
    }

    protected Fragment sentMessagesFragment() {
        return new SentNotificationsFragment();
    }

    protected Fragment createMessageFragment() {

        return new CreateNotificationFragment();
    }

}
