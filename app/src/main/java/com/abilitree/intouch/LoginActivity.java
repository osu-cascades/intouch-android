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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;

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
                                if (response.contains("usertype")) {
                                    String usertype = response.substring(9);
                                    Log.i(TAG, String.format("Usertype: %s", usertype));
                                    Settings.setLoginSettings(getApplicationContext(), mUsername, mPassword, usertype);
                                    boolean isLoggedIn = Settings.getLoginStatus(getApplicationContext());
                                    Log.i(TAG, String.format("Login status: %b", isLoggedIn));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getApplicationContext(), TabViewActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    final String failResponse = response;
                                    Log.i(TAG, String.format("Error response: %s", response));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast toast= Toast.makeText(getApplicationContext(), failResponse, Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    });
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG, "Request error: %s", error);
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
}
