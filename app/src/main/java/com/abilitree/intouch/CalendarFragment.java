package com.abilitree.intouch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private CalendarView mCalendarView;
    private ListView mEventListView;
    private ArrayList<HashMap<String, String>> mEventList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendarView = view.findViewById(R.id.calendar_view);
        mEventListView = view.findViewById(R.id.event_list_view);

        MailBox mailBox = MailBox.getInstance(getActivity());

        Calendar now = Calendar.getInstance();

        Integer yearMonthDay = Integer.valueOf("" + now.get(Calendar.YEAR) + now.get(Calendar.MONTH) + now.get(Calendar.DAY_OF_MONTH));
        Log.i(TAG, "yearmonthday" + yearMonthDay);
//        mEventList = mailBox.getEventForDate(yearMonthDay);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Log.i(TAG, "year, month, dayofmonth" + year + month + dayOfMonth);
            }
        });

        return view;
    }
}
