package com.appsinventiv.uberautomate;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  WASenderFgSvc extends Service {

    private static final int NOTIFICATION_ID = 12;
    private static final String NOTIFICATION_CHANNEL_ID = "312312";
    SharedPreferences sp;
    Integer progress = 0;
    List<String> recipientList = new ArrayList<>();
    private NotificationCompat.Builder notificationBuilder;
//    String message;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "dss";
            NotificationChannel serviceChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
        Intent stopSelf = new Intent(this, WASenderFgSvc.class);
        stopSelf.setAction("ACTION_STOP_SERVICE");
        PendingIntent pStopSelf = PendingIntent
                .getService(this, 0, stopSelf
                        , PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(
                        0, "Close", pStopSelf
                ).build();
        Notification notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Ask Question")
                .setContentText("Ask Question is running")
                .addAction(action)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
        notificationManager.notify(1, notification);
        startForeground(1, notification);
        notificationManager.cancel(1);
        Boolean start = intent.getBooleanExtra("start", true);
        send();
        return START_STICKY;
    }

    @SuppressLint("ApplySharedPref")
    private void send() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.ubercab","com.ubercab.presidio.app.core.root.RootActivity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

//        if (progress >= recipientList.size()) {
//            Toast.makeText(this, "Task Completed", Toast.LENGTH_SHORT).show();
//            sp.edit().putBoolean("running", false).commit();
//            notificationBuilder.setContentText("Sent");
//            Notification not = notificationBuilder.build();
//            startForeground(NOTIFICATION_ID, not);
//            return;
//        }
//        String recipient = recipientList.get(progress);
//        String message = "*Test%20Message*\n\nPlease%20Ignore%20it\nThanks";
////        Intent sendIntent = new Intent();
////        sendIntent.setPackage("com.whatsapp");
////        sendIntent.setAction("android.intent.action.SEND");
////        sendIntent.setType("text/plain");
////        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
////        sendIntent.putExtra("jid", recipient + "@s.whatsapp.net");
////        String url = "https://api.whatsapp.com/send?phone=" + recipientList.get(progress)[0];
//        String url = "https://wa.me/" + recipient + "?text=" + Sender.msg;
//        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setPackage("com.whatsapp");
//        i.putExtra("jid", recipient + "@s.whatsapp.net");
//        progress++;
//        startActivity(i);


//        startActivity(sendIntent);
    }
}