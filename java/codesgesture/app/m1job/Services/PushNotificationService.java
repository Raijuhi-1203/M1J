package codesgesture.app.m1job.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import codesgesture.app.m1job.MainActivity;
import codesgesture.app.m1job.R;


public class PushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "";

    // Override onNewToken to get new token
    @Override
    public void onNewToken(@NonNull String token)
    {
        Log.d(TAG, "Refreshed token: " + token);
    }
    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage)
    {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("body"));
        }

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly
            // from FCM, the title and the body can be
            // fetched directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title, String message)
    {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title, String message)
    {
        Intent intent = new Intent(this, MainActivity.class);
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }


        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
        }
        else {
            builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.baseline_circle_notifications_24);
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }
}