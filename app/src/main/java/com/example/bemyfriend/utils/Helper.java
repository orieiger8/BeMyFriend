package com.example.bemyfriend.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bemyfriend.R;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.Chats;
import com.example.bemyfriend.screens.FindNewFriends;
import com.example.bemyfriend.screens.Loading;
import com.example.bemyfriend.screens.Login;
import com.example.bemyfriend.screens.MyProfile;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Random;

public class Helper {
    private static Random rg= new Random();
    private static boolean musicOn= false;
    //create chat name for firebase
    public static String FlushName(User otherUser, User thisUser) {
        String name = AlphabetOrder(otherUser.getParentName(), thisUser.getParentName());
        String childName = AlphabetOrder(otherUser.getChildName(), thisUser.getChildName());
        String s = name + childName + (otherUser.getAge() + thisUser.getAge());
        return s;
    }
    public static boolean IsMusicON(){
        return musicOn;
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
    public static Intent MenuSelect(MenuItem item, Activity activity,
                                    MenuItem startMusic, MenuItem stopMusic){
        if (item.getItemId() == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            return new Intent(activity, Login.class);
        }
        if (item.getItemId() == R.id.menu_chats) {
            return new Intent(activity, Chats.class);
        }
        if (item.getItemId() == R.id.menu_find_new_friends) {
            return new Intent(activity, FindNewFriends.class);
        }
        if (item.getItemId() == R.id.menu_my_profile) {
            return new Intent(activity, MyProfile.class);
        }

        if (item.getItemId() == R.id.menu_start_music) {
            Intent intent = new Intent(activity, TrackPlayer.class);
            activity.startService(intent);
            startMusic.setVisible(false);
            stopMusic.setVisible(true);
            musicOn= true;
        }
        if (item.getItemId() == R.id.menu_stop_music) {
            Intent intent = new Intent(activity, TrackPlayer.class);
            activity.stopService(intent);
            stopMusic.setVisible(false);
            startMusic.setVisible(true);
            musicOn= false;
        }
        return null;
    }
    //TODO:make this work goot
    public static void SendNotification(Context context){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, Loading.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Be My Friend")
                .setContentText("יש לך הודעות שלא נקראו")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
}
