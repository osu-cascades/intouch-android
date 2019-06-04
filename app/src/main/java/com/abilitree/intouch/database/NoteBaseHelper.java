package com.abilitree.intouch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abilitree.intouch.database.NoteDbSchema.EventTable;
import com.abilitree.intouch.database.NoteDbSchema.NoteTable;

/**
 * Created by Kyleen on 3/18/18.
 */

public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 7;
    private static final String DATABASE_NAME = "noteBase.db";

    public NoteBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "create table " + NoteTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            NoteTable.Cols.TITLE + "," +
            NoteTable.Cols.DATE + ", " +
            NoteTable.Cols.SENDER + ", " +
            NoteTable.Cols.BODY + ", " +
            NoteTable.Cols.FROM_USERNAME + " VARCHAR(50) DEFAULT NULL, " +
            NoteTable.Cols.GROUP_RECIPIENTS + " VARCHAR(2048) DEFAULT NULL" +
            ")"
        );

        db.execSQL(
            "CREATE TABLE " + EventTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            EventTable.Cols.TITLE + " TEXT NOT NULL, " +
            EventTable.Cols.DESCRIPTION + " TEXT NOT NULL, " +
            EventTable.Cols.DATE + " TEXT NOT NULL, " +
            EventTable.Cols.TIME + " TEXT NOT NULL, " +
            EventTable.Cols.PLACE + " TEXT NOT NULL, " +
            EventTable.Cols.NOTES + " TEXT NOT NULL, " +
            EventTable.Cols.GROUP_PARTICIPANTS + " TEXT NOT NULL, " +
            EventTable.Cols.HOST + " TEXT NOT NULL, " +
            EventTable.Cols.COLOR + " TEXT NOT NULL, " +
            EventTable.Cols.RAILS_ID + " INTEGER UNIQUE" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + NoteTable.NAME + " ADD COLUMN " + NoteTable.Cols.FROM_USERNAME + " VARCHAR(50) DEFAULT NULL");
        }

        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + NoteTable.NAME + " ADD COLUMN " + NoteTable.Cols.GROUP_RECIPIENTS + " VARCHAR(2048) DEFAULT NULL");
        }

        if (oldVersion < 5) {
            db.execSQL(
                    "CREATE TABLE " + EventTable.NAME + "(" +
                    " _id integer primary key autoincrement, " +
                    EventTable.Cols.TITLE + " TEXT NOT NULL, " +
                    EventTable.Cols.DATE + " INTEGER NOT NULL, " +
                    EventTable.Cols.TIME + " TEXT NOT NULL, " +
                    "location TEXT NOT NULL, " +
                    EventTable.Cols.NOTES + " TEXT NOT NULL, " +
                    "participants TEXT NOT NULL, " +
                    EventTable.Cols.COLOR + " TEXT NOT NULL" +
                    ")"
            );
        }

        if (oldVersion < 6) {
            db.execSQL("DROP TABLE " + EventTable.NAME);
            db.execSQL(
                "CREATE TABLE " + EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                EventTable.Cols.TITLE + " TEXT NOT NULL, " +
                EventTable.Cols.DESCRIPTION + " TEXT NOT NULL, " +
                EventTable.Cols.DATE + " TEXT NOT NULL, " +
                EventTable.Cols.TIME + " TEXT NOT NULL, " +
                EventTable.Cols.PLACE + " TEXT NOT NULL, " +
                EventTable.Cols.NOTES + " TEXT NOT NULL, " +
                EventTable.Cols.GROUP_PARTICIPANTS + " TEXT NOT NULL, " +
                EventTable.Cols.HOST + " TEXT NOT NULL, " +
                EventTable.Cols.COLOR + " TEXT NOT NULL" +
                ")"
            );
        }

        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE " + EventTable.NAME + " ADD COLUMN " + EventTable.Cols.RAILS_ID + " INTEGER");
            db.execSQL("CREATE UNIQUE INDEX rails_id_index ON " + EventTable.NAME + "(" + EventTable.Cols.RAILS_ID + ")");
        }
    }
}
