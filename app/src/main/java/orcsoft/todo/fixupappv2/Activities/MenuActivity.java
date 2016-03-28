package orcsoft.todo.fixupappv2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.Map;

import orcsoft.todo.fixupappv2.Entity.Container;
import orcsoft.todo.fixupappv2.Entity.Order;
import orcsoft.todo.fixupappv2.Fragments.ActiveOrdersFragment_;
import orcsoft.todo.fixupappv2.Fragments.ArchiveOrderFragments_;
import orcsoft.todo.fixupappv2.Fragments.FreeOrdersFragment_;
import orcsoft.todo.fixupappv2.Fragments.OrdersFragment;
import orcsoft.todo.fixupappv2.Fragments.WaitingOrdersFragment_;
import orcsoft.todo.fixupappv2.Operations;
import orcsoft.todo.fixupappv2.R;
import orcsoft.todo.fixupappv2.SharedPreferences.Prefs_;

@EActivity()
public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OrdersFragment.OnFragmentInteractionListener {

    private static final String currentFragmenKey = "currentFragmentKey";
    public static final String orders = "orders";
    private int currentFragment;

    @Pref
    protected Prefs_ prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle bundle = null;
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
        } else {
            bundle = new Bundle();
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(currentFragmenKey)) {
                currentFragment = savedInstanceState.getInt(currentFragmenKey);
                menuListener(currentFragment, bundle);
            }
        } else {
            menuListener(R.id.menu_orders_active, bundle);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        menuListener(id);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        menuListener(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Container container) {
        Map<String, Object> params = container.parameters;
        if (params.containsKey(Operations.OPERATION)) {
            String operationName = (String) params.get(Operations.OPERATION);

            if (Operations.CHANGE_FRAGMENT.equals(operationName)) {
                if (params.containsKey(Operations.ID)) {
                    int id = (int) params.get(Operations.ID);
                    menuListener(id);
                } else if (params.containsKey(Operations.ACTIVITY_NAME)) {
                    String activityName = (String) params.get(Operations.ACTIVITY_NAME);
                    if (Operations.ORDERS_CLOSING_ACTIVITY.equals(activityName)) {
                        Order currentOrder = (Order) params.get(Operations.ORDER_ENTITY);
                        OrderClosingActivity_
                                .intent(getApplicationContext())
                                .extra(Operations.ORDER_ENTITY, currentOrder)
                                .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .start();
                    }

                } else if (params.containsKey(Operations.FRAGMENT_ID)) {
                    Integer newFragmentId = (Integer) params.get(Operations.FRAGMENT_ID);
                    Bundle bundle = new Bundle();
                    bundle.putString(Operations.WITH_RELOAD, Operations.YES);
                    menuListener(newFragmentId, bundle);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(currentFragmenKey, currentFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        menuListener(inState.getInt(currentFragmenKey));
    }

    private void menuListener(int itemId) {
        menuListener(itemId, new Bundle());
    }

    private void menuListener(int itemId, Bundle bundle) {
        Fragment fragment = null;
        if (itemId == R.id.menu_orders_active) {
            fragment = ActiveOrdersFragment_.builder().arg(bundle).build();
            currentFragment = R.id.menu_orders_active;
            setTitle(R.string.menu_orders_active);

        } else if (itemId == R.id.menu_orders_free) {
            fragment = FreeOrdersFragment_.builder().arg(bundle).build();
            currentFragment = R.id.menu_orders_free;
            setTitle(R.string.menu_orders_free);

        } else if (itemId == R.id.menu_orders_waiting) {
            fragment = WaitingOrdersFragment_.builder().arg(bundle).build();
            currentFragment = R.id.menu_orders_waiting;
            setTitle(R.string.menu_orders_waiting);

        } else if (itemId == R.id.menu_orders_archive) {
            fragment = ArchiveOrderFragments_.builder().arg(bundle).build();
            currentFragment = R.id.menu_orders_active;
            setTitle(R.string.menu_orders_archive);

        } else if (itemId == R.id.menu_preferences) {
            PreferencesActivity_.intent(getApplicationContext())
                    .flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .start();

        } else if (itemId == R.id.menu_logout) {
            prefs.accessToken().put("");
            prefs.password().put("");
            LogOnActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK).start();
        }

        if (null != fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment lostFragment = fragmentManager.findFragmentByTag("TAG");
            if (lostFragment != null && lostFragment.getClass().equals(fragment.getClass())) {
                // do nothing
            } else {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment, "TAG")
                        .commit();
            }
        }
    }
}
