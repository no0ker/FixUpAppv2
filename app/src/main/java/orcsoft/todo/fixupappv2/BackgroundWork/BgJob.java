package orcsoft.todo.fixupappv2.BackgroundWork;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import orcsoft.todo.fixupappv2.Activities.MenuActivity_;
import orcsoft.todo.fixupappv2.DBHelpers.OrdersDatabaseHelper;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.Utils.NetHelper;
import orcsoft.todo.fixupappv2.Utils.NetHelper_;

public class BgJob extends Job {
    public static String TAG = "bgJob";
    private int a = 0;

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        List<Order> orders = new ArrayList<Order>();
        NetHelper netHelper = NetHelper_.getInstance_(getContext());
        try {
            orders = netHelper.getFreeOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Order> ordersFromCache = new ArrayList<>();
        OpenHelperManager.setOpenHelperClass(OrdersDatabaseHelper.class);
        OrdersDatabaseHelper ordersDatabaseHelper = OpenHelperManager.getHelper(getContext(), OrdersDatabaseHelper.class);
        try {
            Dao<Order, Integer> ordersDao = ordersDatabaseHelper.getDao();
            ordersFromCache = ordersDao.queryForEq("category", Order.Category.FREE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OpenHelperManager.releaseHelper();
        ordersDatabaseHelper = null;


        Notification.Builder builder = new Notification.Builder(getContext());

        Intent notificationIntent = new Intent(getContext(), MenuActivity_.class);
        notificationIntent.putExtra("bbb", "aaa");
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        ++a;
        Notification notification = builder.setSmallIcon(R.drawable.ic_menu_camera)
//                .setLargeIcon(R.drawable.ic_menu_manage)
                .setTicker("ticker text")
                .setContentText("content text" + a)
                .setContentTitle("content title")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000L, 1000L, 1000L})
                .build();

        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);


        return null;
    }
}
