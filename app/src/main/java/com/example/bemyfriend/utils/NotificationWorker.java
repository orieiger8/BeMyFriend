package com.example.bemyfriend.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.bemyfriend.R;
import com.example.bemyfriend.db.Repository;
import com.example.bemyfriend.model.User;

public class NotificationWorker extends Worker {
    private Context context;

    private static final int NOTIF_ID = 69;

    private int unreadMessages;


    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        unreadMessages = 0;

        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        User user = Repository.getInstance().getMyUser();
        int unreads = user.getTotalUnreads();
        if (unreadMessages < unreads)
            SendNotification();

        unreadMessages = unreads;

        return null;
    }

    private void SendNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                context.getString(R.string.notif_channel_id));
        builder.setContentTitle("Do you know Joe?")
                .setContentText("JOE MAMA! HA GOT'EM!")
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        notificationManager.notify(NOTIF_ID, builder.build());
    }
}
