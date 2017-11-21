package com.bakeaaro.intouch;


import android.content.ReceiverCallNotAllowedException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class DisplayNotificationsFragment extends Fragment {

    private RecyclerView mNotificationRV;
    private NotificationAdapter mAdapter;

    private List<Notification> mNotifications;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_notifications_fragment, container, false);

        mNotificationRV = (RecyclerView) view.findViewById(R.id.notifcation_rv);
        mNotificationRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        MailBox mailBox = MailBox.get(getActivity());
        List<Notification> notifications = mailBox.getReceivedNotifications();
        mAdapter = new NotificationAdapter(notifications);
        mNotificationRV.setAdapter(mAdapter);
    }

    private class NotificationHolder extends RecyclerView.ViewHolder {

        private Notification mNotification;

        private TextView mTitleTV;
        private TextView mDateTV;
        private TextView mFromTV;

        public NotificationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_notification, parent, false));

            mTitleTV = (TextView) itemView.findViewById(R.id.title_tv);
            mDateTV = (TextView) itemView.findViewById(R.id.date_tv);
            mFromTV = (TextView) itemView.findViewById(R.id.from_tv);

        }

        public void bind(Notification notification) {
            mNotification = notification;
            mTitleTV.setText(mNotification.getTitle());
            mDateTV.setText(mNotification.getDateCreated());
            mFromTV.setText(mNotification.getFrom());
        }

    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

        private List<Notification> mNotification;

        public NotificationAdapter(List<Notification> notifications) {
            mNotifications = notifications;
        }

        @Override
        public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInfalter = LayoutInflater.from(getActivity());

            return new NotificationHolder(layoutInfalter, parent);

        }

        @Override
        public void onBindViewHolder(NotificationHolder holder, int position) {
            Notification notification = mNotifications.get(position);
            holder.bind(notification);
            //holder.itemView.setBackground();
        }

        @Override
        public int getItemCount() {
            return mNotifications.size();
        }
    }

}