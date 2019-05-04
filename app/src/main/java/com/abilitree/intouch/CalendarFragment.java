package com.abilitree.intouch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private MaterialCalendarView mCalendarView;
    private RecyclerView mEventRV;
    private EventAdapter mAdapter;

    private List<Event> mEventList;
    private List<CalendarDay> mDaysWithEvents;

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

        mCalendarView.addDecorator(new EventDecorator(getActivity()));

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

        // When retrieve events from Rails API, will need to split 'time' at 'T' and remove trailing timezone
        mEventList = new ArrayList<Event>();
        mDaysWithEvents = new ArrayList<CalendarDay>();
        Event event = new Event("title", "description",  "2019-05-30", "01:00:00.000", "place", "notes", "groups", "host", "#F596EB");
        Event event2 = new Event("t", "d",  "2019-05-13", "01:00:00.000", "p", "n", "g", "h", "#A396F5");

        mEventList.add(event);
        mEventList.add(event2);

        for (Event e : mEventList) {
            mDaysWithEvents.add(CalendarDay.from(e.getYear(), e.getMonth(), e.getDay()));
        }

        if (mAdapter == null) {
            mAdapter = new EventAdapter(mEventList);
            mEventRV.setAdapter(mAdapter);
        } else {
            mAdapter.setEvents(mEventList);
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

            // Set border color
            GradientDrawable border = new GradientDrawable();
            border.setColor(Color.WHITE);
            border.setCornerRadius(5);
            border.setStroke(10, Color.parseColor(event.getColor()));
            itemView.setBackground(border);

            // Set whole event background color
//            itemView.setBackgroundColor(Color.parseColor(event.getColor()));
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

    private class EventDecorator implements DayViewDecorator {

        private Drawable highlightDrawable;
        private Context mContext;

        public EventDecorator(Context context) {
            mContext = context;
            highlightDrawable = mContext.getResources().getDrawable(R.drawable.circle_background);
        }

        @Override public boolean shouldDecorate(final CalendarDay day) {
            return mDaysWithEvents.contains(day);
        }

        @Override public void decorate(final DayViewFacade view) {
            view.setBackgroundDrawable(highlightDrawable);
        }
    }
}
