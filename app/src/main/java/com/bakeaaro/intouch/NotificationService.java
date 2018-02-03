package com.bakeaaro.intouch;


import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

public class NotificationService extends IntentService {

    private static final String TAG = "NotificationService";

    private PusherOptions mOptions;
    private Pusher mPusher;
    private Channel mChannel;

    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public NotificationService() {
        super(TAG);

        // create connection to Pusher
        mOptions = new PusherOptions();
        mOptions.setCluster("us2");
        mPusher = new Pusher("6fd53017f3f4e0fb720f", mOptions);
        mPusher.connect();

        // subscribe to public channel
        mChannel = mPusher.subscribe("my-channel");

        // listen for events

        mChannel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                Resources resources = getResources();
                Intent mainIntent = MainActivity.newIntent(getApplicationContext());
                mainIntent.putExtra("channel", channelName);
                mainIntent.putExtra("event", eventName);
                mainIntent.putExtra("data", data);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                Notification notification = new Notification.Builder(getApplicationContext())
                        .setTicker("Abilitree")
                        .setSmallIcon(R.drawable.ic_message_black_24dp)
                        .setContentTitle("Notification")
                        .setContentText("Tap to view notification")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                notificationManagerCompat.notify(0, notification);

                Log.i(TAG, channelName + " " + eventName + " " + data);
            }
        });

    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        if (!isNetworkAvailableAndConnected()) {
//            return;
//        }
//
//        Log.i(TAG, "Received an intent: " + intent);
//
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {}
//
//        Resources resources = getResources();
//        Intent mainIntent = MainActivity.newIntent(this);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);
//
//        Notification notification = new Notification.Builder(this)
//                .setTicker("Abilitree")
//                .setSmallIcon(R.drawable.ic_message_black_24dp)
//                .setContentTitle("Dance Party")
//                .setContentText("Everyone report to the dance floor!")
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .build();
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(0, notification);

    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        boolean isNetConnected = isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();

        return isNetConnected;
    }

}
