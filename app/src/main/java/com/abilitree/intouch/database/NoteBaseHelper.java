package com.abilitree.intouch.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abilitree.intouch.database.NoteDbSchema.NoteTable;

/**
 * Created by Kyleen on 3/18/18.
 */

public class NoteBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 4;
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
            NoteTable.Cols.BODY +
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
    }
}
