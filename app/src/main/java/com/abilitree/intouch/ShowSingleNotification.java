package com.abilitree.intouch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ShowSingleNotification extends AppCompatActivity {

    private static final String EXTRA_NOTIFICATION_TITLE = "com.abilitree.intouch.noteTitle";
    private static final String EXTRA_NOTIFICATION_DATE = "com.abilitree.intouch.noteDate";
    private static final String EXTRA_NOTIFICATION_FROM = "com.abilitree.intouch.noteFrom";
    private static final String EXTRA_NOTIFICATION_FROM_USERNAME = "com.abilitree.intouch.noteFromUsername";
    private static final String EXTRA_NOTIFICATION_BODY = "com.abilitree.intouch.noteBody";

    //Sending intent back to DisplayNotificationsFragment for changing letter from open to closed
    private static final String EXTRA_NOTIFICATION_READ = "com.abilitree.intouch.noteRead";

    private static final String REPLY_URL = "https://abilitree-intouch-staging.herokuapp.com/reply_to_sender";


    private String mNoteTitle;
    private String mNoteDate;
    private String mNoteFrom;
    private String mNoteBody;
    private String mNoteFromUsername;

    private TextView mNotificationTitle;
    private TextView mNotificationDate;
    private TextView mNotificationFrom;
    private TextView mNotificationBody;

    private EditText mMessageContent;
    private Button mReplyToSenderButton;
    private Button mSendNotificationButton;

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
        mNoteFromUsername = getIntent().getStringExtra(EXTRA_NOTIFICATION_FROM_USERNAME);


        mNotificationTitle = (TextView) findViewById(R.id.title_t);
        mNotificationDate = (TextView) findViewById(R.id.date_t);
        mNotificationFrom = (TextView) findViewById(R.id.from_t);
        mNotificationBody = (TextView) findViewById(R.id.message_t);
        mMessageContent = (EditText) findViewById(R.id.message_content);
        mMessageContent.setVisibility(View.GONE);
        mSendNotificationButton = (Button) findViewById(R.id.send_reply);
        mSendNotificationButton.setVisibility(View.GONE);

        mNotificationTitle.setText(mNoteTitle);
        mNotificationDate.setText(mNoteDate);
        mNotificationFrom.setText(mNoteFrom);
        mNotificationBody.setText(mNoteBody);

        mReplyToSenderButton = (Button) findViewById(R.id.reply_btn);
        mReplyToSenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessageContent.setVisibility(View.VISIBLE);
                mSendNotificationButton.setVisibility(View.VISIBLE);
            }
        });

        mSendNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = mMessageContent.getText().toString().trim();
                if (message.equals("")) {
                    Log.i("ShowSingleNotification", "All fields must have input");
                    return;
                } else {
                    // send post
                    final Activity activity = ShowSingleNotification.this;
                    RequestQueue queue = Volley.newRequestQueue(activity);
                    String url = REPLY_URL;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("ShowSingleNotification", String.format("response: %s", response));
                                    Toast toast= Toast.makeText(ShowSingleNotification.this,
                                            response, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 30);
                                    toast.show();
                                    if (response.contains("notification sent"))
                                        ShowSingleNotification.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mMessageContent.setText("");
                                            }
                                        });
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("ShowSingleNotification", "Request Error");
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();

                            Log.i("ShowSingleNotification", "sender: " + mNoteFromUsername);
                            Log.i("ShowSingleNotification", "body: " + message);

                            params.put("sender", mNoteFromUsername);
                            params.put("body", mMessageContent.getText().toString().trim());
                            params.put("username", Settings.getUsername(ShowSingleNotification.this));
                            params.put("password", Settings.getPassword(ShowSingleNotification.this));
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            return params;
                        }
                    };

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }
        });

        setLetterOpened(true);
    }

    private void setLetterOpened(boolean isLetterRead) {
        Intent data = new Intent();
        data.putExtra(EXTRA_NOTIFICATION_READ, isLetterRead);
        setResult(RESULT_OK, data);
    }
}
