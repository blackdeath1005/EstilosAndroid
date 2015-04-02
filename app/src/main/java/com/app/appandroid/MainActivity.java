package com.app.appandroid;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.app.appandroid.menu.fragment.FavoritesFragment;
import com.app.appandroid.menu.fragment.HistoryFragment;
import com.app.appandroid.menu.fragment.MapFragment;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private  NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    String idUsuario;
    String noUsuario;
    String correoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idUsuario = this.getIntent().getExtras().getString("idUsuario");
        noUsuario = this.getIntent().getExtras().getString("noUsuario");
        correoUsuario = this.getIntent().getExtras().getString("correoUsuario");

        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment selectedFragment = null;

        switch (position) {
            case 0:
                selectedFragment = new MapFragment();
                mTitle = getString(R.string.title_section_search);
                break;
            case 1:
                selectedFragment = new HistoryFragment();
                Bundle argsHistory = new Bundle();
                argsHistory.putString("idUsuario", idUsuario);
                selectedFragment.setArguments(argsHistory);
                mTitle = getString(R.string.title_section_history);
                break;
            case 2:
                selectedFragment = new FavoritesFragment();
                Bundle argsFavorite = new Bundle();
                argsFavorite.putString("idUsuario", idUsuario);
                selectedFragment.setArguments(argsFavorite);
                mTitle = getString(R.string.title_section_favorites);
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, selectedFragment).commit();
    }

    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle); //Titulo al cerrar el drawer y ver ventanas
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}