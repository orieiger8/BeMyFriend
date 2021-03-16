package com.example.bemyfriend.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bemyfriend.R;
import com.example.bemyfriend.presenter.LoadingPresenter;

public class Loading extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressText;
    private LoadingPresenter presenter;
    private int currentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        currentProgress = 0;

        presenter = new LoadingPresenter(this);

        // Progress bar
        progressBar = findViewById(R.id.imageView_progressBar);
        progressText = findViewById(R.id.textView_progress);
    }

    public void updateProgressBar(int percent) {
        progressTo(percent);
    }

    private void progressTo(int percent) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int targetPercent = percent;
                if (targetPercent > 100)
                    targetPercent = 100;

                if (currentProgress <= targetPercent) {
                    // change percentage
                    progressText.setText(currentProgress + "%");
                    progressBar.setProgress((int) currentProgress);
                    currentProgress++;

                    // callback to self to "loop"
                    handler.postDelayed(this, 0);
                } else {
                    // reached target percent - end loop
                    handler.removeCallbacks(this);

                    if (presenter.isLoggedIn()) {
                        // move to main page
                        startActivity(new Intent(Loading.this, Chats.class));
                    } else {
                        // move to login page
                        startActivity(new Intent(Loading.this, Login.class));
                    }
                    finish();
                }
            }
        }, 0);
    }
}