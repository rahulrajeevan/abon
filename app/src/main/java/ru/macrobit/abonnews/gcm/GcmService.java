package ru.macrobit.abonnews.gcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.List;

import ru.macrobit.abonnews.R;
import ru.macrobit.abonnews.Values;
import ru.macrobit.abonnews.controller.GsonUtils;
import ru.macrobit.abonnews.model.PushIncome;
import ru.macrobit.abonnews.ui.activity.FragmentActivity;

public class GcmService extends GcmListenerService {

    public static final int notifyID = 9001;

    public GcmService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        sendNotification(data);
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
        try {
            ActivityManager am =(ActivityManager)getApplicationContext().getSystemService(getApplicationContext().ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            ActivityManager.RunningTaskInfo task = tasks.get(0); // get current task
            ComponentName rootActivity = task.baseActivity;

            Intent resultIntent = new Intent(this, FragmentActivity.class);
            if(rootActivity.getPackageName().equalsIgnoreCase("ru.macrobit.abonnews")){
                //your app is open
                // Now build an Intent that will bring this task to the front
                resultIntent = new Intent(this, FragmentActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

            }
            else
            {
                //your app is not open,start it by calling launcher activity
                resultIntent = new Intent(this, FragmentActivity.class);


            }
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PushIncome pushIncome = GsonUtils.fromJson(msg.getString("message"), PushIncome.class);
            resultIntent.putExtra(Values.TAG, Values.DETAIL_TAG);
            resultIntent.putExtra("id", pushIncome.getPostid());
            resultIntent.putExtra(Values.PUSH_TAG, Values.PUSH_TAG);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);


            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager;

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setSmallIcon(R.drawable.ic_notification);
            mNotifyBuilder.setContentIntent(resultPendingIntent);

            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;

            mNotifyBuilder.setDefaults(defaults);
            mNotifyBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification));
            mNotifyBuilder.setContentText(pushIncome.getText());
            mNotifyBuilder.setAutoCancel(true);
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}