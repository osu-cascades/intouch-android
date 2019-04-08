package com.abilitree.intouch;

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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

public class TabViewActivity extends AppCompatActivity  {

    private static final String TAG = "TabViewActivity";
    private static final String viewNotificationsFragmentTag = "viewNotifications";
    private static final String createNotificationFragmentTag = "createNotification";
    private static final String calendarFragmentTag = "calendar";

    private BottomNavigationView mNavViewBnv;
    public UpdateFragmentRecyclerView mUpdateFragmentRecyclerView;

    public interface UpdateFragmentRecyclerView {
        public void updateView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);
        
        PushNotifications.start(getApplicationContext(), BuildConfig.PUSHER_INSTANCE_ID_STR);
        PushNotifications.subscribe(Settings.getUsername(getApplicationContext()));

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_tab_view_fragment_container);

        if (fragment == null) {
            fragment = DisplayNotificationsFragment();
            fm.beginTransaction().add(R.id.activity_tab_view_fragment_container, fragment, viewNotificationsFragmentTag).commit();
        }

        Fragment frag = getSupportFragmentManager().findFragmentByTag(viewNotificationsFragmentTag);

        if (frag instanceof DisplayNotificationsFragment) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mUpdateFragmentRecyclerView.updateView();
                }
            });
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
                    case R.id.navigation_calendar:
                        fragment = fm.findFragmentByTag(calendarFragmentTag);
                        if (fragment == null) {
                            fragment = CalendarFragment();
                            fm.beginTransaction().add(R.id.activity_tab_view_fragment_container, fragment, calendarFragmentTag).commit();
                        }
                        fm.beginTransaction().replace(R.id.activity_tab_view_fragment_container, fragment, calendarFragmentTag).commit();
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushNotifications.setOnMessageReceivedListener(new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                Log.i(TAG, "body: " + remoteMessage.getNotification().getBody());
                Log.i(TAG, "from: " + remoteMessage.getData().get("sender"));
                Log.i(TAG, "title: " + remoteMessage.getNotification().getTitle());
                Log.i(TAG, "datetime: " + remoteMessage.getData().get("datetime"));
                Log.i(TAG, "from username: " + remoteMessage.getData().get("from_username"));
                Log.i(TAG, "group string: " + remoteMessage.getData().get("group_recipients"));

                String group = remoteMessage.getData().get("group_recipients");
                String updatedGroups = group.replace("[", "").replace("]", "").replace(", ", "").replace("\"", "");

                MailBox mailBox = MailBox.getInstance(getApplicationContext());

                mailBox.createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getData().get("sender"), remoteMessage.getData().get("datetime"), remoteMessage.getNotification().getBody(), remoteMessage.getData().get("from_username"), updatedGroups);

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(viewNotificationsFragmentTag);

                if (fragment instanceof DisplayNotificationsFragment) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mUpdateFragmentRecyclerView.updateView();
                        }
                    });
                }
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
        MailBox mailBox = MailBox.getInstance(getApplicationContext());

        switch(item.getItemId()) {
            case R.id.logout_mi:
                mailBox.deleteAllNotifications();
                Settings.clearLoginSettings(this);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.delete_all_mi:
                mailBox.deleteAllNotifications();
                mUpdateFragmentRecyclerView.updateView();
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

    protected Fragment CalendarFragment() {
        return new CalendarFragment();
    }
}
