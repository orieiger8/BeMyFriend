package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class loading extends AppCompatActivity implements Observer {

    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        isLoggedIn = mAuth.getCurrentUser() != null;

        //connect to database with DB
        DB db = DB.getInstance(this);
    }

    public void update() {
        if (isLoggedIn) {
            // move to main page
            startActivity(new Intent(loading.this, Chats.class));
        }
        else {
            // move to login page
            startActivity(new Intent(loading.this, Login.class));
        }

        finish();
    }
}