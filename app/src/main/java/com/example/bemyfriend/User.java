package com.example.bemyfriend;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String parentName, childName, mail, address, details, gender, picId;
    private int age;
    private Hobbies hobby;
    private ArrayList<Chat> chats = new ArrayList<>();

    public User(String parentName, String childName, String mail, String address, int age,
                String details, String gender, String picId, Hobbies hobby) {
        this.parentName = parentName;
        this.childName = childName;
        this.mail = mail;
        this.address = address;
        this.age = age;
        this.details = details;
        this.gender = gender;
        this.picId = picId;
        this.hobby = hobby;
    }

    public User() {
    }

    public void setUnreedMassegesToZero(String mailOther){
        for (int i=0;i<chats.size();i++){
            if(chats.get(i).getMail().equals(mailOther)){
                chats.get(i).setUnreadMessage(0);
            }
        }
    }

    public void deleteChat(String mail2) {
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).getMail().equals(mail2)) {
                chats.remove(i);
            }
        }
    }
    public void addChat(String name){
        chats.add(new Chat(name));
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public void addMessage(String mailu2, String message2){
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).getMail().equals(mailu2)) {
                chats.get(i).setUnreadMessage(chats.get(i).getUnreadMessages() + 1);
                chats.get(i).setText(message2);
                chats.get(i).setTime(new Date().getTime());
            }
        }
    }

    public void addMyMessage(String mailu2, String message2){
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).getMail().equals(mailu2)) {
                chats.get(i).setText(message2);
                chats.get(i).setTime(new Date().getTime());
            }
        }
    }


    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void setHobby(Hobbies hobby) {
        this.hobby = hobby;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPicId(String picId) { this.picId = picId; }

    public Hobbies getHobby() {
        return hobby;
    }
    
    public String getParentName() {
        return parentName;
    }

    public String getChildName() {
        return childName;
    }

    public String getMail() {
        return mail;
    }
    
    public String getAddress() {
        return address;
    }

    public int getAge() { return age; }
    
    public int getAgeInt() {
        return age;
    }

    public String getAgeString() { return( age+""); }

    public String getDetails() {
        return details;
    }

    public String getGender() {
        return gender;
    }

    public String getPicId() { return picId; }

}
