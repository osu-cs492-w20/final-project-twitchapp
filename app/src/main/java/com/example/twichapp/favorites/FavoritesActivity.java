package com.example.twichapp.favorites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.twichapp.MainActivity;
import com.example.twichapp.R;
import com.example.twichapp.data.FavStreamer;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchStream;
import com.example.twichapp.streamers.StreamersActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FavoritesAdapter.OnFavoriteClickListener {
    private static final String TAG = FavoritesActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    private RecyclerView mFavoritesRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;

    private FavoritesAdapter mFavoritesAdapter;
    private FavoritesViewModel mFavoritesViewModel;

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

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mFavoritesRV = findViewById(R.id.rv_favorites);

        mFavoritesAdapter = new FavoritesAdapter(this);
        mFavoritesRV.setAdapter(mFavoritesAdapter);
        mFavoritesRV.setLayoutManager(new LinearLayoutManager(this));
        mFavoritesRV.setHasFixedSize(true);

        mDrawerLayout = findViewById(R.id.drawer_layout_favorites);

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer_favorites);
        navigationView.setNavigationItemSelectedListener(this);

        mFavoritesViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(
                        getApplication()
                )
        ).get(FavoritesViewModel.class);

        mFavoritesViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mFavoritesRV.setVisibility(View.VISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mFavoritesRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        mFavoritesViewModel.getAllFavStreams().observe(this,
                new Observer<List<TwitchStream>>() {
                    @Override
                    public void onChanged(List<TwitchStream> twitchStreams) {
                        mFavoritesAdapter.updateFavorites(twitchStreams);
                    }
                });

        mFavoritesViewModel.loadFavorites(mFavoritesViewModel.getAllFavorites().getValue());
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
            case R.id.nav_streamers:
                Intent streamersIntent = new Intent(this, StreamersActivity.class);
                startActivity(streamersIntent);
                return true;
            case R.id.nav_favorites:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onFavoriteClick(TwitchStream twitchStream) {

    }
}
