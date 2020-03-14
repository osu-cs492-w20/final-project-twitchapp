package com.example.twichapp.stream;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
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
    private LinearLayout mlinearLayout;
    private WebView mWebView;
    private double mWidth;
    private double mHeight;

    private String mChannel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // you can use bundle to pass in the stream channel
        Bundle bundle = getIntent().getExtras();
        mChannel = bundle.getString("channel_name");

        setContentView(R.layout.activity_stream);

        Toolbar toolbar = findViewById(R.id.toolbar_stream);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_menu);

        // The following 3 lines change the default toolbar title to a given string
        TextView textView = toolbar.findViewById(R.id.toolbar_tv_stream);
        textView.setText(mChannel);
        actionBar.setDisplayShowTitleEnabled(false);

        mDrawerLayout = findViewById(R.id.drawer_layout_stream);

        NavigationView navigationView = findViewById(R.id.nv_nav_drawer_stream);
        navigationView.setNavigationItemSelectedListener(this);


        // This is because the height and width take time to lad and those
        // values are needed to build the webview so we must wait tell
        // they load and then we can initialize the stream
        mlinearLayout = findViewById(R.id.linear_layout_stream);
        ViewTreeObserver viewTreeObserver = mlinearLayout.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int viewHeight = mlinearLayout.getHeight();
                    if (viewHeight != 0) {
                        mlinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        initializePlayer();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stream_menu, menu);
        return true;
    }



    private void initializePlayer() {
        // get the height of the action bar so we can account
        // for it when making the height of the stream
        TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        // get the demintions of the viewable window
        // this is the width and height minus the status
        // and nav bar
        int height = mlinearLayout.getHeight();
        int width = mlinearLayout.getWidth();

        // here we get the display metrics because we need
        // the density to convert from pixels to dots
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // here we convert to dps
        mWidth = width / displayMetrics.density;
        mHeight = ((height - actionBarHeight) / displayMetrics.density);

        // this is the html for the stream
        String streamHTML =
                "<html> " +
                    "<body style=\"margin: 0; padding: 0\"> " +
                        "<div id=\"twitch-embed\"></div> " +
                        "<script src=\"https://embed.twitch.tv/embed/v1.js\"></script> " +
                        "<!-- Create a Twitch.Embed object that will render within the \"twitch-embed\" root element. --> " +
                        "<script type=\"text/javascript\"> " +
                            "new Twitch.Embed(\"twitch-embed\", { " +
                                "width: " + mWidth + "," +
                                "height: " + mHeight + "," +
                                "channel: \"" + mChannel + "\"," +
                            "}); " +
                    "</script> " +
                    "</body>" +
                "</html>";

        // load the stream in web view
        mWebView = (WebView) findViewById(R.id.webview1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(streamHTML, "text/html", null);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_favorite:
                return true;
            case R.id.action_share:
                shareStream();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareStream() {
        if (mChannel != null) {
            String shareText = "Check out this stream from " + mChannel + ": https://www.twitch.tv/" + mChannel;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(shareIntent, null);
            startActivity(chooserIntent);
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
