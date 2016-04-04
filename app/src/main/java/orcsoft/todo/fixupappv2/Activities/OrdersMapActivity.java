package orcsoft.todo.fixupappv2.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.Utils.GeoCoderHelper;
import orcsoft.todo.fixupappv2.Utils.GeoCoderHelper_;
import orcsoft.todo.fixupappv2.Utils.NetHelper_;

@EActivity
public class OrdersMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private List<Order> orders;
    private GoogleMap mMap;
    private Order.Category ordersCategory;
    private GeoCoderHelper geoCoderHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        orders = (List<Order>) getIntent().getExtras().get(Operations.MAP_ACTIVITY_KEY_ORDERS_LIST);
        ordersCategory = (Order.Category) getIntent().getExtras().get(Operations.MAP_ACTIVITY_KEY_ORDER_CATEGORY);
        geoCoderHelper = GeoCoderHelper_.getInstance_(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap(orders);
    }

    @Background
    protected void initMap(List<Order> orders) {
        LatLng saratov = new LatLng(51.533103, 46.034158);
        setCamera(saratov);

        for (Order o : orders) {
            setMarker(o, o.getAddress());
        }

        setListeners();
    }

    @UiThread
    protected void setCamera(LatLng center) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(center)
                .zoom(11)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @UiThread
    protected void setMarker(Order orderForPoint, String name) {
        mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(orderForPoint.getLatitude(), orderForPoint.getLongitude()))
                        .title("(" + ordersCategory.toString().substring(0, 1) + ")" + name));

    }

    @UiThread
    protected void setListeners() {
        final Context context = getApplicationContext();
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                String markerCategory = marker.getTitle().substring(1, 2);
                final String address = marker.getTitle().substring(3);
                Order currentOrder = getOrder(address);
                if ("F".equals(markerCategory)) {
                    MenuActivity_.intent(context)
                            .extra(Operations.MENU_ACTIVITY_KEY_CHANGE_FRAGMENT_ID, R.id.menu_orders_free)
                            .extra(Operations.ORDER_FRAGMENT_KEY_LONG_CLICK_ORDER_ID, currentOrder.getId())
                            .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .start();
                } else if ("A".equals(markerCategory)) {
                    MenuActivity_.intent(context)
                            .extra(Operations.MENU_ACTIVITY_KEY_CHANGE_FRAGMENT_ID, R.id.menu_orders_active)
                            .extra(Operations.ORDER_FRAGMENT_KEY_LONG_CLICK_ORDER_ID, currentOrder.getId())
                            .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .start();
                }
            }
        });
    }

    private Order getOrder(String address) {
        for (Order currentOrder : orders) {
            if (currentOrder.getAddress().equals(address)) {
                return currentOrder;
            }
        }
        return null;
    }

    @Background
    protected void setAccept(int orderId, String time) {
        try {
            Context context = getApplicationContext();
            NetHelper_.getInstance_(context).setAccept(orderId, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
