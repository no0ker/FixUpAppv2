package orcsoft.todo.fixupappv2.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import org.androidannotations.annotations.EFragment;

import java.io.IOException;
import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Container;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Exceptions.NetException;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;

@EFragment(R.layout.fragment_orders)
public class ActiveOrdersFragment extends OrdersFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersCategory = Order.Category.ACTIVE;
    }

    @Override
    protected void onLongClickMakeAlertDialog(final Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Операции")
                .setMessage("Изволите закрыть заявку?")
                .setPositiveButton("Ага", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onFragmentInteraction(new Container() {{
                                                            parameters.put(Operations.OPERATION, Operations.CHANGE_FRAGMENT);
                                                            parameters.put(Operations.ACTIVITY_NAME, Operations.ORDERS_CLOSING_ACTIVITY);
                                                            parameters.put(Operations.ORDER_ENTITY, order);
                                                        }}
                        );
                    }
                })
                .setNegativeButton("Неа", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    @Override
    protected List<Order> getOrders() throws IOException, NetException {
        List<Order> newOrders = netHelper.getActiveOrders();
        addOrdersToDBCache(newOrders, ordersCategory);
        return newOrders;
    }

    @Override
    protected List<Order> getOrdersFromCache() {
        return getOrdersFromCache(ordersCategory);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.active_orders_fragment_menu, menu);
    }
}
