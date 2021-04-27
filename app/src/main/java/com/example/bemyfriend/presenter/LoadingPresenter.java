package com.example.bemyfriend.presenter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.screens.Loading;
import com.example.bemyfriend.utils.InternetBroadcastReceiver;
import com.example.bemyfriend.utils.Observer;
import com.google.firebase.auth.FirebaseAuth;

public class LoadingPresenter implements Observer {
    private Loading activity;
    private boolean isLoggedIn;
    private InternetBroadcastReceiver netReceiver;
    private boolean netConnected;
    private IntentFilter filter;

    public LoadingPresenter(Loading activity) {
        this.activity = activity;

        // Set up Network Broadcast Receiver
        connectToDB();

        createNotificationChannel();
    }

    public void connectToDB() {
        if (isNetworkConnected()) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            isLoggedIn = mAuth.getCurrentUser() != null;

            //connect to database with DB
            Repository.InitializeConnection();
            Repository.attach(this);
        } else {
            Button button = activity.findViewById(R.id.button_try_again);
            button.setVisibility(View.VISIBLE);
            Toast.makeText(activity, "לא ניתן להיכנס לאפליקציה כאשר אין רשת מחוברת",
                    Toast.LENGTH_LONG).show();
        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = activity.getString(R.string.notif_channel_name);
            String description = activity.getString(R.string.notif_channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    activity.getString(R.string.notif_channel_id), name, importance
            );
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void update() {
        activity.updateProgressBar(100);
    }

    public void setNetConnected(boolean netConnected) {
        this.netConnected = netConnected;
    }
}
