package com.abilitree.intouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.List;

public class NotificationPagerActivity extends AppCompatActivity {

    private static final String EXTRA_NOTIFICATION_ID = "notification_id";
    private static final String EXTRA_FRAGMENT_TYPE = "type";
    private String FRAGMENT_TYPE;

    private ViewPager mViewPager;
    private List<Notification> mNotifications;

    public static Intent newIntent(Context packageContext, int notificationId, String type) {
        Intent intent = new Intent(packageContext, NotificationPagerActivity.class);
        // put string extra representing received or sent list
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        intent.putExtra(EXTRA_FRAGMENT_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_pager);

        mViewPager = (ViewPager) findViewById(R.id.notification_view_pager);
        int notificationId = (int) getIntent().getSerializableExtra(EXTRA_NOTIFICATION_ID);
        FRAGMENT_TYPE = (String) getIntent().getSerializableExtra(EXTRA_FRAGMENT_TYPE);

        mNotifications = MailBox.getInstance(this).getNotifications(FRAGMENT_TYPE);
        FragmentManager fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                super.setPrimaryItem(container, position, object);
                Notification notification = mNotifications.get(position);
                notification.setViewed();
            }

            @Override
            public Fragment getItem(int position) {
                Notification notification = mNotifications.get(position);
                return DisplaySingleNotificationFragment.newInstance(notification.getDbId(), FRAGMENT_TYPE);

            }

            @Override
            public int getCount() {
                return mNotifications.size();
            }
        });

        for (int i = 0; i < mNotifications.size(); i++) {
            if (mNotifications.get(i).getDbId() == notificationId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
