package ru.macrobit.abonnews.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.ui.activity.FragmentActivity;

public class GcmService extends GcmListenerService {

    public static final int notifyID = 9001;

    public GcmService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        sendNotification(data);
        String s = data.getString("message");
        s = data.getString("title");
    }

    @Override
    public void onDeletedMessages() {

//        sendNotification("Deleted messages on server");
    }

    @Override
    public void onMessageSent(String msgId) {
//        sendNotification("Upstream message sent. Id=" + msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
//        sendNotification("Upstream message send error. Id=" + msgId + ", error" + error);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle msg) {
        Intent resultIntent = new Intent(this, FragmentActivity.class);
//        resultIntent.putExtra("msg", msg);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
//                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(msg.getString("title"))
                .setContentText(msg.getString("message"))
                .setSmallIcon(R.mipmap.ic_launcher);
//        mNotifyBuilder.setContentIntent(resultPendingIntent);

        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        mNotifyBuilder.setContentText("New message from Server");
        mNotifyBuilder.setAutoCancel(true);
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
}