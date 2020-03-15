package com.example.twichapp.data;

import android.os.AsyncTask;

import com.example.twichapp.utils.NetworkUtils;
import com.example.twichapp.utils.TwitchUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoadTwitchTask extends AsyncTask<Void, Void, String> {
    public interface AsyncCallback {
        void onTwitchLoadFinished(List<TwitchStream> forecastItems);
    }

    private String mURL;
    private AsyncCallback mCallback;

    LoadTwitchTask(String url, AsyncCallback callback) {
        mURL = url;
        mCallback = callback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String forecastJSON = null;
        try {
            forecastJSON = NetworkUtils.doHttpGet(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forecastJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<TwitchStream> twitchStreams = null;
        if (s != null) {
            twitchStreams = TwitchUtils.parseTwitchSearchResults(s);
        }
        mCallback.onTwitchLoadFinished(twitchStreams);
    }
}
