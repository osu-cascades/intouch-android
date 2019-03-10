package com.abilitree.intouch;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NotificationTest {
    private Notification notification;

    @Before
    public void createNotification() {
        notification = new Notification("Title", "John Doe", "2019-02-21", "Body", "john_doe", "group1,group2");
    }

    @Test
    public void createsNotification() {
        assertEquals(notification.getMessageBody(), "Body");
        assertEquals(notification.getDateCreated(), "2019-02-21");
        assertEquals(notification.getFrom(), "John Doe");
        assertEquals(notification.getFromUsername(), "john_doe");
        assertEquals(notification.getGroupRecipients(), "group1,group2");
        assertEquals(notification.getTitle(), "Title");
    }

    @Test
    public void setsNotificationAttributes() {
        notification.setFromUsername("jane_doe");
        notification.setFrom("Jane Doe");
        notification.setDateCreated("2000-01-01");
        notification.setGroupRecipients("group3,group4");
        notification.setMessageBody("New body");
        notification.setTitle("New title");

        assertEquals(notification.getMessageBody(), "New body");
        assertEquals(notification.getDateCreated(), "2000-01-01");
        assertEquals(notification.getFrom(), "Jane Doe");
        assertEquals(notification.getFromUsername(), "jane_doe");
        assertEquals(notification.getGroupRecipients(), "group3,group4");
        assertEquals(notification.getTitle(), "New title");
    }
}
