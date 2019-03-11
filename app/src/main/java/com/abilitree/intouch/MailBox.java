package com.abilitree.intouch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.abilitree.intouch.database.NoteBaseHelper;
import com.abilitree.intouch.database.NoteCursorWrapper;
import com.abilitree.intouch.database.NoteDbSchema.NoteTable;

import java.util.ArrayList;
import java.util.Collections;
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
    private static ContentValues getContentValues(Notification note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.DATE, note.getDateCreated());
        values.put(NoteTable.Cols.SENDER, note.getFrom());
        values.put(NoteTable.Cols.BODY, note.getMessageBody());
        values.put(NoteTable.Cols.FROM_USERNAME, note.getFromUsername());
        values.put(NoteTable.Cols.GROUP_RECIPIENTS, note.getGroupRecipients());

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

    public void createNotification(String title, String from, String datetime, String body, String fromUsername, String groupRecipients) {
        Notification notification = new Notification(title, from, datetime, body, fromUsername, groupRecipients);

        //Inserting and updating rows for database ch 14 pg279
        ContentValues values = getContentValues(notification);
        try {
            long row = mDatabase.insertOrThrow(NoteTable.NAME, null, values);
            Log.i(TAG, "Inserted row: " + row);
        } catch (SQLException e) {
            Log.i(TAG, "SQL exception: " + e);
        }
    }

    public void deleteAllNotifications() {
        mDatabase.delete(NoteTable.NAME, null, null);
    }

    public boolean deleteNotification(Notification notification) {
        return mDatabase.delete(
                NoteTable.NAME,
                NoteTable.Cols.BODY + "=? and " +
                        NoteTable.Cols.FROM_USERNAME + "=? and " +
                        NoteTable.Cols.DATE + "=? and " +
                        NoteTable.Cols.SENDER + "=? and " +
                        NoteTable.Cols.TITLE + "=? and " +
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
}
