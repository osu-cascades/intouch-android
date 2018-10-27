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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.HashMap;

public class TabViewActivity extends AppCompatActivity  {

    public interface UpdateFragmentRecyclerView{
        public void updateView();
    }

    private static final String TAG = "FireTree-TabView";
    private static final String viewNotificationsFragmentTag = "viewNotifications";
    private static final String createNotificationFragmentTag = "createNotification";

    private BottomNavigationView mNavViewBnv;
    public UpdateFragmentRecyclerView mUpdateFragmentRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);

        //This is for FCM

        PusherOptions options = new PusherOptions();
        options.setCluster("us2");
        Pusher pusher = new Pusher("9d82b24b0c3b8eaf2b9f", options);

        Channel channel = pusher.subscribe(Settings.getUsername(getApplicationContext()));

        channel.bind("new-notification", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                if (data == null) {
                    Log.i(TAG, "Missing data");
                } else {
                    MailBox mailBox = MailBox.getInstance(getApplicationContext());

                    HashMap<String,String> notification = new Gson().fromJson(data, new TypeToken<HashMap<String, String>>(){}.getType());

                    mailBox.createNotification(notification.get("title"), notification.get("datetime") , notification.get("from"), notification.get("body"));

                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(viewNotificationsFragmentTag);
                    if (fragment instanceof DisplayNotificationsFragment)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mUpdateFragmentRecyclerView.updateView();
                            }
                        });

                }
            }
        });

        pusher.connect();

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
