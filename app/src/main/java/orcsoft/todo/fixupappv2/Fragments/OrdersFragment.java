package orcsoft.todo.fixupappv2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import orcsoft.todo.fixupappv2.Activities.OrdersMapActivity_;
import orcsoft.todo.fixupappv2.Adapters.OrdersExpListAdapter;
import orcsoft.todo.fixupappv2.DBHelpers.OrdersDatabaseHelper;
import orcsoft.todo.fixupappv2.Entity.Container;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Exceptions.NetException;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.Utils.NetHelper;
import orcsoft.todo.fixupappv2.Utils.NetHelper_;

@EFragment(R.layout.fragment_orders)
public abstract class OrdersFragment extends Fragment {

    private List<Order> orders = Collections.emptyList();
    private OrdersExpListAdapter ordersExpListAdapter;

    protected Order.Category ordersCategory;
    protected OnFragmentInteractionListener mListener;
    protected NetHelper netHelper;
    protected AdapterView.OnItemLongClickListener onGroupLongClickListener;

    abstract protected List<Order> getOrders() throws IOException, NetException;
    abstract protected List<Order> getOrdersFromCache();
    abstract protected void onLongClickMakeAlertDialog(Order order);

    @ViewById(R.id.expandable_list_view)
    protected ExpandableListView expandable_list_view;

    @ViewById(R.id.fragments_progress_bar)
    protected ProgressBar progressBar;

    public static interface OnFragmentInteractionListener {
        void onFragmentInteraction(Container container);
    }

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersExpListAdapter = new OrdersExpListAdapter(getActivity().getApplicationContext(), orders);
        onGroupLongClickListener = new OnOrdersGroupClickListener();
        netHelper = NetHelper_.getInstance_(getContext());
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (Operations.YES.equals(bundle.getString(Operations.WITH_RELOAD))) {
                onOptionsItemSelected(R.id.action_update);
            }
        }
    }

    @AfterViews
    protected void updateAfterViews() {
        expandable_list_view.setAdapter(ordersExpListAdapter);
        expandable_list_view.setOnItemLongClickListener(onGroupLongClickListener);
        updateOrdersFromCacheBg();
    }

    protected void updateOrdersFromCacheBg() {
        List<Order> cacheOrders = getOrdersFromCache();
        if (cacheOrders != null && !cacheOrders.isEmpty()) {
            ordersExpListAdapter.setOrders(cacheOrders);
            ordersExpListAdapter.notifyDataSetChanged();
        }
    }

    // ---------------------------------------------------

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // ---------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        onOptionsItemSelected(id);
        return super.onOptionsItemSelected(item);
    }

    public void onOptionsItemSelected(int itemId) {
        if (itemId == R.id.action_update) {
            updateOrdersBg();
        } else if (itemId == R.id.action_get_map) {
            OrdersMapActivity_
                    .intent(this)
                    .parcelableArrayListExtra(Operations.ORDERS_KEY, (ArrayList<? extends Parcelable>) getOrdersFromCache())
                    .start();
        }
    }

    // ---------------------------------------------------

    @Background
    protected void updateOrdersBg() {
        setProgressBar(true);
        try {
            orders = getOrders();
            ordersExpListAdapter.setOrders(orders);
            ordersExpListAdapter.setOrdersCategory(ordersCategory);
            notifyDataSetChangedUI();
        } catch (NetException e) {
            showErrorMessage(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            setProgressBar(false);
        }
    }

    @UiThread
    protected void showErrorMessage(NetException e) {
        Snackbar.make(expandable_list_view, e.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @UiThread
    protected void notifyDataSetChangedUI() {
        ordersExpListAdapter.notifyDataSetChanged();
    }

    @UiThread
    protected void setProgressBar(boolean b) {
        progressBar.setIndeterminate(b);
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    // ---------------------------------------------------

    public class OnOrdersGroupClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            Order order = ((OrdersExpListAdapter) ((ExpandableListView) parent).getExpandableListAdapter())
                    .getOrders().get(position);
            onLongClickMakeAlertDialog(order);
            return true;
        }
    }

    protected void addOrdersToDBCache(List<Order> ordersForCache, Order.Category category) {
        for (Order o : ordersForCache) {
            o.setCategory(category);
        }
        OpenHelperManager.setOpenHelperClass(OrdersDatabaseHelper.class);
        OrdersDatabaseHelper ordersDatabaseHelper = OpenHelperManager.getHelper(getContext(), OrdersDatabaseHelper.class);
        try {
            Dao<Order, Integer> ordersDao = ordersDatabaseHelper.getDao();
            DeleteBuilder<Order, Integer> deleteBuilder = ordersDao.deleteBuilder();
            deleteBuilder.where().eq("category", category);
            PreparedDelete<Order> preparedDelete = deleteBuilder.prepare();
            ordersDao.delete(preparedDelete);
            for (Order o : ordersForCache) {
                ordersDao.createOrUpdate(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OpenHelperManager.releaseHelper();
        ordersDatabaseHelper = null;
    }

    protected List<Order> getOrdersFromCache(Order.Category category) {
        List<Order> result = new ArrayList<>();
        OpenHelperManager.setOpenHelperClass(OrdersDatabaseHelper.class);
        OrdersDatabaseHelper ordersDatabaseHelper = OpenHelperManager.getHelper(getContext(), OrdersDatabaseHelper.class);
        try {
            Dao<Order, Integer> ordersDao = ordersDatabaseHelper.getDao();
            result = ordersDao.queryForEq("category", category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OpenHelperManager.releaseHelper();
        ordersDatabaseHelper = null;
        return result;
    }
}
