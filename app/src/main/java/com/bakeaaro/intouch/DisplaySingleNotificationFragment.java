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

        int notificationId = (int) getActivity().getIntent().getSerializableExtra(DisplaySingleNotificationActivity.EXTRA_NOTIFICATION_ID);
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

        return view;
    }


}
