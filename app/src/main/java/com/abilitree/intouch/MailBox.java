package com.abilitree.intouch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.abilitree.intouch.database.NoteBaseHelper;
import com.abilitree.intouch.database.NoteCursorWrapper;
import com.abilitree.intouch.database.NoteDbSchema.EventTable;
import com.abilitree.intouch.database.NoteDbSchema.NoteTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MailBox {

    private static final String TAG = "MailBox";

    private static MailBox sMailBox;
    private List<Notification> mReceivedNotifications;
    private List<Notification> mSentNotifications;

    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static MailBox getInstance(Context context) {
        if (sMailBox == null) {
            sMailBox = new MailBox(context);
        }
        return sMailBox;
    }

    private MailBox(Context context){
        mSentNotifications = new ArrayList<>();

        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext).getWritableDatabase();
    }

    // This is for writing to the database
    private static ContentValues getNotificationContentValues(Notification note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.DATE, note.getDateCreated());
        values.put(NoteTable.Cols.SENDER, note.getFrom());
        values.put(NoteTable.Cols.BODY, note.getMessageBody());
        values.put(NoteTable.Cols.FROM_USERNAME, note.getFromUsername());
        values.put(NoteTable.Cols.GROUP_RECIPIENTS, note.getGroupRecipients());

        return values;
    }

    private static ContentValues getEventContentValues(Event event) {
        ContentValues values = new ContentValues();
        values.put(EventTable.Cols.TITLE, event.getTitle());
        values.put(EventTable.Cols.DESCRIPTION, event.getDescription());
        values.put(EventTable.Cols.DATE, event.getDate());
        values.put(EventTable.Cols.TIME, event.getTime());
        values.put(EventTable.Cols.PLACE, event.getPlace());
        values.put(EventTable.Cols.NOTES, event.getNotes());
        values.put(EventTable.Cols.GROUP_PARTICIPANTS, event.getGroupParticipants());
        values.put(EventTable.Cols.HOST, event.getHost());
        values.put(EventTable.Cols.COLOR, event.getColor());

        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new NoteCursorWrapper(cursor);
    }

    private NoteCursorWrapper queryEvents(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                EventTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new NoteCursorWrapper(cursor);
    }

    public List<Notification> getNotifications() {
        List<Notification> notes = new ArrayList<>();

        NoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                notes.add(cursor.getNote());
            }
        } finally {
            cursor.close();
        }

        Collections.reverse(notes);

        Log.i(TAG, "Number of notifications: " + Integer.toString(notes.size()));
        return notes;
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();

        NoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                events.add(cursor.getEvent());
            }
        } finally {
            cursor.close();
        }

        Log.i(TAG, "Number of events: " + Integer.toString(events.size()));
        return events;
    }

    public ArrayList<HashMap<String, String>> getEventForDate(String date){
        ArrayList<HashMap<String, String>> eventList = new ArrayList<>();
        Cursor cursor = mDatabase.query(EventTable.NAME,
                null,
                NoteTable.Cols.DATE + "=?",
                new String[] { date },
                null,
                null,
                null);

        while (cursor.moveToNext()){
            HashMap<String,String> event = new HashMap<>();
            event.put("title", cursor.getString(cursor.getColumnIndex(EventTable.Cols.TITLE)));
            event.put("description", cursor.getString(cursor.getColumnIndex(EventTable.Cols.DESCRIPTION)));
            event.put("date", cursor.getString(cursor.getColumnIndex(EventTable.Cols.DATE)));
            event.put("time", cursor.getString(cursor.getColumnIndex(EventTable.Cols.TIME)));
            event.put("place", cursor.getString(cursor.getColumnIndex(EventTable.Cols.PLACE)));
            event.put("notes", cursor.getString(cursor.getColumnIndex(EventTable.Cols.NOTES)));
            event.put("group_participants", cursor.getString(cursor.getColumnIndex(EventTable.Cols.GROUP_PARTICIPANTS)));
            event.put("host", cursor.getString(cursor.getColumnIndex(EventTable.Cols.HOST)));
            event.put("color", cursor.getString(cursor.getColumnIndex(EventTable.Cols.COLOR)));
            eventList.add(event);
        }
        return  eventList;
    }

    public void createNotification(String title, String from, String datetime, String body, String fromUsername, String groupRecipients) {
        Notification notification = new Notification(title, from, datetime, body, fromUsername, groupRecipients);

        //Inserting and updating rows for database ch 14 pg279
        ContentValues values = getNotificationContentValues(notification);
        try {
            long row = mDatabase.insertOrThrow(NoteTable.NAME, null, values);
            Log.i(TAG, "Inserted notification: " + row);
        } catch (SQLException e) {
            Log.i(TAG, "SQL exception inserting notification: " + e);
        }
    }

    public void createEvent(String title, String description, String date, String time, String place, String notes, String groupParticipants, String host, String color) {
        Event event = new Event(title, description, date, time, place, notes, groupParticipants, host, color);

        ContentValues values = getEventContentValues(event);
        try {
            long row = mDatabase.insertOrThrow(EventTable.NAME, null, values);
            Log.i(TAG, "Inserted event: " + row);
        } catch (SQLException e) {
            Log.i(TAG, "SQL exception inserting event: " + e);
        }
    }

    public void deleteAllNotifications() {
        mDatabase.delete(NoteTable.NAME, null, null);
    }

    public void deleteAllEvents() {
        mDatabase.delete(EventTable.NAME, null, null);
    }

    public boolean deleteNotification(Notification notification) {
        return mDatabase.delete(
                NoteTable.NAME,
                NoteTable.Cols.BODY + "=? AND " +
                        NoteTable.Cols.FROM_USERNAME + "=? AND " +
                        NoteTable.Cols.DATE + "=? AND " +
                        NoteTable.Cols.SENDER + "=? AND " +
                        NoteTable.Cols.TITLE + "=? AND " +
                        NoteTable.Cols.GROUP_RECIPIENTS + "=?",
                new String[]{
                        notification.getMessageBody(),
                        notification.getFromUsername(),
                        notification.getDateCreated(),
                        notification.getFrom(),
                        notification.getTitle(),
                        notification.getGroupRecipients()
                }
        ) > 0;
    }

    public boolean deleteEvent(Event event) {
        return mDatabase.delete(
                EventTable.NAME,
                EventTable.Cols.TITLE + "=? AND " +
                        EventTable.Cols.DESCRIPTION + "=? AND " +
                        EventTable.Cols.DATE + "=? AND " +
                        EventTable.Cols.TIME + "=? AND " +
                        EventTable.Cols.PLACE + "=? AND " +
                        EventTable.Cols.NOTES + "=? AND " +
                        EventTable.Cols.GROUP_PARTICIPANTS + "=? AND" +
                        EventTable.Cols.HOST + "=? AND" +
                        EventTable.Cols.COLOR + "=?",
                new String[] {
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getTime(),
                        event.getPlace(),
                        event.getNotes(),
                        event.getGroupParticipants(),
                        event.getHost(),
                        event.getColor()
                }
        ) > 0;
    }
}
