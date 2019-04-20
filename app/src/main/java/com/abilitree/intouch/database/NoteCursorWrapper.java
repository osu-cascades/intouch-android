package com.abilitree.intouch.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.abilitree.intouch.Event;
import com.abilitree.intouch.Notification;
import com.abilitree.intouch.database.NoteDbSchema.NoteTable;
import com.abilitree.intouch.database.NoteDbSchema.EventTable;

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Notification getNote() {
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        String date = getString(getColumnIndex(NoteTable.Cols.DATE));
        String sender = getString(getColumnIndex(NoteTable.Cols.SENDER));
        String body = getString(getColumnIndex(NoteTable.Cols.BODY));
        String fromUsername = getString(getColumnIndex(NoteTable.Cols.FROM_USERNAME));
        String groupRecipients = getString(getColumnIndex(NoteTable.Cols.GROUP_RECIPIENTS));

        Notification note = new Notification(title, sender,  date, body, fromUsername, groupRecipients);
        note.setTitle(title);
        note.setDateCreated(date);
        note.setFrom(sender);
        note.setMessageBody(body);
        note.setFromUsername(fromUsername);
        note.setGroupRecipients(groupRecipients);

        return note;
    }

    public Event getEvent() {
        String title = getString(getColumnIndex(EventTable.Cols.TITLE));
        String description = getString(getColumnIndex(EventTable.Cols.DESCRIPTION));
        String date = getString(getColumnIndex(EventTable.Cols.DATE));
        String time = getString(getColumnIndex(EventTable.Cols.TIME));
        String place = getString(getColumnIndex(EventTable.Cols.PLACE));
        String notes = getString(getColumnIndex(EventTable.Cols.NOTES));
        String groupParticipants = getString(getColumnIndex(EventTable.Cols.GROUP_PARTICIPANTS));
        String color = getString(getColumnIndex(EventTable.Cols.COLOR));

        Event event = new Event(title, description, date, time, place, notes, groupParticipants, color);

        return event;
    }
}
