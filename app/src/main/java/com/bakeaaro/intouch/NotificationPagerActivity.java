package com.bakeaaro.intouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class NotificationPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Notification> mNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification_pager);

        mViewPager = (ViewPager) findViewById(R.id.notification_view_pager);

        mNotifications = MailBox.getInstance(this).getNotifications("received");
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Notification notification = mNotifications.get(position);
                DisplaySingleNotificationFragment.newInstance(notification.getDbId());
                return null;
            }

            @Override
            public int getCount() {
                return mNotifications.size();
            }
        });

    }
}
