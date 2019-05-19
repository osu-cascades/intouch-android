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
    private String mHost;
    private String mColor;
    private Integer mRailsId;

    public Event(String title, String description, String date, String time, String place, String notes, String groupParticipants, String host, String color, Integer railsId) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mTime = time;
        mPlace = place;
        mNotes = notes;
        mGroupParticipants = groupParticipants;
        mHost = host;
        mColor = color;
        mRailsId = railsId;
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

    public Integer getYear() {
        try {
            return Integer.parseInt(mDate.split("-")[0]);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse year: " + nfe);
            return 0;
        }
    }

    public Integer getMonth() {
        try {
            return Integer.parseInt(mDate.split("-")[1]);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse month: " + nfe);
            return 0;
        }
    }

    public Integer getDay() {
        try {
            return Integer.parseInt(mDate.split("-")[2]);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse day: " + nfe);
            return 0;
        }
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

    public void setHost(String host) {
        mHost = host;
    }

    public String getHost() {
        return mHost;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getColor() {
        return mColor;
    }

    public Integer getRailsId() {
        return mRailsId;
    }

    public void setRailsId(Integer railsId) {
        mRailsId = railsId;
    }
}
