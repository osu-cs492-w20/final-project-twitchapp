package com.example.twichapp.stream;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.twichapp.FavoritesActivity;
import com.example.twichapp.MainActivity;
import com.example.twichapp.R;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class StreamActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stream);

        Toolbar toolbar = findViewById(R.id.toolbar_stream);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        // The following 3 lines change the default toolbar title to a given string
        TextView textView = toolbar.findViewById(R.id.toolbar_tv_stream);
        textView.setText(getString(R.string.page_stream));
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout_stream);

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer_stream);
        navigationView.setNavigationItemSelectedListener(this);

        mVideoView = (VideoView)findViewById(R.id.vv_video_view_stream);

        playVideo("https://www.youtube.com/watch?time_continue=418&v=SrPHLj_q_OQ&feature=emb_logo");
    }

    private void playVideo(String url) {
        Log.d(TAG, "YEET?");
        Uri uri = Uri.parse(url);
        mVideoView.setVideoURI(uri);
        mVideoView.start();
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
                // Uncomment this to test Streamers Activity
//                Intent streamersIntent = new Intent(this, StreamersActivity.class);
//                startActivity(streamersIntent);
                return true;
            case R.id.nav_favorites:
                Intent favoritesIntent = new Intent(this, FavoritesActivity.class);
                startActivity(favoritesIntent);
                return true;
            case R.id.temp_stream:
                Intent streamIntent = new Intent(this, StreamActivity.class);
                startActivity(streamIntent);
            default:
                return false;
        }
    }
}
