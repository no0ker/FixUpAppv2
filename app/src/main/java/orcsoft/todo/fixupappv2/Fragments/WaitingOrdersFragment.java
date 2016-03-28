package orcsoft.todo.fixupappv2.Fragments;

import org.androidannotations.annotations.EFragment;

import java.io.IOException;
import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Exceptions.NetException;
import orcsoft.todo.fixupappv2.R;

@EFragment(R.layout.fragment_orders)
public class WaitingOrdersFragment extends OrdersFragment{
    Order.Category ordersCategory = Order.Category.DONE;

    @Override
    protected void onLongClickMakeAlertDialog(Order order) {

    }

    @Override
    protected List<Order> getOrders() throws IOException, NetException {
        List<Order> newOrders = netHelper.getDoneOrders();
        addOrdersToDBCache(newOrders, ordersCategory);
        return newOrders;
    }

    @Override
    protected List<Order> getOrdersFromCache() {
        return getOrdersFromCache(ordersCategory);
    }
}
