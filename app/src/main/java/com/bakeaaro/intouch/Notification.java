package com.bakeaaro.intouch;

import java.util.Date;
import java.util.UUID;

public class Notification {

    private UUID mUUID;
    private int mDbId;
    private String mTitle;
    private Date mDateCreated;
    private boolean isAuthor;
    private String mMessageBody;

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
