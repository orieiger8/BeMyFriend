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
import com.example.bemyfriend.utils.AudioPlayer;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfile extends AppCompatActivity {

    private Toolbar toolbar;
    private MyProfilePresenter presenter;

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
        getMenuInflater().inflate(R.menu.menu_myprofile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_myprofile_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_myprofile_chats) {
            startActivity(new Intent(this, Chats.class));
            finish();
        }
        if (item.getItemId() == R.id.menu_myprofile_findnewfriends) {
            startActivity(new Intent(this, FindNewFriends.class));
            finish();
        }
        if(item.getItemId()== R.id.menu_myprofile_music){
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