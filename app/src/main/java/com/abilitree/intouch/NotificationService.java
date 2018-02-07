package com.abilitree.intouch;


import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.gson.Gson;
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
        mChannel = mPusher.subscribe("abilitree");

        // listen for events
        mChannel.bind("notifications", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {

                // TODO add notification to mailbox

                // de-jsonify
                String json = data;
                Gson gson = new Gson();
                Note note = gson.fromJson(json, Note.class);
                Log.i(TAG, note.message);

                Resources resources = getResources();
                Intent mainAppIntent = MainActivity.newIntent(getApplicationContext());
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, mainAppIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                //if (appOpen) {
                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setTicker("Abilitree")
                            .setSmallIcon(R.drawable.ic_message_green_24dp)
                            .setContentTitle("Notification")
                            .setContentText(note.message)
                            .setContentIntent(null)
                            .setAutoCancel(true)
                            .build();
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                    notificationManagerCompat.notify(0, notification);
                //}
                Log.i(TAG, channelName + " " + eventName + " " + data);
            }
        });

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        boolean isNetConnected = isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();

        return isNetConnected;
    }

}
