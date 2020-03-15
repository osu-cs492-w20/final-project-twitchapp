package com.example.twichapp.data;

import android.os.AsyncTask;

import com.example.twichapp.utils.NetworkUtils;
import com.example.twichapp.utils.TwitchUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadTwitchTask extends AsyncTask<Void, Void, String> {
    public interface AsyncCallback {
        void onTwitchLoadFinished(List<TwitchGames> gamesItems);
    }

    private String mURL;
    private AsyncCallback mCallback;

    public LoadTwitchTask(String url, AsyncCallback callback) {
        mURL = url;
        mCallback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String gamesJSON = null;
        try {
            gamesJSON = NetworkUtils.doHttpGet(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gamesJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<TwitchGames> twitchGames = null;
        if (s != null) {
            twitchGames = TwitchUtils.parseTwitchGamesSearchResults(s);
        }
        mCallback.onTwitchLoadFinished(twitchGames);
    }
}