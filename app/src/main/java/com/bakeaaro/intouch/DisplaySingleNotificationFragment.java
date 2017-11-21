package com.bakeaaro.intouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DisplaySingleNotificationFragment extends Fragment {

    private static final String ARG_NOTIFICATION_ID = "notification_id";

    private Notification mNotification;

    private TextView mTitleTV;
    private TextView mDateTV;
    private TextView mFromTV;
    private TextView mMessageTV;
    private Button mDeleteBtn;

    public static DisplaySingleNotificationFragment newInstance(int notificationId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION_ID, notificationId);
        DisplaySingleNotificationFragment fragment = new DisplaySingleNotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int notificationId = (int) getArguments().getSerializable(ARG_NOTIFICATION_ID);
        mNotification = MailBox.getInstance(getActivity()).getReceivedNotification(notificationId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.single_notification, container, false);

        mTitleTV = view.findViewById(R.id.title_tv);
        mDateTV = view.findViewById(R.id.date_tv);
        mFromTV = view.findViewById(R.id.from_tv);
        mMessageTV = view.findViewById(R.id.message_tv);
        mDeleteBtn = view.findViewById(R.id.delete_notification_btn);

        mTitleTV.setText(mNotification.getTitle());
        mDateTV.setText(mNotification.getDateCreated());
        mFromTV.setText(mNotification.getFrom());
        mMessageTV.setText(mNotification.getMessageBody());
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Notification will be deleted at a later date", Toast.LENGTH_SHORT).show();
                //TODO AsyncTask delete entry from db table?
                //TODO remove from view pager, show next notification
            }
        });

        mNotification.setViewed();

        return view;
    }


}
