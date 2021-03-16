package com.example.bemyfriend.screens;

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

import com.example.bemyfriend.R;
import com.example.bemyfriend.adapter.ChatAdapter;
import com.example.bemyfriend.presenter.ChatsPresenter;
import com.example.bemyfriend.utils.AudioPlayer;
import com.google.firebase.auth.FirebaseAuth;


public class Chats extends AppCompatActivity implements ChatAdapter.RecyclerViewClickListener {

    private Toolbar toolbar;
    private ChatAdapter.RecyclerViewClickListener listener;
    private ChatAdapter chatAdapter;
    private ChatsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.chats);

        presenter = new ChatsPresenter(this);

        //start create the recycler view
        final RecyclerView recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //put listener on recycler view
        listener = new ChatAdapter.RecyclerViewClickListener() {
            @Override
            public void OnClick(int position) {
                //move to the chosen chat
                Intent intent = new Intent(Chats.this, ChatRoom.class);
                intent.putExtra("mail", presenter.getChats().get(position).getMail());
                startActivity(intent);
                finish();
            }
        };

        //create new list with the chats that the user has
        if (!presenter.chatsExist()) {
            //if there are no chats. move to find new friends
            startActivity(new Intent(Chats.this, FindNewFriends.class));
            finish();
        }
        //create recycler view
        chatAdapter = new ChatAdapter(presenter.getChats(), listener);
        recyclerView.setAdapter(chatAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chats_list, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final android.widget.SearchView searchBar = (android.widget.SearchView) searchViewItem.getActionView();
        searchBar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBar.clearFocus();

                /*if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // filtering and replacing
                filterChats(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void filterChats(String newText) {
        chatAdapter.setChats(presenter.filter(newText));
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_find_new_friends_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_find_new_friends_myprofile) {
            startActivity(new Intent(this, MyProfile.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_room_findnewfriends) {
            startActivity(new Intent(this, ChatRoom.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_chat_music) {
            if(AudioPlayer.getInstance().isPlaying()){
                AudioPlayer.getInstance().stop();
            }
            else {
                AudioPlayer.getInstance().play(this, R.raw.never_gonna_give_you_up);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void moveToMain(View view) {
        //move to chats page
        startActivity(new Intent(Chats.this, FindNewFriends.class));
        finish();
    }

    @Override
    public void OnClick(int position) {
        //chats.get(position);
    }
}