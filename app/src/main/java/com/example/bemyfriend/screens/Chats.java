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
import com.example.bemyfriend.utils.Helper;


public class Chats extends AppCompatActivity implements ChatAdapter.RecyclerViewClickListener {

    private Toolbar toolbar;
    private ChatAdapter.RecyclerViewClickListener listener;
    private ChatAdapter chatAdapter;
    private ChatsPresenter presenter;
    private MenuItem startMusic, stopMusic;

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
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        menu.findItem(R.id.menu_chats).setVisible(false);
        menu.findItem(R.id.menu_delete_chat).setVisible(false);
        menu.findItem(R.id.menu_find_new_friends).setVisible(true);
        menu.findItem(R.id.menu_logout).setVisible(true);
        menu.findItem(R.id.menu_my_profile).setVisible(true);
        startMusic= menu.findItem(R.id.menu_start_music);
        stopMusic= menu.findItem(R.id.menu_stop_music);
        if(Helper.IsMusicON()){
            stopMusic.setVisible(true);
            startMusic.setVisible(false);
        }
        else {
            stopMusic.setVisible(false);
            startMusic.setVisible(true);
        }
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

        Intent intent= Helper.MenuSelect(item, this, startMusic, stopMusic);
        if(intent!=null){
            startActivity(intent);
            finish();
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