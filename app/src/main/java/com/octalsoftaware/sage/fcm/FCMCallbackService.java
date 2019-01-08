package com.octalsoftaware.sage.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.octalsoftaware.sage.view.activity.HomeActiviy;
import com.sage.conslantant.android.R;


/**
 * Created by shubham on 2/20/2017.
 * notification catch then show accordingly
 */

public class FCMCallbackService extends FirebaseMessagingService {
    private static final String TAG = "FCMCallbackService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("message", remoteMessage.getData().toString());
        sendNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("body"));
    }

    private void sendNotification(String title,String body) {
        Intent intent = new Intent(this, HomeActiviy.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(title.equalsIgnoreCase("new request")){
            Intent i = new Intent("new_request");
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "3")
                        .setSmallIcon(R.mipmap.ic_launcher)//R.drawable.ic_stat_ic_notification
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("3",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}

