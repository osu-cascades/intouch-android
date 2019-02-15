package com.abilitree.intouch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DisplayNotificationsFragment extends Fragment implements TabViewActivity.UpdateFragmentRecyclerView {

    private static String FRAGMENT_TAG;

    // This is for intent for opening and closing letter
    private static final int REQUEST_CODE_LETTER = 0;
    private boolean mNoteRead;

    private RecyclerView mNotificationRV;
    private NotificationAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FRAGMENT_TAG = this.getTag();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((TabViewActivity) context).mUpdateFragmentRecyclerView = this;
    }

    @Override
    public void updateView() {
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_notifications_fragment, container, false);

        mNotificationRV =  view.findViewById(R.id.notifcation_rv);
        mNotificationRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        MailBox mailBox = MailBox.getInstance(getActivity());

        List<Notification> notifications = mailBox.getNotifications();
        if (mAdapter == null) {
            mAdapter = new NotificationAdapter(notifications);
            mNotificationRV.setAdapter(mAdapter);
        } else {
            mAdapter.setNotifications(notifications);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Notification mNotification;

        private TextView mTitleTV;
        private TextView mDateTV;
        private TextView mFromTV;
        private ImageView mOpenedIV;

        public NotificationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

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

        private AlertDialog confirmDeletion() {
            AlertDialog confirmDeletion = new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this notification?")
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MailBox mailBox = MailBox.getInstance(getActivity());
                        boolean isDeleted = mailBox.deleteNotification(mNotification);
                        Log.i("DeleteDialog", "Deleted notification: " + isDeleted);
                        if (isDeleted) {
                            Toast toast= Toast.makeText(getActivity(), "Successfully deleted notification", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Toast toast= Toast.makeText(getActivity(), "Failed to delete notification", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        updateUI();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

            return confirmDeletion;
        }

        // This is for passing data from listing notification to viewing single notifications
        @Override
        public void onClick(View view) {
            // Have to use getActivity() passing a fragment as Context is invalid
            String noteTitle = mNotification.getTitle();
            String noteDate = mNotification.getDateCreated();
            String noteFrom = mNotification.getFrom();
            String noteBody = mNotification.getMessageBody();
            String noteFromUsername = mNotification.getFromUsername();
            String noteGroupRecipients = mNotification.getGroupRecipients();

            Intent intent = ShowSingleNotification.newIntent(getActivity(), noteTitle, noteDate, noteFrom, noteBody, noteFromUsername, noteGroupRecipients);
            startActivityForResult(intent, REQUEST_CODE_LETTER);
        }

        @Override
        public boolean onLongClick(View view) {
            AlertDialog confirmDeletion = confirmDeletion();
            confirmDeletion.show();
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_LETTER) {
            if (data == null) {
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
