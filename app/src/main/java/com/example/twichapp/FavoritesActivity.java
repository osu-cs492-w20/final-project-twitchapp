package com.example.twichapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.twichapp.data.TwitchStream;
import com.example.twichapp.stream.SavedStreamViewModel;
import com.example.twichapp.stream.StreamActivity;
import com.example.twichapp.streamers.StreamersAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, StreamersAdapter.OnStreamerClickListener {
    private DrawerLayout mDrawerLayout;

    private SavedStreamViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        RecyclerView savedStreamsRV = findViewById(R.id.rv_saved_stream_items);
        savedStreamsRV.setLayoutManager(new LinearLayoutManager(this));
        savedStreamsRV.setHasFixedSize(true);

        final StreamersAdapter adapter = new StreamersAdapter(this);
        savedStreamsRV.setAdapter(adapter);

        mViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication())
        ).get(SavedStreamViewModel.class);

        mViewModel.getAllStreams().observe(this, new Observer<List<TwitchStream>>() {
            @Override
            public void onChanged(List<TwitchStream> twitchStreams) {
                adapter.updateTwitchStreams(twitchStreams);
            }
        });

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
            default:
                return false;
        }
    }

    @Override
    public void onStreamerClick(TwitchStream twitchStream) {
        Intent streamIntent = new Intent(this, StreamActivity.class);
        streamIntent.putExtra(StreamActivity.EXTRA_TWITCH_STREAM, twitchStream);
        startActivity(streamIntent);
    }
}
