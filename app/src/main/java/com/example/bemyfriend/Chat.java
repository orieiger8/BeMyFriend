package com.example.bemyfriend;

import java.util.Date;

public class Chat {
    private  long time;
    private  String title;
    private  String text;
    private  String imageId;
    private  int unreadMessages;
    private String mail;

    public Chat(long time, String title, String text, String imageId, int unreadMessages, String mail) {
        this.time = time;
        this.title = title;
        this.text = text;
        this.imageId = imageId;
        this.unreadMessages = unreadMessages;
        this.mail = mail;
    }

    public Chat(String mail) {
        this.mail = mail;
        time = new Date().getTime();
        unreadMessages = 0;
        text = "";
    }

    public Chat() {
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImageId() {
        return imageId;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessage(int i) {
        unreadMessages=i;
    }
}
