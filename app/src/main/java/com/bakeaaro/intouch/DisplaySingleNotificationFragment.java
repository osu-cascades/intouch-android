package com.bakeaaro.intouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DisplaySingleNotificationFragment extends Fragment {

    private Notification mNotification;

    private TextView mTitleTV;
    private TextView mDateTV;
    private TextView mFromTV;
    private TextView mMessageTV;
    private Button mDeleteBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNotification = new Notification();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_notification, container, false);

        mTitleTV = view.findViewById(R.id.title_tv);
        mDateTV = view.findViewById(R.id.title_tv);
        mFromTV = view.findViewById(R.id.title_tv);
        mMessageTV = view.findViewById(R.id.title_tv);
        mDeleteBtn = view.findViewById(R.id.delete_notification_btn);

        return view;
    }


}
