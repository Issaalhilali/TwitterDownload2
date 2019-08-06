package com.issa.twitterdownload.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.issa.twitterdownload.MainActivity;
import com.issa.twitterdownload.R;
import com.issa.twitterdownload.utils.Constant;
import com.issa.twitterdownload.utils.ServiceUtil;

public class AutoListenService extends Service {


    private static final String GENERAL_CHANNEL = "general channel";
    private Context context;
    private ClipboardManager.OnPrimaryClipChangedListener listener;
    private ClipboardManager mClipboard;
    private NotificationManager notificationManager;
    private Notification vNotification;
    private NotificationCompat.Builder vNotification1;


    @Override
    public void onCreate() {
        super.onCreate();

        context=getApplicationContext();

        mClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();


        listener = new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                performClipboardCheck();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent openActivityIntent=new Intent(context, MainActivity.class);
        openActivityIntent.putExtra("service_on",true);
        openActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent openActivityPIntent = PendingIntent.getActivity(context, Constant.AUTO_REQUEST_CODE, openActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent stopAutoIntent = new Intent(context, StopAutoListenReceiver.class);
        stopAutoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent stopAutoPIntent = PendingIntent.getBroadcast(context, Constant.REQUEST_CODE, stopAutoIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Icon icon = Icon.createWithResource(context,R.drawable.ic_stop_black_24dp);
            Notification.Action.Builder builder = new Notification.Action.Builder(icon, "STOP", stopAutoPIntent);

            vNotification = new Notification.Builder(context,GENERAL_CHANNEL)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setContentTitle("TwittaSave AutoListen Running...")
                    .setContentText("Copy tweet URL to start downloading video or gif")
                    .setContentIntent(openActivityPIntent)
                    .setAutoCancel(true)
                    .addAction(builder.build())
                    .build();
            vNotification.flags=Notification.FLAG_NO_CLEAR;

            notificationManager.notify(Constant.NOTI_IDENTIFIER, vNotification);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            vNotification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setContentTitle("TwittaSave AutoListen Running...")
                    .setContentText("Copy tweet URL to start downloading video or gif")
                    .setContentIntent(openActivityPIntent)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .addAction(R.drawable.ic_stop_black_24dp, "STOP", stopAutoPIntent)
                    .build();
            vNotification.flags=Notification.FLAG_NO_CLEAR;

            notificationManager.notify(Constant.NOTI_IDENTIFIER, vNotification);
        }else{
            vNotification1 = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setContentTitle("TwittaSave AutoListen Running...")
                    .setContentText("Copy tweet URL to start downloading video or gif")
                    .setContentIntent(openActivityPIntent)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_stop_black_24dp, "STOP", stopAutoPIntent);

            notificationManager.notify(Constant.NOTI_IDENTIFIER, vNotification1.build());

        }



        mClipboard.addPrimaryClipChangedListener(listener);
        Toast.makeText(context, "TwittaSave AutoListen Enabled", Toast.LENGTH_SHORT).show();


        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel genChannel = new NotificationChannel(GENERAL_CHANNEL,
                    getResources().getString(R.string.general_channel),
                    NotificationManager.IMPORTANCE_HIGH);

            genChannel.setShowBadge(true);
            genChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(genChannel);
        }
    }

    private void performClipboardCheck() {
        if (mClipboard.hasPrimaryClip()){
            String copiedURL = mClipboard.getPrimaryClip().getItemAt(0).getText().toString();
            ServiceUtil.fetchTweet(copiedURL,context);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (notificationManager!=null) {
            notificationManager.cancel(Constant.NOTI_IDENTIFIER);
        }

        if (mClipboard!=null && listener!=null){
            this.mClipboard.removePrimaryClipChangedListener(listener);
            Toast.makeText(context, "TwittaSave AutoListen Disabled", Toast.LENGTH_SHORT).show();

        }
    }
}

