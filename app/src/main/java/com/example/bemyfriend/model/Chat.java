package com.example.bemyfriend.model;

import java.util.Date;

public class Chat {
    private long time;
    private String name, lastMessage, imageId, mail;
    private int unreadMessages;

    public Chat(long time, String name, String lastMessage, String imageId, int unreadMessages, String mail) {
        this.time = time;
        this.name = name;
        this.lastMessage = lastMessage;
        this.imageId = imageId;
        this.unreadMessages = unreadMessages;
        this.mail = mail;
    }

    public Chat(String mail) {
        this.mail = mail;
        time = new Date().getTime();
        unreadMessages = 0;
        lastMessage = "";
    }

    public Chat() {
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
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

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getImageId() {
        return imageId;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessage(int i) {
        unreadMessages = i;
    }
}
