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
import com.example.bemyfriend.utils.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity implements NewFriendAdapter.OnListListener {
    private Toolbar toolbar;
    private ChatRoomPresenter presenter;
    private MenuItem startMusic, stopMusic;

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

                if (presenter.isNetConnected()) {
                    // Clear the input
                    input.setText("");
                }
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
        getMenuInflater().inflate(R.menu.menu, menu);

        //TODO: make this look less ugly
        menu.findItem(R.id.menu_chats).setVisible(true);
        menu.findItem(R.id.menu_delete_chat).setVisible(true);
        menu.findItem(R.id.menu_find_new_friends).setVisible(true);
        menu.findItem(R.id.menu_logout).setVisible(true);
        menu.findItem(R.id.menu_my_profile).setVisible(true);
        menu.findItem(R.id.app_bar_search).setVisible(false);
        startMusic = menu.findItem(R.id.menu_start_music);
        stopMusic = menu.findItem(R.id.menu_stop_music);
        if (Helper.IsMusicON()) {
            stopMusic.setVisible(true);
            startMusic.setVisible(false);
        } else {
            stopMusic.setVisible(false);
            startMusic.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = Helper.MenuSelect(item, this, startMusic, stopMusic);
        if (intent != null) {
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.menu_delete_chat) {
            presenter.deleteChat();

            startActivity(new Intent(this, Chats.class));
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public void setNetConnection(boolean bool) {
        presenter.setNetConnected(bool);
    }
}