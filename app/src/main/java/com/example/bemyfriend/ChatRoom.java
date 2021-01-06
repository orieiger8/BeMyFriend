package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity  implements newFriendAdapter.OnListListener, Observer{
    private Toolbar toolbar;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private User otherUser, thisUser;
    private String partnerMail;
    private String flushName;
    private boolean alreadyResetToZero= false;
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //connect to database with DB
        db= DB.getInstance();

        //get my user
        thisUser= db.getMyUser();

        //start the attach
        db.attachChat(this);

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("צ'אט");

        //get the partner mail
        partnerMail = getIntent().getStringExtra("mail");

        //find the users at the firebase
        otherUser= db.getPartnerName(partnerMail);

        //get the chat name in firebase
        flushName= FlushName(thisUser,otherUser);

        //get the send button
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        //put listener on send button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                String message= input.getText().toString();

                //put message in firebase in the chat
                db.sendMessage(flushName,message);
                //add last message for my in firebase
                thisUser.addMyMessage(partnerMail,message);
                db.updateUserInDB(thisUser);
                //add last message for the partner in firebase
                otherUser.addMessage(thisUser.getMail(), message);
                db.updateOtherUserInDB(otherUser);

                // Clear the input
                input.setText("");
            }
        });
        //put partner information in the activity
        ImageView icon= findViewById(R.id.imageView3);
        TextView name= findViewById(R.id.textView2);
        name.setText(otherUser.getChildName() + " (" + otherUser.getParentName() + ")");
        icon.setImageResource(name.getResources().getIdentifier(otherUser.getPicId(),
                "drawable", name.getContext().getPackageName()));
        CreateChatMessages();
    }


    @Override
    protected void onStop() {
        super.onStop();
        //reset unread messages
        thisUser.setUnreedMassegesToZero(otherUser.getMail());
        db.updateUserInDB(thisUser);
    }

    public void CreateChatMessages() {
        //create recycler view layout
        RecyclerView listOfMessages = findViewById(R.id.messageRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        listOfMessages.setLayoutManager(layoutManager);

        //get all messages on this chat
        messages = db.getChatMessages(flushName);

        //create recycler view
        ChatMessageAdapter adapter = new ChatMessageAdapter(messages, thisUser.getMail());
        adapter.setMessages(messages);
        adapter.notifyDataSetChanged();
        listOfMessages.setAdapter(adapter);
    }


    @Override
    public void onListClick(int position) {

    }
    //create chat name for firebase
    private String FlushName(User user1, User thisUser) {
        String name = AlphabetOrder(user1.getParentName(),thisUser.getParentName());
        String childName = AlphabetOrder(user1.getChildName(),thisUser.getChildName());
        String s = name + childName + (user1.getAge()+thisUser.getAge());
        return s;

    }
    //sort the names by alphabetic order
    private String AlphabetOrder(String parentName, String parentName1) {
        int i;
        for (i = 0; i < parentName.length() && i<parentName1.length(); i++) {
            if (parentName.charAt(i) != parentName1.charAt(i)) {
                if (parentName.charAt(i) < parentName1.charAt(i))
                    return parentName + parentName1;
                else
                    return parentName1 + parentName;
            }
        }
        if (i == parentName.length()) return parentName + parentName1;
        else return parentName1 + parentName;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuchat_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menuchat_myprofile) {
            startActivity(new Intent(this, MyProfile.class));
            finish();
        }
        if (item.getItemId() == R.id.menuchat_chats) {
            startActivity(new Intent(this, Chats.class));
            finish();
        }
        if (item.getItemId() == R.id.menuchat_findnewfriends) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if (item.getItemId() == R.id.menuchat_deletechat) {

            thisUser.deleteChat(partnerMail);
            db.updateUserInDB(thisUser);
            otherUser.deleteChat(thisUser.getMail());
            db.updateOtherUserInDB(otherUser);
            db.deleteChat(flushName);



            startActivity(new Intent(this, Chats.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update() {
        CreateChatMessages();
    }
}