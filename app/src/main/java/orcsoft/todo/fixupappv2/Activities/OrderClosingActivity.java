package orcsoft.todo.fixupappv2.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import orcsoft.todo.fixupappv2.Adapters.AbstractItemsAdapter;
import orcsoft.todo.fixupappv2.Entity.AbstractItem;
import orcsoft.todo.fixupappv2.Entity.CloseOrderRequest;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Entity.Product;
import orcsoft.todo.fixupappv2.Entity.Service;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.Utils.NetHelper;
import orcsoft.todo.fixupappv2.Utils.NetHelper_;

@EActivity(R.layout.activity_order_closing)
public class OrderClosingActivity extends AppCompatActivity implements AbstractItemsAdapter.OnAdapterInteractionListener {
    private Order currentOrder;
    private List<Service> services;
    private List<Product> products;
    private NetHelper netHelper;
    private List<AbstractItem> abstractItems = new ArrayList<AbstractItem>();
    private AbstractItemsAdapter abstractItemsAdapter;

    @ViewById(R.id.items_list_view)
    protected ListView listView;

    @ViewById(R.id.sum_value)
    protected TextView sumValue;

    @ViewById(R.id.parent_layout)
    protected LinearLayout parentLaoyut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentOrder = getIntent().getParcelableExtra(Operations.ORDER_ENTITY);
        netHelper = NetHelper_.getInstance_(getBaseContext());
        init();
    }

    @AfterViews
    protected void initList() {
        abstractItemsAdapter = new AbstractItemsAdapter(abstractItems, this);
        listView.setAdapter(abstractItemsAdapter);
        sumValue.setText("0");
    }

    @Background
    protected void init() {
        try {
            services = netHelper.getServices();
            products = netHelper.getProducts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.add_service)
    protected void buttonAddService() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[services.size()];
        for (int i = 0; i < services.size(); ++i) {
            Service service = services.get(i);
            items[i] = (service.getTitle() + " - " + service.getCost());
        }
        builder.setTitle("Услуги")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        abstractItems.add(services.get(which));
                        abstractItemsAdapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }

    @Click(R.id.add_product)
    protected void buttonAddProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[products.size()];
        for (int i = 0; i < products.size(); ++i) {
            Product product = products.get(i);
            items[i] = (product.getTitle() + " - " + product.getCost());
        }
        builder.setTitle("Продукты")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        abstractItems.add(products.get(which));
                        abstractItemsAdapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onAdapterInteraction() {
        Map<Integer, Integer> counts = abstractItemsAdapter.getCounts();
        Integer sumCount = 0;
        for (int i = 0; i < abstractItems.size(); ++i) {
            if (counts.containsKey(i)) {
                sumCount += counts.get(i) * abstractItems.get(i).getCost();
            }
        }
        sumValue.setText(String.valueOf(sumCount));
    }

    @Background
    @Click(R.id.close_order)
    protected void closeOrder() {








        Map<Integer, Integer> counts = abstractItemsAdapter.getCounts();
        for (int i = 0; i < abstractItems.size(); ++i) {
            if (counts.containsKey(i)) {
                abstractItems.get(i).setCount(counts.get(i));
            }
        }

        if(abstractItems.isEmpty()){
            Snackbar.make(parentLaoyut, R.string.error_message_empty_items, Snackbar.LENGTH_LONG).show();
            return;
        }

        List<Product> productsForRequest = new ArrayList<Product>();
        List<Service> servicesForRequest = new ArrayList<Service>();

        for (AbstractItem abstractItem : abstractItems) {
            if (Product.class.equals(abstractItem.getClass())) {
                productsForRequest.add((Product) abstractItem);
            } else if (Service.class.equals(abstractItem.getClass())) {
                servicesForRequest.add((Service) abstractItem);
            } else {
                throw new IllegalArgumentException();
            }
        }

        CloseOrderRequest closeOrderRequest = new CloseOrderRequest(productsForRequest, servicesForRequest);
        try {
            NetHelper.CommonResponseMessage responseMessage = NetHelper_.getInstance_(this).closeOrder(currentOrder, closeOrderRequest);
            if(0 != responseMessage.code){
                Snackbar.make(parentLaoyut, responseMessage.code + " " + responseMessage.message, Snackbar.LENGTH_LONG).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(Operations.ORDERS_FRAGMENT_WITH_RELOAD, Operations.YES);
                MenuActivity_.intent(this)
                        .flags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .extra(Operations.ORDERS_FRAGMENT_WITH_RELOAD, Operations.YES)
                        .start();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(parentLaoyut, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
}
