package com.example.twichapp.streamers;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.twichapp.FavoritesActivity;
import com.example.twichapp.MainActivity;
import com.example.twichapp.R;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchStream;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class StreamersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, StreamersAdapter.OnStreamerClickListener {
    private static final String TAG = StreamersActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private RecyclerView mStreamersRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;

    private StreamersAdapter mStreamersAdapter;
    private StreamersViewModel mStreamersViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streamers);

        Toolbar toolbar = findViewById(R.id.toolbar_streamers);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        // The following 3 lines change the default toolbar title to a given string
        TextView textView = toolbar.findViewById(R.id.toolbar_tv_streamers);
        textView.setText(getString(R.string.page_streamers));
        actionBar.setDisplayShowTitleEnabled(false);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mStreamersRV = findViewById(R.id.rv_twitch_stream_items);

        mStreamersAdapter = new StreamersAdapter(this);
        mStreamersRV.setAdapter(mStreamersAdapter);
        mStreamersRV.setLayoutManager(new LinearLayoutManager(this));
        mStreamersRV.setHasFixedSize(true);

        mDrawerLayout = findViewById(R.id.drawer_layout_streamers);

        mStreamersViewModel = new ViewModelProvider(this).get(StreamersViewModel.class);

        mStreamersViewModel.getStreams().observe(this, new Observer<List<TwitchStream>>() {
            @Override
            public void onChanged(List<TwitchStream> twitchStreams) {
                mStreamersAdapter.updateTwitchStreams(twitchStreams);
                if (twitchStreams != null) {
                    mStreamersAdapter.updateTwitchStreams(twitchStreams);
                }
            }
        });

        mStreamersViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
                    mStreamersRV.setVisibility(View.VISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mStreamersRV.setVisibility(View.INVISIBLE);
                    mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer_streamers);
        navigationView.setNavigationItemSelectedListener(this);

        Button button = findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStreamersViewModel.loadStreams("");
            }
        });
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
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onStreamerClick(TwitchStream twitchStream) {

    }
}
