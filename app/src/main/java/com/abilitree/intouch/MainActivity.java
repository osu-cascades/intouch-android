package com.abilitree.intouch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FireTree-Main";


//    public static Intent newIntent(Context context) {
//        return new Intent(context, MainActivity.class);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null ) {

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            String title = bundle.getString("title");
            String from = bundle.getString("by");
            String datetime = bundle.getString("datetime");
            String body = bundle.getString("body");

            Log.d(TAG, "Key: title, Value:  " + title);
            Log.d(TAG, "Key: by, Value:  " + from);
            Log.d(TAG, "Key: datetime, Value:  " + datetime);
            Log.d(TAG, "Key: body, Value:  " + body);



            if (title != null && from != null && datetime != null && body != null) {
                MailBox mailBox = MailBox.getInstance(this);
                mailBox.createNotification(title, from, datetime, body);
            }

        }

    }

}
