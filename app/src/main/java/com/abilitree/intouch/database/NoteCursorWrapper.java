package com.abilitree.intouch.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import com.abilitree.intouch.Notification;
import com.abilitree.intouch.database.NoteDbSchema.NoteTable;

public class NoteCursorWrapper extends CursorWrapper{
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Notification getNote() {
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        String date = getString(getColumnIndex(NoteTable.Cols.DATE));
        String sender = getString(getColumnIndex(NoteTable.Cols.SENDER));
        String body = getString(getColumnIndex(NoteTable.Cols.BODY));

        Notification note = new Notification(title, sender,  date, body);
        note.setTitle(title);
        note.setDateCreated(date);
        note.setFrom(sender);
        note.setMessageBody(body);

        return note;
    }
}
