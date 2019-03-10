package com.abilitree.intouch;

public class Notification {

    private int mDbId;
    private String mTitle;
    private String mDateCreated;
    private boolean isAuthor;
    private String mMessageBody;
    private String mFrom;
    private String mFromUsername;
    private String mGroupRecipients;
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

    public String getFromUsername() {
        return mFromUsername;
    }

    public void setFromUsername(String fromUsername) {
        mFromUsername = fromUsername;
    }

    public String getGroupRecipients() {
        return mGroupRecipients;
    }

    public void setGroupRecipients(String groupRecipients) {
        mGroupRecipients = groupRecipients;
    }

    public Notification(String title, String from, String datetime, String body, String fromUsername, String groupRecipients) {
        mTitle = title;
        mFrom = from;
        mDateCreated = datetime;
        mMessageBody = body;
        mFromUsername = fromUsername;
        mGroupRecipients = groupRecipients;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getDbId() {
        return mDbId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setMessageBody(String messageBody) {
        mMessageBody = messageBody;
    }

    public String getMessageBody() {
        return mMessageBody;
    }
}
