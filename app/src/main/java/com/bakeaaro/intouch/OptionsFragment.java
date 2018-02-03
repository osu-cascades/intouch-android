package com.bakeaaro.intouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class OptionsFragment extends Fragment {

    private final static String TAG = "Pusher MainActivity";
    private static String FRAGMENT_TAG;

    private Switch mToggleSerice;
    private Intent mServiceIntent;

    private boolean mSwitchState = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FRAGMENT_TAG = this.getTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.options_fragment, container, false);

        mServiceIntent = NotificationService.newIntent(getActivity());

        mToggleSerice = (Switch) view.findViewById(R.id.service_switch);
        mToggleSerice.setChecked(false);
        mToggleSerice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().stopService(mServiceIntent);
                    mSwitchState = false;
                    mToggleSerice.setChecked(false);
                    Log.i(TAG, "Service stopped");
                } else {
                    getActivity().startService(mServiceIntent);
                    mSwitchState = true;
                    mToggleSerice.setChecked(true);
                    Log.i(TAG, "Service started");
                }
            }
        });


        return view;

    }

}
