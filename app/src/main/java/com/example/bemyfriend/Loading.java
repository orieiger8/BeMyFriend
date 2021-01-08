package com.example.bemyfriend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Loading extends AppCompatActivity implements Observer {

    private boolean isLoggedIn;
    private ProgressBar progressBar;
    private TextView progressText;
    private int progressPercentage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        isLoggedIn = mAuth.getCurrentUser() != null;

        //connect to database with DB
        DB db = DB.getInstance(this);

        // Progress bar
        progressBar = findViewById(R.id.imageView_progressBar);
        progressText = findViewById(R.id.textView_progress);
    }

    public void update() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressPercentage <= 100) {
                    progressText.setText(progressPercentage + "%");
                    progressBar.setProgress(progressPercentage);
                    progressPercentage++;
                    handler.postDelayed(this,0);
                } else {
                    handler.removeCallbacks(this);
                    if (isLoggedIn) {
                        // move to main page
                        startActivity(new Intent(Loading.this, Chats.class));
                    } else {
                        // move to login page
                        startActivity(new Intent(Loading.this, Login.class));
                    }
                    finish();
                }
            }
        },0);
    }
}