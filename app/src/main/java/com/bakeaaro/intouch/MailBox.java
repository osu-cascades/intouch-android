package com.bakeaaro.intouch;


import android.content.Context;

public class MailBox {

    private static MailBox sMailBox;

    public static MailBox get(Context context) {
        if (sMailBox == null) {
            sMailBox = new MailBox(context);
        }
        return sMailBox;
    }

    private MailBox(Context context){

    }

}
