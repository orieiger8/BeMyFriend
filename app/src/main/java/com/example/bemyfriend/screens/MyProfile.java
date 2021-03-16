package com.example.bemyfriend.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bemyfriend.R;
import com.example.bemyfriend.presenter.MyProfilePresenter;
import com.example.bemyfriend.utils.Helper;

public class MyProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private MyProfilePresenter presenter;
    private MenuItem startMusic,stopMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //set the toolbar
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.my_profile);

        presenter= new MyProfilePresenter(this);
    }

    public void moveToEditProfile(View view) {
        startActivity(new Intent(this, EditProfile.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menu_chats).setVisible(true);
        menu.findItem(R.id.menu_delete_chat).setVisible(false);
        menu.findItem(R.id.menu_find_new_friends).setVisible(true);
        menu.findItem(R.id.menu_logout).setVisible(true);
        menu.findItem(R.id.menu_my_profile).setVisible(false);
        menu.findItem(R.id.app_bar_search).setVisible(false);
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
        return super.onCreateOptionsMenu(menu);
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
}