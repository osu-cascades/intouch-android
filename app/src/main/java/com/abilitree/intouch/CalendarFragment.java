package com.abilitree.intouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;


public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private CalendarView mCalendarView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendarView = view.findViewById(R.id.calendar_view);

        return view;
    }
}
