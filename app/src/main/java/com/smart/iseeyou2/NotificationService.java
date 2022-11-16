package com.smart.iseeyou2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NotificationService extends FirebaseMessagingService {

    private String TAG = "Notification Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getNotification().getBody());
    }

    NotificationManager notificationManager;

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_alert)
                        .setContentTitle("!ACTIVITY ALERT!")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel();

    }

    private void createNotificationChannel() {
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.sentry);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "mychannel";
            String description = "testing";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            NotificationChannel channel = new NotificationChannel("cnid", name, importance);
            channel.setDescription(description);
            channel.enableLights(true); channel.enableVibration(true);
            channel.setSound(sound, audioAttributes);
            notificationManager.createNotificationChannel(channel);
        }
    };


}
