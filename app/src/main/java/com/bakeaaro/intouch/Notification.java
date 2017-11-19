package com.bakeaaro.intouch;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Notification {

    private UUID mUUID;
    private int mDbId;
    private String mTitle;
    //private Date mDateCreated;
    private String mDateCreated;
    private boolean isAuthor;
    private String mMessageBody;

    public Notification() {
        mUUID = UUID.randomUUID();
        mDbId = new Random().nextInt();
        mTitle = String.format("Notification #%d", mDbId);
        mDateCreated = "Right now";
        isAuthor = (new Random().nextInt() % 2 == 0) ? false : true;
        mMessageBody = "RAGE: Sing, Goddess, Achilles' rage, Black and murderous, " +
                "that cost the Greeks Incalculable pain, pitched countless souls " +
                "Of heroes into Hades' dark, And left their bodies to rot as feasts " +
                "For dogs and birds, as Zeus' will was done.";

    }

    public UUID getUUID() {
        return mUUID;
    }

    public int getDbId() {
        return mDbId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDateCreated() {
        return mDateCreated;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public String getMessageBody() {
        return mMessageBody;
    }
}
