package com.abilitree.intouch;

/**
 * Created by HannaBennett on 4/11/19.
 */

public class Event {

    private String mTitle;
    private String mDescription;
    private String mDate;
    private String mTime;
    private String mPlace;
    private String mNotes;
    private String mGroupParticipants;
    private String mColor;

    public Event(String title, String description, String date, String time, String place, String notes, String groupParticipants, String color) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mTime = time;
        mPlace = place;
        mNotes = notes;
        mGroupParticipants = groupParticipants;
        mColor = color;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getDate() {
        return mDate;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTime() {
        return mTime;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setGroupParticipants(String groupParticipants) {
        mGroupParticipants = groupParticipants;
    }

    public String getGroupParticipants() {
        return mGroupParticipants;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getColor() {
        return mColor;
    }
}
