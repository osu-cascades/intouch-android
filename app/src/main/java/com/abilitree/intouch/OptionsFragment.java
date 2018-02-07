package com.abilitree.intouch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class OptionsFragment extends Fragment {

    private final static String TAG = "OptionsFragment";
    private static String FRAGMENT_TAG;

    private Switch mToggleServiceSw;
    private Intent mServiceIntent;

    //private boolean mSwitchState = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FRAGMENT_TAG = this.getTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.options_fragment, container, false);

        mServiceIntent = NotificationService.newIntent(getActivity());

        mToggleServiceSw = (Switch) view.findViewById(R.id.service_switch);

        mToggleServiceSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getActivity().startService(mServiceIntent);
                    Log.i(TAG, "Service started");

                } else {
                    getActivity().stopService(mServiceIntent);
                    Log.i(TAG, "Service stopped");
                }
            }
        });

        return view;

    }

}
