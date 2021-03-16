package com.example.bemyfriend.screens;

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

import com.example.bemyfriend.R;
import com.example.bemyfriend.adapter.ChatMessageAdapter;
import com.example.bemyfriend.adapter.NewFriendAdapter;
import com.example.bemyfriend.model.ChatMessage;
import com.example.bemyfriend.presenter.ChatRoomPresenter;
import com.example.bemyfriend.utils.AudioPlayer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity implements NewFriendAdapter.OnListListener {
    private Toolbar toolbar;
    private ChatRoomPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.chat);

        //get the partner mail
        String partnerMail = getIntent().getStringExtra("mail");

        presenter = new ChatRoomPresenter(this, partnerMail);


        //get the send button
        FloatingActionButton fab =
                (FloatingActionButton) findViewById(R.id.fab);

        //put listener on send button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.input);
                String message = input.getText().toString();

                presenter.sendMessage(message);

                // Clear the input
                input.setText("");
            }
        });
        //put partner information in the activity
        ImageView icon = findViewById(R.id.imageView3);
        TextView name = findViewById(R.id.textView2);
        name.setText(presenter.getOtherUser().getChildName() + " (" + presenter.getOtherUser().getParentName() + ")");
        icon.setImageResource(name.getResources().getIdentifier(presenter.getOtherUser().getPicId(),
                "drawable", name.getContext().getPackageName()));
        CreateChatMessages();
    }


    @Override
    protected void onStop() {
        super.onStop();
        presenter.resentUnreadMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.resentUnreadMessages();
    }

    public void CreateChatMessages() {
        //create recycler view layout
        RecyclerView listOfMessages = findViewById(R.id.messageRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        listOfMessages.setLayoutManager(layoutManager);

        ArrayList<ChatMessage> messages = presenter.getMessages();
        //create recycler view
        ChatMessageAdapter adapter = new ChatMessageAdapter(messages, presenter.getThisUser().getMail());
        adapter.setMessages(messages);
        adapter.notifyDataSetChanged();
        listOfMessages.setAdapter(adapter);
    }


    @Override
    public void onListClick(int position) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_chat_room_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_room_myprofile) {
            startActivity(new Intent(this, MyProfile.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_room_chats) {
            startActivity(new Intent(this, Chats.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_room_findnewfriends) {
            startActivity(new Intent(this, FindNewFriends.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_room_deletechat) {
            presenter.deleteChat();

            startActivity(new Intent(this, Chats.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_room_music) {
            if(AudioPlayer.getInstance().isPlaying()){
                AudioPlayer.getInstance().stop();
            }
            else {
                AudioPlayer.getInstance().play(this, R.raw.never_gonna_give_you_up);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}