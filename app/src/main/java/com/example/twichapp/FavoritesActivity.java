package com.example.twichapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.twichapp.stream.StreamActivity;
import com.google.android.material.navigation.NavigationView;

public class FavoritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = findViewById(R.id.toolbar_favorites);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        // The following 3 lines change the default toolbar title to a given string
        TextView textView = toolbar.findViewById(R.id.toolbar_tv_favorites);
        textView.setText(getString(R.string.page_favorites));
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout_favorites);

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer_favorites);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.nav_favorites:
                return true;
            case R.id.temp_stream:
                Intent streamIntent = new Intent(this, StreamActivity.class);
                startActivity(streamIntent);
            default:
                return false;
        }
    }
}
