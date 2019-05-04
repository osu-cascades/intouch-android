package com.abilitree.intouch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private MaterialCalendarView mCalendarView;
    private RecyclerView mEventRV;
    private EventAdapter mAdapter;

    private ArrayList<HashMap<String, String>> mEventList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendarView = view.findViewById(R.id.calendar_view);
        mEventRV = view.findViewById(R.id.event_rv);
        mEventRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCalendarView.setDateSelected(CalendarDay.today(), true);

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Log.i(TAG, "date: " + date.toString());
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

    public void updateUI() {
        MailBox mailBox = MailBox.getInstance(getActivity());

//        List<Event> events = mailBox.getEvents();

        List<Event> events = new ArrayList<Event>();
        Event event = new Event("title", "description", "date", "time", "place", "notes", "groups", "host", "color");
        Event event2 = new Event("t", "d", "d", "t", "p", "n", "g", "h", "c");

        events.add(event);
        events.add(event2);

        if (mAdapter == null) {
            mAdapter = new EventAdapter(events);
            mEventRV.setAdapter(mAdapter);
        } else {
            mAdapter.setEvents(events);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder {
        private Event mEvent;

        private TextView mTitleTV;
        private TextView mDescriptionTV;
        private TextView mDateTV;
        private TextView mTimeTV;
        private TextView mPlaceTV;
        private TextView mHostTV;
        private TextView mNotesTV;

        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_event, parent, false));

            mTitleTV = itemView.findViewById(R.id.title);
            mDescriptionTV = itemView.findViewById(R.id.description);
            mDateTV = itemView.findViewById(R.id.date);
            mTimeTV = itemView.findViewById(R.id.time);
            mPlaceTV = itemView.findViewById(R.id.place);
            mHostTV = itemView.findViewById(R.id.host);
            mNotesTV = itemView.findViewById(R.id.notes);
        }

        public void bind(Event event) {
            mEvent = event;

            mTitleTV.setText(event.getTitle());
            mDescriptionTV.setText(event.getDescription());
            mDateTV.setText(event.getDate());
            mTimeTV.setText(event.getTime());
            mPlaceTV.setText(event.getPlace());
            mHostTV.setText(event.getHost());
            mNotesTV.setText(event.getNotes());
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private List<Event> mEvents;

        public EventAdapter(List<Event> events) {
            mEvents = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = mEvents.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }

        public void setEvents(List<Event> events) {
            mEvents = events;
        }
    }
}
