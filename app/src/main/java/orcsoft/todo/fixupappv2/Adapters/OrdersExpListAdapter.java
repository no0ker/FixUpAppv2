package orcsoft.todo.fixupappv2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.R;

public class OrdersExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Order> orders;

    public OrdersExpListAdapter(Context mContext, List<Order> orders) {
        this.mContext = mContext;
        this.orders = new ArrayList<>();
        this.orders.addAll(orders);
    }

    @Override
    public int getGroupCount() {
        return orders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_orders_group, null);
        }

        if (isExpanded){
            //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        Order currentOrder = orders.get(groupPosition);

        TextView address = (TextView) convertView.findViewById(R.id.list_address);
        address.setText(
                StringEscapeUtils.unescapeHtml4(
                    currentOrder.getAddress()));
        TextView date_time = (TextView) convertView.findViewById(R.id.list_date_time);
//        String date_time_string = currentOrder.getDatetime_works();
//        date_time.setText(date_time_string.substring(0, date_time_string.length() - 3));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setOrders(List<Order> orders) {
        this.orders.clear();
        this.orders.addAll(orders);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
