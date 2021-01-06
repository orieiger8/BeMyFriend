package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Random;


public class Chats extends AppCompatActivity implements ChatAdapter.RecyclerViewClickListener {

    private Toolbar toolbar;
    Random rg = new Random();
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Chat> finalChats = new ArrayList<>();
    private ChatAdapter.RecyclerViewClickListener listener;
    private User thisUser, val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        DB db = DB.getInstance();

        thisUser = db.getMyUser();
        chats = db.getAllUsersChatsExceptMe();


        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("צ'אטים");

        final RecyclerView recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Chat> finalChats = new ArrayList<>();
        if (thisUser.getChats().size() != 0) {
            for (int i = 0; i < chats.size(); i++) {
                for (int j = 0; j < thisUser.getChats().size(); j++) {
                    if (thisUser.getChats().get(j).getMail().equals(chats.get(i).getMail())) {
                        finalChats.add(chats.get(i));
                    }
                }
            }
            chats.clear();
            for(int i=0; i<finalChats.size(); i++){
                chats.add(finalChats.get(i));
            }
            //order by time and unread message
            finalChats.clear();
            Chat temp= null;
            for(int i=0; i<chats.size(); i++) {
                int newChat= 0;
                for(int j=0;j<chats.size();j++) {
                    if(chats.get(i).getTime()<chats.get(i).getTime()){
                        temp= chats.get(j);
                        newChat=j;
                    }
                }
                finalChats.add(temp);
                chats.remove(newChat);
                i--;
            }
            for(int i=0; i<finalChats.size(); i++) {
                if (finalChats.get(i).getUnreadMessages() > 0) {
                    chats.add(finalChats.get(i));
                    finalChats.remove(i);
                    i--;
                }
            }
            for(int i=0; i<finalChats.size(); i++) {
                chats.add(finalChats.get(i));
            }
            finalChats.clear();;
        }

        listener = new ChatAdapter.RecyclerViewClickListener() {
            @Override
            public void OnClick(int position) {
                //move to the chosen chat
                Intent intent = new Intent(Chats.this, ChatRoom.class);
                intent.putExtra("mail", chats.get(position).getMail());
                startActivity(intent);
                finish();
            }
        };

        //create new list with the chats that the user has
        if (thisUser.getChats().size() == 0) {
            //if there are no chats. move to find new friends
            startActivity(new Intent(Chats.this, MainActivity.class));
            finish();
        }
        ChatAdapter chatAdapter = new ChatAdapter(chats, listener);
        recyclerView.setAdapter(chatAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_myprofile){
            startActivity(new Intent(this, MyProfile.class));
            finish();
        }
        if (item.getItemId() == R.id.menuchat_findnewfriends){
            startActivity(new Intent(this, ChatRoom.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveToMain(View view) {
        startActivity(new Intent(Chats.this,MainActivity.class));
        finish();
    }

    @Override
    public void OnClick(int position) {
        //chats.get(position);
    }
}