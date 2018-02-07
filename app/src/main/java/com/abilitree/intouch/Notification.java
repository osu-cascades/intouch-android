package com.bakeaaro.intouch;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Notification {

    private int mDbId;
    private String mTitle;
    //private Date mDateCreated;
    private String mDateCreated;
    private boolean isAuthor;
    private String mMessageBody;
    private String mFrom;
    private Boolean mViewed = false;

    public Boolean hasBeenViewed() {
        return mViewed;
    }

    public void setViewed() {
        mViewed = true;
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        mFrom = from;
    }

    public Notification() {
    }

    public void setDbId(int dbId) {
        mDbId = dbId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public void setMessageBody(String messageBody) {
        mMessageBody = messageBody;
    }

    public int getDbId() {
        return mDbId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public String getMessageBody() {
        return mMessageBody;
    }
}
