package com.example.bemyfriend.presenter;

import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.ChatMessage;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.screens.ChatRoom;
import com.example.bemyfriend.utils.Helper;
import com.example.bemyfriend.utils.Observer;

import java.util.ArrayList;

public class ChatRoomPresenter implements Observer {
    private ChatRoom activity;
    private Repository repository;
    private User otherUser, thisUser;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private String partnerMail, flushName;

    public ChatRoomPresenter(ChatRoom activity, String partnerMail) {
        this.activity = activity;
        //connect to database with DB
        repository = Repository.getInstance();

        //get my user
        thisUser = repository.getMyUser();

        //start the attach
        repository.attachChat(this);

        //find the users at the firebase
        this.partnerMail = partnerMail;
        otherUser = repository.getPartnerName(this.partnerMail);

        //get the chat name in firebase
        flushName = Helper.FlushName(thisUser, otherUser);

        //reset unread messages
        thisUser.setUnreedMessagesToZero(otherUser.getMail());
        repository.updateUserInDB(thisUser);

    }

    public void sendMessage(String message) {
        //put message in firebase in the chat
        repository.sendMessage(flushName, message);
        //add last message for my in firebase
        thisUser.addMyMessage(partnerMail, message);
        repository.updateUserInDB(thisUser);
        //add last message for the partner in firebase
        otherUser.addMessage(thisUser.getMail(), message);
        repository.updateOtherUserInDB(otherUser);

    }

    public User getOtherUser() {
        return otherUser;
    }

    public void resentUnreadMessages() {
        //reset unread messages
        thisUser.setUnreedMessagesToZero(otherUser.getMail());
        repository.updateUserInDB(thisUser);
    }

    public ArrayList<ChatMessage> getMessages() {
        //get all messages on this chat
        messages = repository.getChatMessages(flushName);

        return messages;
    }

    public User getThisUser() {
        return thisUser;
    }

    public void deleteChat() {
        thisUser.deleteChat(partnerMail);
        repository.updateUserInDB(thisUser);
        otherUser.deleteChat(thisUser.getMail());
        repository.updateOtherUserInDB(otherUser);
        repository.deleteChat(flushName);
    }

    @Override
    public void update() {
        activity.CreateChatMessages();
    }
}
