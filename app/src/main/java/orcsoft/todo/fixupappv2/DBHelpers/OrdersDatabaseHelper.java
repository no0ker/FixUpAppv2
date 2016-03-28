package orcsoft.todo.fixupappv2.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import orcsoft.todo.fixupappv2.Entity.Order;

public class OrdersDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "orders";
    private static final int DATABASE_VERSION = 5;
    private Dao<Order, Integer> ordersDao;


    public OrdersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Order.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Order.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Order, Integer> getDao() throws SQLException {
        if (ordersDao == null) {
            ordersDao = getDao(Order.class);
        }
        return ordersDao;
    }

}
