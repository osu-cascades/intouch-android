package com.abilitree.intouch;

import org.junit.Test;
import static org.junit.Assert.*;

public class NotificationTest {

    @Test
    public void instantiation() throws Exception {
        Notification n = new Notification("Fake Title",
                "Fake Sender",
                "Fake DateTime",
                "Fake Body");
    }

}
