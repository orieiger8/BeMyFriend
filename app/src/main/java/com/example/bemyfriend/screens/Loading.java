package com.example.bemyfriend.screens;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
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
    private Bitmap[] animation;
    private ImageView currentFrame;
    private double currentImageIndex = 0;
    private int numberOfFrames=8;
    //TODO: find animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // start update service
        //startService(new Intent(this, NotificationMan.class));

        //
        currentProgress = 0;

        createNotificationChannel();

        presenter = new LoadingPresenter(this);

        // Progress bar
        progressBar = findViewById(R.id.imageView_progressBar);
        progressText = findViewById(R.id.textView_progress);

        currentFrame = findViewById(R.id.imageViewAnimation);
        animation = new Bitmap[numberOfFrames];
        for (int i = 0; i < numberOfFrames; i++) {
            animation[i] = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("animation" + i, "drawable", getPackageName()));
        }

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

                    //animation
                    currentImageIndex += 1;
                    currentImageIndex %= numberOfFrames;
                    currentFrame.setImageBitmap(animation[(int) currentImageIndex]);

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

    public void tryAgain(View view) {
        findViewById(R.id.button_try_again).setVisibility(View.GONE);
        presenter.connectToDB();
    }

    public void setNetConnection(boolean b) {
        presenter.setNetConnected(b);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "getString(R.string.channel_name)";
            String description = "getString(R.string.channel_description)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}