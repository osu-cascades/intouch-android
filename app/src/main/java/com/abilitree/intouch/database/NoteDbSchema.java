package com.abilitree.intouch.database;

/**
 * Created by Kyleen on 3/18/18.
 */

public class NoteDbSchema {
    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SENDER = "sender";
            public static final String BODY = "body";
        }
    }
}