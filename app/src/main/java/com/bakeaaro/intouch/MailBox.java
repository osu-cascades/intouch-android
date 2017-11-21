package com.bakeaaro.intouch;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MailBox {

    private static MailBox sMailBox;
    private List<Notification> mReceivedNotifications;
    private List<Notification> mSentNotifications;

    public static MailBox get(Context context) {
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
            notification.setDbId(new Random().nextInt());
            notification.setTitle(String.format("Notification #%d", notification.getDbId()));
            notification.setDateCreated("right now");
            notification.setAuthor((new Random().nextInt() % 2 == 0) ? false : true);
            notification.setFrom("Someone");
            notification.setMessageBody("RAGE: Sing, Goddess, Achilles' rage, Black and murderous, " +
                    "that cost the Greeks Incalculable pain, pitched countless souls " +
                    "Of heroes into Hades' dark, And left their bodies to rot as feasts " +
                    "For dogs and birds, as Zeus' will was done.");
            if (notification.isAuthor())
                mSentNotifications.add(notification);
            else
                mReceivedNotifications.add(notification);
        }
    }

    public List<Notification> getReceivedNotifications() {
        return mReceivedNotifications;
    }

    public Notification getReceivedNotification(int id) {
        for (Notification notification : mReceivedNotifications) {
            if (notification.getDbId() == id) {
                return notification;
            }
        }
        return null;
    }

    public List<Notification> getSentNotifications() {
        return mSentNotifications;
    }

    public Notification getSentNotification(int id) {
        for (Notification notification : mSentNotifications) {
            if (notification.getDbId() == id) {
                return notification;
            }
        }
        return null;
    }

}
