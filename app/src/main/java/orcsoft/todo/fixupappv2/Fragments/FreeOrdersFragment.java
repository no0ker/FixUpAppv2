package orcsoft.todo.fixupappv2.Fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TimePicker;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Container;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Exceptions.NetException;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;

@EFragment(R.layout.fragment_orders)
public class FreeOrdersFragment extends OrdersFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersCategory = Order.Category.FREE;
    }

    @Background
    protected void setAccept(int orderId, String time) {
        try {
            netHelper.setAccept(orderId, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeFragmentToActive();
    }

    @UiThread
    protected void changeFragmentToActive() {
        onOptionsItemSelected(R.id.action_update);
        mListener.onFragmentInteraction(new Container() {{
                                            parameters.put(Operations.OPERATION, Operations.CHANGE_FRAGMENT);
                                            parameters.put(Operations.FRAGMENT_ID, R.id.menu_orders_active);
                                            parameters.put(Operations.WITH_RELOAD, Operations.YES);
                                        }}
        );
    }


    @Override
    protected void onLongClickMakeAlertDialog(final Order order) {
        Context context = getContext();

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    String minuteStr;
                    if (minute == 0) {
                        minuteStr = "00";
                    } else {
                        minuteStr = String.valueOf(minute);
                    }
                    setAccept(order.getId(), hourOfDay + ":" + minuteStr);
                }
            }
        };

        final Calendar dateAndTime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, dateAndTime.get(Calendar.HOUR_OF_DAY), 0, true);
        timePickerDialog.setTitle("Выберите время прибытия на заявку");
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "Принять заявку", timePickerDialog);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Отказаться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        timePickerDialog.show();
    }


    @Override
    protected List<Order> getOrders() throws IOException, NetException {
        List<Order> newOrders = netHelper.getFreeOrders();
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
