package com.bakeaaro.intouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class NotificationPagerActivity extends AppCompatActivity {

    private static final String EXTRA_NOTIFICATION_ID = "notification_id";

    private ViewPager mViewPager;
    private List<Notification> mNotifications;

    public static Intent newIntent(Context packageContext, int notificationId) {
        Intent intent = new Intent(packageContext, NotificationPagerActivity.class);
        // put string extra representing received or sent list
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_pager);

        mViewPager = (ViewPager) findViewById(R.id.notification_view_pager);
        int notificationId = (int) getIntent().getSerializableExtra(EXTRA_NOTIFICATION_ID);
        // get serializable string for type
        mNotifications = MailBox.getInstance(this).getNotifications("received");
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Notification notification = mNotifications.get(position);
                return DisplaySingleNotificationFragment.newInstance(notification.getDbId());

            }

            @Override
            public int getCount() {
                return mNotifications.size();
            }
        });

        for (int i = 0; i < mNotifications.size(); i++) {
            if (mNotifications.get(i).getDbId() == (notificationId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
