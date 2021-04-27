package com.example.bemyfriend.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.bemyfriend.screens.ChatRoom;
import com.example.bemyfriend.screens.FindNewFriends;
import com.example.bemyfriend.screens.Loading;

public class InternetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMan = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connMan.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            // there is internet yay! :P UwU
            //for ChatRoom
            if (context instanceof ChatRoom) {
                ChatRoom cr = (ChatRoom)context;
                cr.setNetConnection(true);
            }
            //for Loading
            if (context instanceof Loading) {
                Loading loading = (Loading)context;
                loading.setNetConnection(true);
            }
            //for Find new friends
            if (context instanceof FindNewFriends) {
                FindNewFriends fnf = (FindNewFriends)context;
                fnf.setNetConnection(true);
            }
        } else {
            // there is no internet
            //for ChatRoom
            if (context instanceof ChatRoom) {
                ChatRoom cr = (ChatRoom)context;
                cr.setNetConnection(false);
            }
            //for Loading
            if (context instanceof Loading) {
                Loading loading = (Loading)context;
                loading.setNetConnection(false);
            }
            //for Find new friends
            if (context instanceof FindNewFriends) {
                FindNewFriends fnf = (FindNewFriends)context;
                fnf.setNetConnection(false);
            }
        }
    }
}
