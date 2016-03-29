package orcsoft.todo.fixupappv2.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import org.androidannotations.annotations.EActivity;

import java.io.IOException;
import java.util.List;

import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;

@EActivity
public class OrdersMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private List<Order> orders;
    private GoogleMap mMap;
    private Order.Category ordersCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        orders = (List<Order>) getIntent().getExtras().get(Operations.ORDERS_KEY);
        ordersCategory = (Order.Category) getIntent().getExtras().get(Operations.CATEGORY_KEY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            initCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCamera() throws IOException {
        Geocoder geocoder = new Geocoder(this);

        Address saratov = geocoder.getFromLocationName("Саратов", 1).get(0);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(saratov.getLatitude(), saratov.getLongitude()))
                .zoom(11)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        for (Order o : orders) {
            List<Address> address = geocoder.getFromLocationName(o.getAddress(), 1);
            if (address.size() >= 1) {
                LatLng point = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
                mMap.addMarker(
                        new MarkerOptions()
                                .position(point)
                                .title(ordersCategory.toString().substring(0, 1) + "-" + o.getAddress()));
            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerCategory = marker.getTitle().substring(0, 1);
                final String address = marker.getTitle().substring(2);

                AlertDialog.Builder builder = new AlertDialog.Builder(OrdersMapActivity.this);

                if ("A".equals(markerCategory)) {
                    builder.setTitle("Операции")
                            .setMessage("Изволите закрыть заявку?")
                            .setTitle(marker.getTitle())
                            .setPositiveButton("Ага", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (Order currentOrder : orders) {
                                        if (currentOrder.getAddress().equals(address)) {
                                            OrderClosingActivity_
                                                    .intent(getApplicationContext())
                                                    .extra(Operations.ORDER_ENTITY, currentOrder)
                                                    .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    .start();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Неа", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.create().show();
                } else if ("F".equals(markerCategory)) {

                }
                return false;
            }
        });
    }
}
