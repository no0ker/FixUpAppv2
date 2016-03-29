package orcsoft.todo.fixupappv2.BackgroundWork;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import orcsoft.todo.fixupappv2.DBHelpers.OrdersDatabaseHelper;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Utils.NetHelper;
import orcsoft.todo.fixupappv2.Utils.NetHelper_;

public class BgJob extends Job {
    public static String TAG = "bgJob";

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





        return null;
    }
}
