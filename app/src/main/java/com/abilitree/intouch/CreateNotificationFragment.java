package com.abilitree.intouch;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class CreateNotificationFragment extends Fragment {

    private static final String TAG = "FireTree-Create";

    private Button mSendBtn;
    private EditText mTitleEt;
    private EditText mMessageEt;
    private Spinner mToSpnr;

    private String mTitle;
    private String mTo;
    private String mMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_notification_fragment, container, false);

        mTitleEt = view.findViewById(R.id.title_et);
        mToSpnr = view.findViewById(R.id.to_spnr);
        mMessageEt = view.findViewById(R.id.message_et);
        mSendBtn = view.findViewById(R.id.send_btn);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle = mTitleEt.getText().toString().trim();
                mTo = mToSpnr.getSelectedItem().toString();
                mMessage = mMessageEt.getText().toString().trim();
                if (mTitle.equals("") || mTo.equals("") || mMessage.equals("")) {
                    Log.i(TAG, "All fields must have input");
                    return;
                } else {
                    // send post
                    final Activity activity = getActivity();
                    RequestQueue queue = Volley.newRequestQueue(activity);
                    String url ="https://abilitree-intouch-staging.herokuapp.com/push";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(TAG, String.format("response: %s", response));

                                    //TODO show alert with response
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//                                            }
//                                        });

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, "Request Error");
                        }
                    }) {
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("title", mTitle);
                            params.put("group", mTo);
                            params.put("body", mMessage);
                            params.put("username", Settings.getUsername(getContext()));
                            params.put("password", Settings.getPassword(getContext()));
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

        return view;
    }

}
