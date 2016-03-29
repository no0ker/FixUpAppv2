package orcsoft.todo.fixupappv2.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.androidannotations.annotations.EService;

import java.util.concurrent.TimeUnit;

import orcsoft.todo.fixupappv2.Activities.MenuActivity_;
import orcsoft.todo.fixupappv2.R;

@EService
public class OrdersDaemonService extends Service {
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            TimeUnit.SECONDS.sleep(3);
            sendNotif();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotif() {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        Intent notificationIntent = new Intent(getApplicationContext(), MenuActivity_.class);
        notificationIntent.putExtra("bbb", "aaa");
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = builder.setSmallIcon(R.drawable.ic_menu_camera)
//                .setLargeIcon(R.drawable.ic_menu_manage)
                .setTicker("ticker text")
                .setContentText("content text")
                .setContentTitle("content title")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000L, 1000L, 1000L})
                .build();

        notificationManager.notify(101, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
