package com.abilitree.intouch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DisplaySingleNotificationFragment extends Fragment {

    private static final String ARG_NOTIFICATION_ID = "notification_id";
    private static final String ARG_TYPE = "type";

    private Notification mNotification;

    private TextView mTitleTV;
    private TextView mDateTV;
    private TextView mFromTV;
    private TextView mMessageTV;
    private Button mDeleteBtn;

    public static DisplaySingleNotificationFragment newInstance(int notificationId, String type) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION_ID, notificationId);
        args.putSerializable(ARG_TYPE, type);
        DisplaySingleNotificationFragment fragment = new DisplaySingleNotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int notificationId = (int) getArguments().getSerializable(ARG_NOTIFICATION_ID);
        String type = (String) getArguments().getSerializable(ARG_TYPE);
        mNotification = MailBox.getInstance(getActivity()).getNotification(type, notificationId);
    }
    }



