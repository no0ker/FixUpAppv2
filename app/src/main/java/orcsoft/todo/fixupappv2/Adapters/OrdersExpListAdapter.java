package orcsoft.todo.fixupappv2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.R;

public class OrdersExpListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Order> orders;
    private Order.Category ordersCategory;

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
        Order currentOrder = orders.get(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_orders_group, null);
        }

        if (isExpanded) {
            convertView.findViewById(R.id.list_exp_view).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.list_comment)).setText(currentOrder.getComment());
            ((TextView) convertView.findViewById(R.id.list_date_time_wishes)).setText(
                    MessageFormat.format("{0}  {1}", currentOrder.getDate_wish(), currentOrder.getInterval_title())
            );
        } else {
            convertView.findViewById(R.id.list_exp_view).setVisibility(View.GONE);
        }

        TextView address = (TextView) convertView.findViewById(R.id.list_address);
        address.setText(
                StringEscapeUtils.unescapeHtml4(
                        currentOrder.getAddress()));


        //-------------------------------------------------------------------------
        // list_date_time_works
        TextView datetimeWorksView = (TextView) convertView.findViewById(R.id.list_date_time_works);
        String datetimeWorks = currentOrder.getDatetime_works();
        if(StringUtils.isEmpty(datetimeWorks)){
            datetimeWorksView.setVisibility(View.GONE);
        } else {
            datetimeWorksView.setText(datetimeWorks.substring(0, datetimeWorks.length() - 3));
            datetimeWorksView.setVisibility(View.VISIBLE);
        }
        // end of list_date_time_works
        //-------------------------------------------------------------------------


        //-------------------------------------------------------------------------
        // list_cost
        TextView costView = (TextView) convertView.findViewById(R.id.list_cost);
        String cost = currentOrder.getCost();
        if(StringUtils.isEmpty(cost)){
            costView.setVisibility(View.GONE);
        } else {
            costView.setText(
                    MessageFormat.format("{0} р.", currentOrder.getCost())
            );
            costView.setVisibility(View.VISIBLE);
        }
        // end of list_cost
        //-------------------------------------------------------------------------


        ((TextView) convertView.findViewById(R.id.list_name)).setText(
                MessageFormat.format("{0}  {1}", currentOrder.getClient_lastname(), currentOrder.getClient_firstname())
        );

        applyCategoryRules(convertView);

        return convertView;
    }

    private void applyCategoryRules(View convertView) {
        if(Order.Category.DONE.equals(ordersCategory)){
            convertView.findViewById(R.id.list_exp_view).setVisibility(View.GONE);
        }
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

    public void setOrdersCategory(Order.Category ordersCategory) {
        this.ordersCategory = ordersCategory;
    }
}
