package com.br.cdr.mercadobarato.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.activity.dummy.DummyContent;
import com.br.cdr.mercadobarato.model.UserWrapper;
import com.br.cdr.mercadobarato.util.ExitAppDialog;
import com.br.cdr.mercadobarato.util.Utils;

public class NavigationMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ExitAppDialog.ExitListener, CompareProductsFragment.OnListFragmentInteractionListener {

    TextView logged_user;
    UserWrapper user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.checkin_menu);

//        logged_user = (TextView) findViewById(R.id.logged_user);
        Intent intent = getIntent();
        user = (UserWrapper) intent.getSerializableExtra("user");
        Log.i("userLogado", "anme " + user.getFirst_name());
//        logged_user = (TextView) drawer.findViewById(R.id.logged_user);
//        logged_user.setText(user.getFirst_name());


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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);

        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        return true;

    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Class fragmentClass = null;

        if (itemId == R.id.checkin_menu) {
            fragmentClass = MapsActivity.class;
        } else if (itemId == R.id.add_products_menu) {
            fragmentClass = AddProductShoppingListActivity.class;
        } else if (itemId == R.id.list_menu) {
            fragmentClass = SuperMarketCheckedInActivity.class;
        } else if (itemId == R.id.profile_menu) {
            fragmentClass = InfoActivity.class;
        } else if (itemId == R.id.compare_products) {
            fragmentClass = ComparePriceFormFragment.class;
        } else if (itemId == R.id.exit_menu) {

            ExitAppDialog dialog = new ExitAppDialog();

            dialog.show(getSupportFragmentManager(), "ExitAppDialog");
        } else {
            fragmentClass = MapsActivity.class;
        }


        Utils.openFragment(fragmentClass, getSupportFragmentManager());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onExit() {
        finish();
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}