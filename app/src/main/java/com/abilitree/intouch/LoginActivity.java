package com.abilitree.intouch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;
    private JSONArray mNotifications;
    private MailBox mailBox = MailBox.getInstance(this);

    private String mUsername = "";
    private String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEt = findViewById(R.id.username_et);
        mPasswordEt = findViewById(R.id.password_et);
        mLoginBtn = findViewById(R.id.login_btn);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mUsernameEt.getText().toString().trim();
                mPassword = mPasswordEt.getText().toString().trim();

                if (mUsername.matches("") || mPassword.matches("")) {
                    Toast toast= Toast.makeText(getApplicationContext(), "Username and password must not be blank", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = BuildConfig.AUTH_URL_STR;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonObj = new JSONObject(response);

                                    if (jsonObj.has("error")) {
                                        Log.i(TAG, "Error response: " + jsonObj);
                                        try {
                                            final String error = jsonObj.getString("error");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast toast= Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                }
                                            });
                                        } catch (JSONException e) {
                                            Log.i(TAG, "JSON exception: " + e);
                                        }
                                    } else {
                                        String usertype = jsonObj.getString("usertype");
                                        JSONArray notifications = jsonObj.getJSONArray("notifications");
                                        mNotifications = notifications;
                                        createNotifications(notifications);
                                        Settings.setLoginSettings(getApplicationContext(), mUsername, mPassword, usertype);
                                        boolean isLoggedIn = Settings.getLoginStatus(getApplicationContext());
                                        Log.i(TAG, String.format("Login status: %b", isLoggedIn));

                                        createNotifications(mNotifications);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(getApplicationContext(), TabViewActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    Log.i(TAG, "JSON exception: " + e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, String.format("Request error: %s", error));
                                Toast toast= Toast.makeText(getApplicationContext(),
                                        "Request error: " + error, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 30);
                                toast.show();
                            }
                        }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("username", mUsername);
                            params.put("password", mPassword);
                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }

    public void createNotifications(JSONArray notifications) {
        MailBox mailBox = MailBox.getInstance(this);

        for (int i = 0; i < notifications.length(); i++) {
            try {
                JSONObject notification = notifications.getJSONObject(i);
                String groupRecipients = notification.optString("group_recipients", null);
                String updatedGroups = groupRecipients.replace("[", "").replace("]", "").replace(", ", "").replace("\"", "");
                mailBox.createNotification(
                        notification.optString("title", null),
                        notification.optString("from", null),
                        notification.optString("created_at", null),
                        notification.optString("content", null),
                        notification.optString("from_username", null),
                        updatedGroups
                );
                Log.i(TAG, "notification: " + notifications.getJSONObject(i));

            } catch (JSONException e) {
                Log.i(TAG, "JSON exception: " + e);
            }
        }
    }
}
