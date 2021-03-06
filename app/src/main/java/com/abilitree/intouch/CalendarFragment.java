package com.abilitree.intouch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment implements TabViewActivity.UpdateFragmentView {

    private static final String TAG = "CalendarFragment";

    private MaterialCalendarView mCalendarView;
    private RecyclerView mEventRV;
    private EventAdapter mAdapter;

    private List<Event> mEventList;
    private List<CalendarDay> mDaysWithEvents;
    private List<Event> mSelectedDaysEvents;

    private JSONArray mEvents;
    private String mSelectedDay;

    private MailBox mailBox = MailBox.getInstance(getActivity());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        mCalendarView = view.findViewById(R.id.calendar_view);
        mEventRV = view.findViewById(R.id.event_rv);
        mEventRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        String month = String.format("%02d", CalendarDay.today().getMonth());
        String day = String.format("%02d", CalendarDay.today().getDay());

        mSelectedDay = String.format("%1$s-%2$s-%3$s", CalendarDay.today().getYear(), month, day);

        mCalendarView.setDateSelected(CalendarDay.today(), true);

        retrieveEvents();

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mEventList = mailBox.getEvents();

                String month = String.format("%02d", date.getMonth());
                String day = String.format("%02d", date.getDay());

                mSelectedDay = String.format("%1$s-%2$s-%3$s", date.getYear(), month, day);

                mSelectedDaysEvents = new ArrayList<Event>();

                for (Event e : mEventList) {
                    if (mSelectedDay.equals(e.getDate())) {
                        mSelectedDaysEvents.add(e);
                    }
                }

                if (mAdapter == null) {
                    mAdapter = new EventAdapter(mSelectedDaysEvents);
                    mEventRV.setAdapter(mAdapter);
                } else {
                    mAdapter.setEvents(mSelectedDaysEvents);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((TabViewActivity) context).mUpdateFragmentView = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveEvents();
    }

    @Override
    public void updateView() {
        updateUI();
    }

    public void updateUI() {
        mEventList = mailBox.getEvents();

        mDaysWithEvents = new ArrayList<CalendarDay>();
        mSelectedDaysEvents = new ArrayList<Event>();

        for (Event e : mEventList) {
            mDaysWithEvents.add(CalendarDay.from(e.getYear(), e.getMonth(), e.getDay()));
            if (mSelectedDay.equals(e.getDate())) {
                mSelectedDaysEvents.add(e);
            }
        }

        mCalendarView.addDecorator(new EventDecorator(getActivity()));

        if (mAdapter == null) {
            mAdapter = new EventAdapter(mSelectedDaysEvents);
            mEventRV.setAdapter(mAdapter);
        } else {
            mAdapter.setEvents(mSelectedDaysEvents);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void retrieveEvents() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.EVENTS_URL_STR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonObj = new JSONObject(response);

                    if (jsonObj.has("error")) {
                        Log.i(TAG, "Error response: " + jsonObj);
                        try {
                            final String error = jsonObj.getString("error");
                            Log.i(TAG, String.format("JSON error occurred: %s", error));
                            Toast toast= Toast.makeText(getActivity(), String.format("Failed to fetch events: %s", error), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } catch (JSONException e) {
                            Log.i(TAG, "JSON exception: " + e);
                        }
                    } else {
                        JSONArray events = jsonObj.getJSONArray("events");
                        mEvents = events;

                        new InsertEvents().execute();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "JSON exception: " + e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, String.format("Error response: %s", error));
                Toast toast= Toast.makeText(getActivity(), String.format("Failed to fetch groups: %s", error), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }) {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", Settings.getUsername(getContext()));
                params.put("password", Settings.getPassword(getContext()));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
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

            itemView.setBackgroundColor(Color.parseColor(event.getColor()));
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

    private class InsertEvents extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... foo) {
            for (int i = 0; i < mEvents.length(); i++) {
                try {
                    JSONObject event = mEvents.getJSONObject(i);
                    String dateTime = event.optString("time", null);
                    String[] dateTimeArray = dateTime.split("T");

                    mailBox.createEvent(
                            event.optString("title", null),
                            event.optString("description", null),
                            dateTimeArray[0],
                            dateTimeArray[1].split("-")[0],
                            event.optString("place", null),
                            event.optString("notes", null),
                            event.optString("group_participants", null),
                            event.optString("hosted_by", null),
                            event.optString("color", "#77961c"),
                            event.optInt("id", 0)
                    );
                } catch (JSONException e) {
                    Log.i(TAG, "JSON exception: " + e);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void bar)
        {
            updateUI();
        }
    }
}
