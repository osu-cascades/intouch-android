package com.abilitree.intouch;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MailBox {

    String TAG = "FireTree-MailBox";

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
    }

    public Notification getNotification(String type, int id) {

        Notification notification = null;

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

    public void createNotification(String title, String from, String datetime, String body) {

        for (Notification notification : mReceivedNotifications) {
            String titleStr = notification.getTitle();
            String fromStr = notification.getFrom();
            String datetimeStr = notification.getDateCreated();
            String bodyStr = notification.getMessageBody();
            Log.d(TAG, title); Log.d(TAG, notification.getTitle());
            Log.d(TAG, from); Log.d(TAG, notification.getFrom());
            Log.d(TAG, datetime); Log.d(TAG, notification.getDateCreated());
            Log.d(TAG, body); Log.d(TAG, notification.getMessageBody());
            if (titleStr == null)
                continue;
            if(titleStr.equals(title) && fromStr.equals(from) && datetimeStr.equals(datetime) && bodyStr.equals(body))
                return;

        }
        Notification notification = new Notification(title, from, datetime, body);
        mReceivedNotifications.add(0, notification);
    }

}
