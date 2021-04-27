package com.example.bemyfriend.presenter;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.Chat;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.Chats;
import com.example.bemyfriend.utils.NotificationWorker;
import com.example.bemyfriend.utils.Observer;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatsPresenter implements Observer {
    private Chats activity;
    private User thisUser;
    private Repository repository;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Chat> finalChats = new ArrayList<>();


    public ChatsPresenter(Chats activity) {
        this.activity = activity;
        //connect to database with DB
        repository = Repository.getInstance();
        //get my user
        thisUser = repository.getMyUser();
        //get all chats
        chats = createChatList();

        doNotificationService();
    }

    public ArrayList<Chat> createChatList(){
        chats = repository.getAllUsersChatsExceptMe();
        deleteUnmatchedChats();
        orderByTime();
        moveUnreadChatsUp();
        return chats;
    }

    private void moveUnreadChatsUp() {
        for (int i = 0; i < chats.size(); i++) {
            if (chats.get(i).getUnreadMessages() != 0) {
                finalChats.add(chats.get(i));
                chats.remove(i);
                i--;
            }
        }
        for (int i = 0; i < chats.size(); i++) {
            finalChats.add(chats.get(i));
        }
        chats.clear();
        for (int i = 0; i < finalChats.size(); i++) {
            chats.add(finalChats.get(i));
        }
        finalChats.clear();
    }

    public void deleteUnmatchedChats() {
        //delete all chats that the user doesn't have
        if (thisUser.getChats().size() != 0) {
            for (int i = 0; i < chats.size(); i++) {
                for (int j = 0; j < thisUser.getChats().size(); j++) {
                    if (thisUser.getChats().get(j).getMail().equals(chats.get(i).getMail())) {
                        finalChats.add(chats.get(i));
                    }
                }
            }
            chats.clear();
        }
    }

    public void orderByTime() {
        //order by time
        Chat temp;
        while (!finalChats.isEmpty()) {
            int newChat = 0;
            temp = finalChats.get(0);
            for (int i = 0; i < finalChats.size(); i++) {
                if (temp.getTime() < finalChats.get(i).getTime()) {
                    temp = finalChats.get(i);
                    newChat = i;
                }
            }
            chats.add(temp);
            finalChats.remove(newChat);
        }
        finalChats.clear();
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public boolean chatsExist() {
        if (thisUser.getChats().size() == 0) {
            return false;
        }
        return true;
    }

    public ArrayList<Chat> filter(String newText) {
        if (newText.equals("")) {
            return chats;
        } else {
            ArrayList<Chat> finalChat = new ArrayList<>();
            for (int i = 0; i < chats.size(); i++) {
                String search = "";
                for (int j = 0; j < newText.length(); j++) {
                    search = search + chats.get(i).getName().charAt(j);
                }
                if (newText.equals(search))
                    finalChat.add(chats.get(i));
            }
            return finalChat;
        }
    }


    public UUID doNotificationService() {
        WorkManager wm = WorkManager.getInstance(activity);
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                NotificationWorker.class,15, TimeUnit.MINUTES)
                .build();

        wm.enqueueUniquePeriodicWork("notification", ExistingPeriodicWorkPolicy.KEEP,
                request);

//        WorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
//
//        wm.enqueue(request);

        return request.getId();
    }

    @Override
    public void update() {
        activity.updateList();
    }
}
