package com.abilitree.intouch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FireTree-Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean loggedIn = Settings.getLoginStatus(getApplicationContext());
        Log.i(TAG, String.format("logged in: %b", loggedIn));

        if (!loggedIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            if (getIntent().getExtras() != null ) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();

                String title = bundle.get("title").toString();
                String from = bundle.get("sender").toString();
                String datetime = bundle.get("datetime").toString();
                String body = bundle.get("body").toString();

                Log.i(TAG, "Title:  " + title);
                Log.i(TAG, "From:  " + from);
                Log.i(TAG, "Datetime:  " + datetime);
                Log.i(TAG, "Body:  " + body);

                if (title != null && from != null && datetime != null && body != null) {
                    MailBox mailBox = MailBox.getInstance(this);
                    mailBox.createNotification(title, from, datetime, body);
                }
            }

            Intent intent = new Intent(this, TabViewActivity.class);
            startActivity(intent);
        }
    }
}
