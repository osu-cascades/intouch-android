package com.bakeaaro.intouch;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MailBox {

    private static MailBox sMailBox;
    private List<Notification> mNotifications;

    public static MailBox get(Context context) {
        if (sMailBox == null) {
            sMailBox = new MailBox(context);
        }
        return sMailBox;
    }

    private MailBox(Context context){
        mNotifications = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Notification notification = new Notification();
            notification.setDbId(new Random().nextInt());
            notification.setTitle(String.format("Notification #%d", notification.getDbId()));
            notification.setDateCreated("right now");
            notification.setAuthor((new Random().nextInt() % 2 == 0) ? false : true);
            notification.setMessageBody("RAGE: Sing, Goddess, Achilles' rage, Black and murderous, " +
                    "that cost the Greeks Incalculable pain, pitched countless souls " +
                    "Of heroes into Hades' dark, And left their bodies to rot as feasts " +
                    "For dogs and birds, as Zeus' will was done.");
        }
    }

    public List<Notification> getNotifications() {
        return mNotifications;
    }

    public Notification getNotification(int id) {
        for (Notification notification : mNotifications) {
            if (notification.getDbId() == id) {
                return notification;
            }
        }
        return null;
    }

}
