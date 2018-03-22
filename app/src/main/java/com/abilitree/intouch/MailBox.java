package com.abilitree.intouch;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.*;

import com.abilitree.intouch.database.NoteBaseHelper;
import com.abilitree.intouch.database.NoteCursorWrapper;
import com.abilitree.intouch.database.NoteDbSchema;
import com.abilitree.intouch.database.NoteDbSchema.NoteTable;

import java.util.ArrayList;
import java.util.List;

public class MailBox {

    String TAG = "FireTree-MailBox";

    private static MailBox sMailBox;
    private List<Notification> mReceivedNotifications;
    private List<Notification> mSentNotifications;

    //THIS IS FOR DATABASE
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static MailBox getInstance(Context context) {
        if (sMailBox == null) {
            sMailBox = new MailBox(context);
        }
        return sMailBox;
    }

    private MailBox(Context context){
       // mReceivedNotifications = new ArrayList<>();
        mSentNotifications = new ArrayList<>();

        //THIS IS FOR DATABASE
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext)
                .getWritableDatabase();
    }

    //THis is for database Writing to the database
    private static ContentValues getContentValues(Notification note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.DATE, note.getDateCreated());
        values.put(NoteTable.Cols.SENDER, note.getFrom());
        values.put(NoteTable.Cols.BODY, note.getMessageBody());

        return values;
    }

    public Notification getNotification(String type, int id) {

        NoteCursorWrapper cursor = queryNotes(
                NoteTable.Cols.TITLE + " = ?",
                new String[] {}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getNote();
        } finally {
            cursor.close();
        }

//        Notification notification = null;
//
//        switch (type) {
//            case "receivedNotifications":
//                for (Notification n : mReceivedNotifications) {
//                    if (n.getDbId() == id) {
//                        notification = n;
//                        break;
//                    }
//                    notification = null;
//                }
//                break;
//            case "sentNotifications":
//                for (Notification n : mSentNotifications) {
//                    if (n.getDbId() == id) {
//                        notification = n;
//                        break;
//                    }
//                    notification = null;
//                }
//                break;
//            default:
//                notification = null;
//                break;
//        }
//        return notification;
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


    public List<Notification> getNotifications(String type) {

        List<Notification> notes = new ArrayList<>();

        NoteCursorWrapper cursor = queryNotes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNote());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

//        List<Notification> mNotifications;
//
//        switch (type) {
//            case "receivedNotifications":
//                mNotifications = mReceivedNotifications;
//                break;
//            case "sentNotifications":
//                mNotifications = mSentNotifications;
//                break;
//            default:
//                mNotifications = new ArrayList<>();
//                break;
//        }
//
//        return mNotifications;
        return notes;

    }



    public void createNotification(String title, String from, String datetime, String body) {

//        for (Notification notification : mReceivedNotifications) {
//            String titleStr = notification.getTitle();
//            String fromStr = notification.getFrom();
//            String datetimeStr = notification.getDateCreated();
//            String bodyStr = notification.getMessageBody();
//
//            Log.d(TAG, title); Log.d(TAG, notification.getTitle());
//            Log.d(TAG, from); Log.d(TAG, notification.getFrom());
//            //Log.d(TAG, datetime); Log.d(TAG, notification.getDateCreated().toString());
//            Log.d(TAG, body); Log.d(TAG, notification.getMessageBody());
//            if (titleStr == null)
//                continue;
//            if(titleStr.equals(title) && fromStr.equals(from) && datetimeStr.equals(datetime) && bodyStr.equals(body))
//                return;
//
//        }
//        Notification notification = new Notification(title, from, datetime, body);
//        mReceivedNotifications.add(0, notification);

        Notification notification = new Notification(title, from, datetime, body);

        //Inserting and updating rows for database ch 14 pg279
        ContentValues values = getContentValues(notification);
        mDatabase.insert(NoteTable.NAME, null, values);
    }

}
