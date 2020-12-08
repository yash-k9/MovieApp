package com.devx.movie;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    public FirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotfication(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void createNotfication(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "FirebaseChannel")
                                                .setContentTitle(title)
                                                .setContentText(body)
                                                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                                                .setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(101, builder.build());

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
