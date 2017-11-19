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

public class DisplayNotificationsFragment extends Fragment {

    private RecyclerView mNotificationRV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_notifications_fragment, container, false);

        mNotificationRV = (RecyclerView) view.findViewById(R.id.notifcation_rv);
        mNotificationRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}