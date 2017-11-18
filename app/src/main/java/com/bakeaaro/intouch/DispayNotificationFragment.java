package com.bakeaaro.intouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DispayNotificationFragment extends Fragment {

    private Notification mNotification;

    private TextView mToTv;
    private TextView mDateTV;
    private TextView mFromTV;
    private TextView mMessageTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotification = new Notification();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_notification_fragment, container, false);

        mToTv = (TextView) view.findViewById(R.id.to_tv);

        return view;
    }


}
