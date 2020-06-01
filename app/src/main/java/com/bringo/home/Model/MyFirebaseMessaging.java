package com.bringo.home.Model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.bringo.home.MessagingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try{
            String sented = remoteMessage.getData().get("sented");
            String user = remoteMessage.getData().get("user");

            SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
            String currentUser = preferences.getString("currentuser", "none");

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null &&
                    sented.equals
                            (firebaseUser.getUid())) {
                if (!currentUser.equals(user)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        sendOreoNotification(remoteMessage);
                    } else {
                        sendNotification(remoteMessage);
                    }
                }
            }
        }
        catch (Exception e){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendOreoNotification(RemoteMessage remoteMessage) {
        Intent intent;
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Bundle bundle = new Bundle();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80];
        intent = new Intent(this, MessagingActivity.class);
        bundle.putString("user_id", user);
        intent.putExtras(bundle);


        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
        //Uri defaultSound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sound);
        Uri defaultSound = Uri.parse("");
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound, icon, audioAttributes);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        oreoNotification.getManager().notify(i, builder.build());

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent;
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        Bundle bundle = new Bundle();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80];
        intent = new Intent(this, MessagingActivity.class);
        bundle.putString("user_id", user);
        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        //Uri defaultSound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.sound);
        Uri defaultSound = Uri.parse("");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        noti.notify(i, builder.build());
    }
}