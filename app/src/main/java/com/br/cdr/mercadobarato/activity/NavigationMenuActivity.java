package com.br.cdr.mercadobarato.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.br.cdr.mercadobarato.R;
import com.br.cdr.mercadobarato.util.Utils;

public class NavigationMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
<<<<<<< HEAD:app/src/main/java/com/br/cdr/mercadobarato/activity/NavigationMenuActivity.java
        displaySelectedScreen(R.id.map_menu);
=======
        displaySelectedScreen(R.id.nav_checkin);
>>>>>>> 450a0e0cd433a75c49f1d44ab6962aa4458a3fa6:app/src/main/java/com/br/cdr/mercadobarato/NavigationMenuActivity.java

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

<<<<<<< HEAD:app/src/main/java/com/br/cdr/mercadobarato/activity/NavigationMenuActivity.java
        if (itemId == R.id.map_menu) {
            fragmentClass = MapsActivity.class;
        } else if (itemId == R.id.list_menu) {
            fragmentClass = InfoActivity.class;
        } else if (itemId == R.id.products_menu) {
=======
        if (itemId == R.id.nav_add_product_cart) {
            fragmentClass = AddProductShoppingListActivity.class;
        } else if (itemId == R.id.nav_checkin) {
            fragmentClass = MapsActivity.class;
        } else if (itemId == R.id.nav_perfil) {
>>>>>>> 450a0e0cd433a75c49f1d44ab6962aa4458a3fa6:app/src/main/java/com/br/cdr/mercadobarato/NavigationMenuActivity.java
            fragmentClass = AddProductActivity.class;

        } else if (itemId == R.id.profile) {

        } else {
            fragmentClass = MapsActivity.class;
        }

        Utils.openFragment(fragmentClass, getSupportFragmentManager());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


}