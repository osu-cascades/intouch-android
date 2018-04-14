package com.abilitree.intouch;


import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

import static java.lang.Boolean.TRUE;

public class DisplayNotificationsFragment extends Fragment {


    private static String FRAGMENT_TAG;

    //This is for intent for opening and closing letter
    private static final int REQUEST_CODE_LETTER = 0;
    private boolean mNoteRead;

    private RecyclerView mNotificationRV;
    private NotificationAdapter mAdapter;

    private Button mLogoutBtn;
    private TextView mTitleTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FRAGMENT_TAG = this.getTag();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_notifications_fragment, container, false);

        mNotificationRV =  view.findViewById(R.id.notifcation_rv);
        mNotificationRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        mTitleTV = (TextView) view.findViewById(R.id.display_notifications_title_tv);
        mLogoutBtn = view. findViewById(R.id.logout_btn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                Settings.clearLoginSettings(activity);
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });


        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        MailBox mailBox = MailBox.getInstance(getActivity());

        List<Notification> notifications = mailBox.getNotifications(FRAGMENT_TAG);
        if (mAdapter == null) {
            mAdapter = new NotificationAdapter(notifications);
            mNotificationRV.setAdapter(mAdapter);
        } else {
            mAdapter.setNotifications(notifications);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NotificationHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Notification mNotification;

        private TextView mTitleTV;
        private TextView mDateTV;
        private TextView mFromTV;
        //private TextView mBodyTv;
        private ImageView mOpenedIV;

        public NotificationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));

            itemView.setOnClickListener(this);

            mTitleTV = itemView.findViewById(R.id.title_tv);
            mDateTV = itemView.findViewById(R.id.date_tv);
            mFromTV = itemView.findViewById(R.id.from_tv);



        }

        public void bind(Notification notification) {
            mNotification = notification;
            mTitleTV.setText(mNotification.getTitle());
            mDateTV.setText(mNotification.getDateCreated());
            mFromTV.setText(mNotification.getFrom());
        }


/*
This is for passing data from listing notification to viewing single notifications
 */
        @Override
        public void onClick(View view){
            //Have to use getActivity() passing a fragment as Context is invalid
            String noteTitle = mNotification.getTitle();
            String noteDate = mNotification.getDateCreated();
            String noteFrom = mNotification.getFrom();
            String noteBody = mNotification.getMessageBody();

            Intent intent = ShowSingleNotification.newIntent(getActivity(), noteTitle, noteDate, noteFrom, noteBody);
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_LETTER);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_LETTER){
            if (data == null){
                return;
            }
            mNoteRead = ShowSingleNotification.wasLetterRead(data);
        }
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

        private List<Notification> mNotifications;

        public NotificationAdapter(List<Notification> notifications) {
            mNotifications = notifications;
        }

        @Override
        public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new NotificationHolder(layoutInflater, parent);

        }

        @Override
        public void onBindViewHolder(NotificationHolder holder, int position) {
            Notification notification = mNotifications.get(position);
            holder.bind(notification);
        }

        @Override
        public int getItemCount() {
            return mNotifications.size();
        }

        public void setNotifications(List<Notification> notifications) {
            mNotifications = notifications;
        }
    }

}