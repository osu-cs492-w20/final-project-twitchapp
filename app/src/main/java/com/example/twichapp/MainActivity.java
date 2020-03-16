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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.twichapp.stream.StreamActivity;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchGame;
import com.example.twichapp.streamers.StreamersActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GamesAdapter.OnGameClickListener {
    private DrawerLayout mDrawerLayout;
    private RecyclerView mGamesRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;

    private GamesAdapter mGamesAdapter;
    private GamesViewModel mGamesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        // The following 3 lines change the default toolbar title to a given string
        TextView textView = toolbar.findViewById(R.id.toolbar_tv_main);
        textView.setText(getString(R.string.page_main));
        actionBar.setDisplayShowTitleEnabled(false);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mGamesRV = findViewById(R.id.rv_twitch_game_items);

        mGamesAdapter = new GamesAdapter(this);
        mGamesRV.setAdapter(mGamesAdapter);
        mGamesRV.setLayoutManager(new LinearLayoutManager(this));
        mGamesRV.setHasFixedSize(true);

        mDrawerLayout = findViewById(R.id.drawer_layout_main);

        mGamesViewModel = new ViewModelProvider(this).get(GamesViewModel.class);

        mGamesViewModel.getGames().observe(this, new Observer<List<TwitchGame>>() {
            @Override
            public void onChanged(List<TwitchGame> twitchGames) {
                mGamesAdapter.updateTwitchGames(twitchGames);
                if (twitchGames != null) {
                    mGamesAdapter.updateTwitchGames(twitchGames);
                }
            }
        });

        mGamesViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mGamesRV.setVisibility(View.VISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mGamesRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer_main);
        navigationView.setNavigationItemSelectedListener(this);

        mGamesViewModel.loadGames();
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
                return true;
            case R.id.nav_streamers:
                Intent streamersIntent = new Intent(this, StreamersActivity.class);
                startActivity(streamersIntent);
                return true;
            case R.id.nav_favorites:
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onGameClick(TwitchGame twitchGame){
        Intent intent = new Intent(this, StreamersActivity.class);
        intent.putExtra(StreamersActivity.EXTRA_GAME, twitchGame);
        startActivity(intent);
    }
}
