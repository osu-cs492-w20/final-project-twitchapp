package com.example.twichapp.data;

import android.os.AsyncTask;

import com.example.twichapp.utils.NetworkUtils;
import com.example.twichapp.utils.TwitchUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadTwitchGamesTask extends AsyncTask<Void, Void, String> {
    public interface AsyncCallback {
        void onTwitchLoadFinished(List<TwitchGame> gamesItems);
    }

    private String mURL;
    private AsyncCallback mCallback;

    public LoadTwitchGamesTask(String url, AsyncCallback callback) {
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
        ArrayList<TwitchGame> twitchGames = null;
        if (s != null) {
            twitchGames = TwitchUtils.parseTwitchGamesSearchResults(s);
        }
        mCallback.onTwitchLoadFinished(twitchGames);
    }
}
