package com.abilitree.intouch;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateNotificationFragment extends Fragment {

    private static final String TAG = "FireTree-Create";
    private static final String URL = "https://abilitree-intouch-staging.herokuapp.com/get_groups";

    private Button mSendBtn;
    private EditText mTitleEt;
    private EditText mMessageEt;
    private Spinner mToSpnr;

    private String mTitle;
    private String mTo;
    private String mMessage;

    private TextView mCreateNotificationPageTitle;

    private ArrayList<String> mGroups;

    private void loadSpinnerData(String url) {
    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArr = new JSONArray(response);

                    for (int i = 0; i < jsonArr.length(); i++)
                    {
                        String group = jsonArr.getString(i);
                        mGroups.add(group);
                    }

                    mToSpnr.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mGroups));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
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
        requestQueue.add(stringRequest);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_notification_fragment, container, false);

        Typeface bebas_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/BebasNeue-Regular.ttf");

        mCreateNotificationPageTitle = view.findViewById(R.id.screen_title_text_view);
        mCreateNotificationPageTitle.setTypeface((bebas_font));

        mTitleEt = view.findViewById(R.id.title_et);
        mMessageEt = view.findViewById(R.id.message_et);
        mSendBtn = view.findViewById(R.id.send_btn);
        mGroups = new ArrayList<>();
        mToSpnr = view.findViewById(R.id.to_spnr);

        loadSpinnerData(URL);

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
                    String url = BuildConfig.PUSH_URL_STR;

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i(TAG, String.format("response: %s", response));
                                    Toast toast= Toast.makeText(getActivity(),
                                            response, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 30);
                                    toast.show();
                                    if (response.contains("notification sent"))
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mTitleEt.setText("");
                                                mMessageEt.setText("");
                                            }
                                        });
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
