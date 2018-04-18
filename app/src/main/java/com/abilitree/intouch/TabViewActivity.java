package com.abilitree.intouch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.PushNotificationReceivedListener;

import java.util.Map;


public class TabViewActivity extends AppCompatActivity {

    private static final String TAG = "FireTree-TabView";
    private static final String viewNotificationsFragmentTag = "viewNotifications";
    private static final String createNotificationFragmentTag = "createNotification";


    private BottomNavigationView mNavViewBnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);

        //This is for FCM
        PushNotifications.start(getApplicationContext(), "9313976c-3ca4-4a1c-9538-1627280923f4");
        PushNotifications.subscribe(Settings.getUsername(getApplicationContext()));
        PushNotifications.setOnMessageReceivedListener(new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                Map<String, String> messagePayload = remoteMessage.getData();
                String title = messagePayload.get("title");
                String from = messagePayload.get("by");
                String datetime = messagePayload.get("datetime");
                String body = messagePayload.get("body");
                Log.i(TAG, messagePayload.toString());
                if (messagePayload == null) {
                    // Message payload was not set for this notification
                    Log.i(TAG, "Payload was missing");
                } else {
                    // Do something interesting with your message payload!
                    Log.i(TAG, messagePayload.toString());
                    // update recycler view
                    MailBox mailBox = MailBox.getInstance(getApplicationContext());
                    mailBox.createNotification(title, from , datetime, body);
                }
            }
        });

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("TabViewActivity", "Token: " + refreshedToken);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_tab_view_fragment_container);
        if (fragment == null) {
            fragment = DisplayNotificationsFragment();
            fm.beginTransaction().add(R.id.activity_tab_view_fragment_container, fragment, viewNotificationsFragmentTag).commit();
        }

        mNavViewBnv = findViewById(R.id.navigation_bnv);
        mNavViewBnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_view:
                        fragment = fm.findFragmentByTag(viewNotificationsFragmentTag);
                        if (fragment == null) {
                            fragment = DisplayNotificationsFragment();
                            fm.beginTransaction().add(R.id.activity_tab_view_fragment_container, fragment, viewNotificationsFragmentTag).commit();
                        }
                        fm.beginTransaction().replace(R.id.activity_tab_view_fragment_container, fragment, viewNotificationsFragmentTag).commit();
                        return true;

                    case R.id.navigation_create:
                        fragment = fm.findFragmentByTag(createNotificationFragmentTag);
                        if (fragment == null) {
                            fragment = CreateNotificationFragment();
                            fm.beginTransaction().add(R.id.activity_tab_view_fragment_container, fragment, createNotificationFragmentTag).commit();
                        }
                        fm.beginTransaction().replace(R.id.activity_tab_view_fragment_container, fragment, createNotificationFragmentTag).commit();
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.logout_mi:
                //Log.d(TAG, "logout");
                Settings.clearLoginSettings(this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected Fragment DisplayNotificationsFragment() {

        return new DisplayNotificationsFragment();
    }

    protected Fragment CreateNotificationFragment() {

        return new CreateNotificationFragment();
    }

}
