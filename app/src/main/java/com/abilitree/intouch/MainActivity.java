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

                Object title_object = bundle.get("title");
                Object from_object = bundle.get("sender");
                Object datetime_object = bundle.get("datetime");
                Object body_object = bundle.get("body");
                Object from_username_object = bundle.get("from_username");
                Object group_recipients_object = bundle.get("group_recipients");

                if(title_object != null && from_object != null && datetime_object != null && body_object != null && from_username_object != null && group_recipients_object != null) {
                    String title = title_object.toString();
                    String from = from_object.toString();
                    String datetime = datetime_object.toString();
                    String body = body_object.toString();
                    String from_username = from_username_object.toString();
                    String group_recipients = group_recipients_object.toString();

                    String updatedGroups = group_recipients.replace("[", "").replace("]", "").replace(", ", "").replace("\"", "");

                    if (title != null && from != null && datetime != null && body != null && from_username != null && updatedGroups != null) {
                        MailBox mailBox = MailBox.getInstance(this);
                        mailBox.createNotification(title, from, datetime, body, from_username, updatedGroups);
                    }
                }
            }

            Intent intent = new Intent(this, TabViewActivity.class);
            startActivity(intent);
        }
    }
}
