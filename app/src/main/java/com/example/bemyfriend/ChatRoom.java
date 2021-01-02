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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity  implements newFriendAdapter.OnListListener{
    private Toolbar toolbar;
    private ArrayList<ChatMessage> messages = new ArrayList<>();
    private FirebaseAuth mAuth;
    private User otherUser, thisUser;
    private String sessionId;
    private String flushName;
    private boolean alreadyResetToZero= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        mAuth = FirebaseAuth.getInstance();

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("צ'אט");

        //get the partner mail
        sessionId = getIntent().getStringExtra("mail");

        //find the users at the firebase
        FindUsers();

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance().getReference("chats/" + flushName).push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        );

                //add last message for my
                thisUser.addMyMessage(sessionId,input.getText().toString());
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference userRef = database.getReference("users/" + thisUser.getDataSnapshot());
                userRef.setValue(thisUser);

                //add last message for the partner
                otherUser.addMessage(mAuth.getCurrentUser().getEmail(), input.getText().toString());
                userRef = database.getReference("users/" + otherUser.getDataSnapshot());
                userRef.setValue(otherUser);

                // Clear the input
                input.setText("");
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        //reset unread messages
        thisUser.setUnreedMassegesToZero(otherUser.getMail());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + thisUser.getDataSnapshot());
        myRef.setValue(thisUser);
    }

    private void FindUsers() {
        TextView textView = findViewById(R.id.textView2);
        ImageView imageView = findViewById(R.id.imageView3);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    User val = currentUser.getValue(User.class);
                    if (val.getMail().equals(mAuth.getCurrentUser().getEmail())){
                        thisUser = val;
                    }
                    if (sessionId.equals(val.getMail())) {
                        otherUser = val;
                        textView.setText(val.getChildName() + " (" + val.getParentName() + ")");
                        imageView.setImageResource(textView.getResources().getIdentifier(val.getPicId(),
                                "drawable", textView.getContext().getPackageName()));
                    }
                }

                flushName = FlushName(thisUser, otherUser);
                CreateChatMessages();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        alreadyResetToZero= true;

    }

    public void CreateChatMessages(){
        if(alreadyResetToZero) {
            //reset unread messages
            thisUser.setUnreedMassegesToZero(otherUser.getMail());
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + thisUser.getDataSnapshot());
            myRef.setValue(thisUser);
            alreadyResetToZero=false;
        }
        RecyclerView listOfMessages =  findViewById(R.id.messageRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        listOfMessages.setLayoutManager(layoutManager);

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database2.getReference("chats/" + flushName);

        final ChatMessageAdapter adapter = new ChatMessageAdapter(messages, thisUser.getMail());

        myRef2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //add messages from firebase
                messages.clear();
                for (DataSnapshot currentUser: dataSnapshot.getChildren()) {
                    ChatMessage val = currentUser.getValue(ChatMessage.class);
                    ChatMessage c = new ChatMessage(
                            val.getMessageText(),
                            val.getMessageUser(),
                            val.getMessageTime()
                    );
                    messages.add(c);
                }
                adapter.setMessages(messages);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

            //delete chat from firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            thisUser.deleteChat(sessionId);
            DatabaseReference userRef = database.getReference("users/" + thisUser.getDataSnapshot());
            userRef.setValue(thisUser);
            otherUser.deleteChat(thisUser.getMail());
            userRef = database.getReference("users/" + otherUser.getDataSnapshot());
            userRef.setValue(otherUser);

            DatabaseReference temp = FirebaseDatabase.getInstance().getReference("chats/" + flushName);
            temp.setValue(null);

            startActivity(new Intent(this, Chats.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}