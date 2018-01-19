package com.bakeaaro.intouch;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MailBox {

    private static MailBox sMailBox;
    private List<Notification> mReceivedNotifications;
    private List<Notification> mSentNotifications;

    public static MailBox getInstance(Context context) {
        if (sMailBox == null) {
            sMailBox = new MailBox(context);
        }
        return sMailBox;
    }

    private MailBox(Context context){
        mReceivedNotifications = new ArrayList<>();
        mSentNotifications = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Notification notification = new Notification();
            notification.setDbId(i);
            notification.setTitle(String.format("Notification #%d", notification.getDbId()));
            notification.setDateCreated("1-8-18");
            if (i % 4 == 1 || i % 4 == 2)
                notification.setAuthor(true);
            notification.setFrom("Group Leader " + String.valueOf(i));
            if (notification.isAuthor())
                notification.setViewed();
            notification.setMessageBody("Sample notification text.");
            //mReceivedNotifications.add(notification);
            if (notification.isAuthor())
                mSentNotifications.add(notification);
            else
                mReceivedNotifications.add(notification);
        }
    }

    public Notification getNotification(String type, int id) {

        Notification notification = new Notification();

        switch (type) {
            case "receivedNotifications":
                for (Notification n : mReceivedNotifications) {
                    if (n.getDbId() == id) {
                        notification = n;
                        break;
                    }
                    notification = null;
                }
                break;
            case "sentNotifications":
                for (Notification n : mSentNotifications) {
                    if (n.getDbId() == id) {
                        notification = n;
                        break;
                    }
                    notification = null;
                }
                break;
            default:
                notification = null;
                break;
        }
        return notification;
    }

    public List<Notification> getNotifications(String type) {

        List<Notification> mNotifications;

        switch (type) {
            case "receivedNotifications":
                mNotifications = mReceivedNotifications;
                break;
            case "sentNotifications":
                mNotifications = mSentNotifications;
                break;
            default:
                mNotifications = new ArrayList<>();
                break;
        }

        return mNotifications;

    }

}
