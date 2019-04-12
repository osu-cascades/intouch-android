package com.abilitree.intouch;

/**
 * Created by HannaBennett on 4/11/19.
 */

public class Event {

    private String mTitle;
    // date example: 042019 = April 20th, 2019 - makes event lookup from calendar easier
    private Integer mDate;
    private String mTime;
    private String mLocation;
    private String mNotes;
    private String mParticipants;
    private String mColor;

    public Event(String title, Integer date, String time, String location, String notes, String participants, String color) {
        mTitle = title;
        mDate = date;
        mTime = time;
        mLocation = location;
        mNotes = notes;
        mParticipants = participants;
        mColor = color;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDate(Integer date) {
        mDate = date;
    }

    public Integer getDate() {
        return mDate;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getTime() {
        return mTime;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setParticipants(String participants) {
        mParticipants = participants;
    }

    public String getParticipants() {
        return mParticipants;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public String getColor() {
        return mColor;
    }
}
