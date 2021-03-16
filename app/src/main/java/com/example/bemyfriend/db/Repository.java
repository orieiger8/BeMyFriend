package com.example.bemyfriend.db;

import com.example.bemyfriend.model.Chat;
import com.example.bemyfriend.model.ChatMessage;
import com.example.bemyfriend.model.User;
import com.example.bemyfriend.utils.Observer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    // private vars
    private static boolean initialized;
    private DataSnapshot usersSnap;
    private DataSnapshot chatSnap;
    private User thisUser;
    private String userKey;
    private FirebaseDatabase database;

    // ctor
    private Repository() {
        initialized = false;

        // get DB references and add event listeners to detect DB changes
        database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                usersSnap = dataSnapshot;

                if (!initialized)
                    setConnectionState(true);

                updateMyUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        usersRef = database.getReference("chats");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                chatSnap = dataSnapshot;

                notifyAllObserversChat();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }


    public void LoggedIn() {
        updateMyUser();
    }

    private void updateMyUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            String userEmail = cUser.getEmail();

            for (DataSnapshot currentUser : usersSnap.getChildren()) {
                User val = currentUser.getValue(User.class);

                if (val.getMail().equals(userEmail)) {
                    thisUser = val;
                    userKey = currentUser.getKey();
                    break;
                }
            }
        }
    }

    public User getMyUser() {
        return thisUser;
    }

    public void updateUserInDB(User myUser) {
        //update my user in realtime database
        DatabaseReference userRef = database.getReference("users/" + userKey);
        userRef.setValue(thisUser);
    }

    public void updateOtherUserInDB(User otherUser) {
        //find the other user
        for (DataSnapshot currentUser : usersSnap.getChildren()) {
            User val = currentUser.getValue(User.class);

            //update the user in realtime data base
            if (val.getMail().equals(otherUser.getMail())) {
                database.getReference("users/" + currentUser.getKey()).setValue(otherUser);
            }
        }
    }

    public ArrayList<User> getAllUsersExceptMe() {
        ArrayList<User> users = new ArrayList<>();

        //find all users except my user
        for (DataSnapshot currentUser : usersSnap.getChildren()) {
            User val = currentUser.getValue(User.class);

            if (!val.getMail().equals(thisUser.getMail())) {
                users.add(val);
            }
        }
        //return list of all users
        return users;
    }


    public ArrayList<Chat> getAllUsersChatsExceptMe() {
        ArrayList<Chat> chats = new ArrayList<>();

        //find all users except my user
        for (DataSnapshot currentUser : usersSnap.getChildren()) {
            User val = currentUser.getValue(User.class);

            //create chat with the user and put him in the list
            if (!val.getMail().equals(thisUser.getMail())) {
                Chat c = new Chat(0, val.getChildName() + " (" + val.getParentName() + ")",
                        " ", val.getPicId(), 0, val.getMail());
                for (int i = 0; i < thisUser.getChats().size(); i++) {
                    if (thisUser.getChats().get(i).getMail().equals(val.getMail())) {
                        c.setUnreadMessages(thisUser.getChats().get(i).getUnreadMessages());
                        c.setLastMessage("" + thisUser.getChats().get(i).getLastMessage());
                        c.setTime(thisUser.getChats().get(i).getTime());
                    }
                }
                chats.add(c);
            }
        }
        return chats;
    }

    public void registerNewUserToFireBase(User newUser) {
        //register new user
        usersSnap.getRef().push().setValue(newUser);
    }

    public User getPartnerName(String otherMail) {
        //find partner name by his mail
        for (DataSnapshot currentUser : usersSnap.getChildren()) {
            User val = currentUser.getValue(User.class);
            if (val.getMail().equals(otherMail)) {
                return val;
            }
        }
        return null;
    }


    public ArrayList<ChatMessage> getChatMessages(String flushName) {
        ArrayList<ChatMessage> messages = new ArrayList<>();

        //get all messages from the chat which his name is the flushName
        for (DataSnapshot currentUser : chatSnap.getChildren()) {
            if (currentUser.getKey().equals(flushName)) {
                for (DataSnapshot message : currentUser.getChildren()) {
                    ChatMessage val = message.getValue(ChatMessage.class);
                    messages.add(val);
                }

                break;
            }
        }
        return messages;
    }

    public boolean mailExist(String mail) {
        for (DataSnapshot currentUser : usersSnap.getChildren()) {
            User val = currentUser.getValue(User.class);
            if (val.getMail().equals(mail)) {
                return true;
            }
        }
        return false;
    }

    public void sendMessage(String flushName, String message) {
        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database
        database.getReference("chats/" + flushName).push().setValue(new ChatMessage(message, thisUser.getMail()));
    }

    public void createChat(String flushName) {
        //start new chat in realtime data base
        database.getReference("chats/" + flushName).push()
                .setValue(new ChatMessage("שלום", thisUser.getMail()));
    }

    public void deleteChat(String flushName) {
        //delete chat from realtime data base
        database.getReference("chats/" + flushName).setValue(null);
    }

    public boolean userChanged(User otherUser){
        if(thisUser.equals(otherUser))
            return false;
        return true;
    }


    /*----------------- SINGLETON STUFFY STUFF -----------------*/
    private static Repository _instance;

    public static Repository getInstance() {
        if (_instance == null) {
            InitializeConnection();
        }

        return _instance;
    }

    public static Repository getInstance(Observer observer) {
        attach(observer);
        return getInstance();
    }

    public static void InitializeConnection() {
        _instance = new Repository();
    }

    /*----------------- OBSERVER STUFFY STUFF -----------------*/
    private static List<Observer> observers = new ArrayList<Observer>();
    private static List<Observer> observersChat = new ArrayList<>();


    public static boolean getConnectionState() {
        return _instance != null && initialized;
    }

    private void setConnectionState(boolean state) {
        this.initialized = state;
        notifyAllObservers();
    }

    public static void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void attachChat(Observer observer) {
        observersChat.add(observer);
    }

    public void notifyAllObserversChat() {
        for (Observer observer : observersChat) {
            observer.update();
        }
    }
}
