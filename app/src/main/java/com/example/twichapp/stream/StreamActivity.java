package com.example.twichapp.stream;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
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
    private TextView mBufferingTextView;
    private double mWidth;
    private double mHeight;

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

        // get the width of the users screen to set the width of the video
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        double ppi = Math.sqrt(Math.pow(displayMetrics.widthPixels, 2) + Math.pow(displayMetrics.heightPixels, 2));
        mWidth = (displayMetrics.widthPixels / ppi) * displayMetrics.densityDpi;
        mHeight = (displayMetrics.heightPixels / ppi) * displayMetrics.densityDpi;
    //
//        mBufferingTextView = findViewById(R.id.buffering_textview);
//        mVideoView = (VideoView)findViewById(R.id.vv_video_view_stream);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setMediaPlayer(mVideoView);
//        mVideoView.setMediaController(mediaController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String CHANNEL = "thewaifuwaluigi";
        String url = "http://player.twitch.tv/?channel=" + CHANNEL;
        initializePlayer(CHANNEL);
    }

    private void initializePlayer(String channel) {
        String streamHTML =
                "<html> " +
                    "<body> " +
                    "<!-- Add a placeholder for the Twitch embed --> " +
                        "<div id=\"twitch-embed\"></div> " +
                        "<!-- Load the Twitch embed script --> " +
                        "<script src=\"https://embed.twitch.tv/embed/v1.js\"></script> " +
                        "<!-- Create a Twitch.Embed object that will render within the \"twitch-embed\" root element. --> " +
                        "<script type=\"text/javascript\"> " +
                            "new Twitch.Embed(\"twitch-embed\", { " +
                                "width: " + mWidth + "," +
                                "height: " + mHeight + "," +
                                "channel: \"" + channel + "\"," +
//                                "layout: \"video\"" +
                            "}); " +
                    "</script> " +
                    "</body>" +
                "</html>";

        WebView mWebView;
        mWebView = (WebView) findViewById(R.id.webview1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(streamHTML, "text/html", null);
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
////        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.loadUrl(url);

//
//        mBufferingTextView.setVisibility(VideoView.VISIBLE);
//
//        Uri uri = Uri.parse(url);
//        mVideoView.setVideoURI(uri);
//
//        mVideoView.setOnPreparedListener(
//                new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);
//                        mVideoView.start();
//                    }
//                }
//        );
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
