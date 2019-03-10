package com.abilitree.intouch;

public class Notification {

    private String mTitle;
    private String mDateCreated;
    private String mMessageBody;
    private String mFrom;
    private String mFromUsername;
    private String mGroupRecipients;

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
