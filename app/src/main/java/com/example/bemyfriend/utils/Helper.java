package com.example.bemyfriend.utils;

import com.example.bemyfriend.model.User;

import java.util.ArrayList;
import java.util.Random;

public class Helper {
    private static Random rg= new Random();
    //create chat name for firebase
    public static String FlushName(User otherUser, User thisUser) {
        String name = AlphabetOrder(otherUser.getParentName(), thisUser.getParentName());
        String childName = AlphabetOrder(otherUser.getChildName(), thisUser.getChildName());
        String s = name + childName + (otherUser.getAge() + thisUser.getAge());
        return s;
    }

    //sort the names by alphabetic order
    private static String AlphabetOrder(String parentName1, String parentName2) {
        int i;
        for (i = 0; i < parentName1.length() && i < parentName2.length(); i++) {
            if (parentName1.charAt(i) != parentName2.charAt(i)) {
                if (parentName1.charAt(i) < parentName2.charAt(i))
                    return parentName1 + parentName2;
                else
                    return parentName2 + parentName1;
            }
        }
        if (i == parentName1.length()) return parentName1 + parentName2;
        else return parentName2 + parentName1;
    }
    public static String produceHobbyString(User user){
        String hobby = "תחומי עניין: ";
        if (user.getHobby().getBoardGames())
            hobby += "משחקי קופסא, ";
        if (user.getHobby().getScience())
            hobby += "מדעים, ";
        if (user.getHobby().getArt())
            hobby += "אומנות, ";
        if (user.getHobby().getGaming())
            hobby += "משחקי מחשב, ";
        if (user.getHobby().getMusic())
            hobby += "מוזיקה, ";
        if (user.getHobby().getNature())
            hobby += "טבע, ";
        if (user.getHobby().getSports())
            hobby += "ספורט, ";
        if (user.getHobby().getOther().length() != 0) {
            hobby += (user.getHobby().getOther()) + ", ";
        }
        hobby = hobby.substring(0, hobby.length() - 2);
        hobby += ".";
        return hobby;
    }
    //change the list to random order
    public static ArrayList<User> randomOrder(ArrayList<User> list, User thisUser) {
        ArrayList<User> ans = new ArrayList<>();
        while (list.size() != 0) {
            int n = Math.abs(rg.nextInt() % list.size());
            ans.add(list.get(n));
            list.remove(n);
        }
        if (thisUser.getChats().size() != 0) {
            for (int i = 0; i < ans.size(); i++) {
                for (int j = 0; j < thisUser.getChats().size(); j++) {
                    if (thisUser.getChats().get(j).getMail().equals(ans.get(i).getMail())) {
                        ans.remove(i);
                        i--;
                        j = thisUser.getChats().size();
                    }
                }
            }
        }
        return ans;
    }
}
