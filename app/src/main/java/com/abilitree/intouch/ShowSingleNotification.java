package com.abilitree.intouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowSingleNotification extends AppCompatActivity {

    private static final String EXTRA_NOTIFICATION_TITLE = "com.abilitree.intouch.noteTitle";
    private static final String EXTRA_NOTIFICATION_DATE = "com.abilitree.intouch.noteDate";
    private static final String EXTRA_NOTIFICATION_FROM = "com.abilitree.intouch.noteFrom";
    private static final String EXTRA_NOTIFICATION_FROM_USERNAME = "com.abilitree.intouch.noteFromUsername";
    private static final String EXTRA_NOTIFICATION_BODY = "com.abilitree.intouch.noteBody";

    //Sending intent back to DisplayNotificationsFragment for changing letter from open to closed
    private static final String EXTRA_NOTIFICATION_READ = "com.abilitree.intouch.noteRead";


    private String mNoteTitle;
    private String mNoteDate;
    private String mNoteFrom;
    private String mNoteBody;

    private TextView mNotificationTitle;
    private TextView mNotificationDate;
    private TextView mNotificationFrom;
    private TextView mNotificationBody;



    public static Intent newIntent(Context packageContext, String noteTitle, String noteDate, String noteFrom, String noteBody, String noteFromUsername) {
        Intent intent = new Intent(packageContext, ShowSingleNotification.class);
        intent.putExtra(EXTRA_NOTIFICATION_TITLE, noteTitle);
        intent.putExtra(EXTRA_NOTIFICATION_DATE, noteDate);
        intent.putExtra(EXTRA_NOTIFICATION_FROM, noteFrom);
        intent.putExtra(EXTRA_NOTIFICATION_BODY, noteBody);
        intent.putExtra(EXTRA_NOTIFICATION_FROM_USERNAME, noteFromUsername);
        return intent;
    }

    public static boolean wasLetterRead(Intent result) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_notification);

    mNoteTitle = getIntent().getStringExtra(EXTRA_NOTIFICATION_TITLE);
    mNoteDate = getIntent().getStringExtra(EXTRA_NOTIFICATION_DATE);
    mNoteFrom = getIntent().getStringExtra(EXTRA_NOTIFICATION_FROM);
    mNoteBody = getIntent().getStringExtra(EXTRA_NOTIFICATION_BODY);


    mNotificationTitle = (TextView) findViewById(R.id.title_t);
    mNotificationDate = (TextView) findViewById(R.id.date_t);
    mNotificationFrom = (TextView) findViewById(R.id.from_t);
    mNotificationBody = (TextView) findViewById(R.id.message_t);

    mNotificationTitle.setText(mNoteTitle);
    mNotificationDate.setText(mNoteDate);
    mNotificationFrom.setText(mNoteFrom);
    mNotificationBody.setText(mNoteBody);

    setLetterOpened(true);
    }

    private void setLetterOpened(boolean isLetterRead) {
        Intent data = new Intent();
        data.putExtra(EXTRA_NOTIFICATION_READ, isLetterRead);
        setResult(RESULT_OK, data);
    }
}
