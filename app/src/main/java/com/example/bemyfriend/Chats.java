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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class Chats extends AppCompatActivity implements ChatAdapter.RecyclerViewClickListener {

    private Toolbar toolbar;
    Random rg = new Random();
    private  FirebaseAuth mAuth;
    private ArrayList<Chat> chats = new ArrayList<>();
    private ArrayList<Chat> finalChats = new ArrayList<>();
    private ChatAdapter.RecyclerViewClickListener listener;
    private User thisUser, val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        mAuth = FirebaseAuth.getInstance();

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("צ'אטים");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        final RecyclerView recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myRef.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                finalChats.clear();

                //find the user
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    val = currentUser.getValue(User.class);
                    if (val.getMail().equals(mAuth.getCurrentUser().getEmail())) {
                        thisUser = val;
                        break;
                    }
                }
                //create chats with all the users
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    val = currentUser.getValue(User.class);
                    if (!val.getMail().equals(mAuth.getCurrentUser().getEmail()))
                        createChat(val);
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
                if (thisUser.getChats().size() != 0) {
                    for (int i = 0; i < chats.size(); i++) {
                        for (int j = 0; j < thisUser.getChats().size(); j++) {
                            if (thisUser.getChats().get(j).getMail().equals(chats.get(i).getMail())) {
                                finalChats.add(chats.get(i));
                            }
                        }
                    }
                } else {
                    //if there are no chats. move to find new friends
                    startActivity(new Intent(Chats.this, MainActivity.class));
                    finish();
                }
                ChatAdapter chatAdapter = new ChatAdapter(finalChats, listener);
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createChat(User val){
        String time = "",text = "";
        int unreadMessages = 0;
        Chat c = new Chat(new Date().getTime(),
                val.getChildName() + " (" + val.getParentName() + ")",
                val.getMail(), val.getPicId(), unreadMessages, val.getMail());
        for (int i = 0; i < thisUser.getChats().size(); i++) {
            if (thisUser.getChats().get(i).getMail().equals(val.getMail())) {
                c.setUnreadMessages(thisUser.getChats().get(i).getUnreadMessages());
                c.setText("" + thisUser.getChats().get(i).getText());
                c.setTime(thisUser.getChats().get(i).getTime());
            }
        }
        chats.add(c);
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