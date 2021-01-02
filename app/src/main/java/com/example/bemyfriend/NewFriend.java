package com.example.bemyfriend;

public class NewFriend {
    private String name, city, imageId;
    private int age;

    public NewFriend() {
    }

    public NewFriend(String name, int age, String city, String imageId) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getAgeInt() {
        return age;
    }

    public String getAgeString(){
        String a = age + "";
        return a;
    }

    public String getCity() {
        return city;
    }

    public String getImageId() {
        return imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
