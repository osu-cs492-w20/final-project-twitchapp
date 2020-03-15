package com.example.twichapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twichapp.data.LoadTwitchTask;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchGames;
import com.example.twichapp.utils.TwitchUtils;

import java.util.List;

public class GamesRepository implements LoadTwitchTask.AsyncCallback {
    private static final String TAG = GamesRepository.class.getSimpleName();

    private MutableLiveData<List<TwitchGames>> mTwitchGames;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentGame;

    public GamesRepository() {
        mTwitchGames = new MutableLiveData<>();
        mTwitchGames.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mCurrentGame = null;
    }

    public void loadGames() {
        if (shouldFetchGames()) {
            mTwitchGames.setValue(null);
            mLoadingStatus.setValue(Status.LOADING);
            String url = TwitchUtils.buildTwitchGamesURL();
            Log.d(TAG, "fetching new twitch streams data with this URL: " + url);
            new LoadTwitchTask(url, this).execute();
        } else {
            Log.d(TAG, "using cached twitch streams data");
        }
    }

    public LiveData<List<TwitchGames>> getStreams() {
        return mTwitchGames;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    private boolean shouldFetchGames() {
        return true;
//        if (!TextUtils.equals(game, mCurrentLocation) || !TextUtils.equals(units, mCurrentUnits)) {
//            return true;
//        } else {
//            List<ForecastItem> forecastItems = mForecastItems.getValue();
//            if (forecastItems == null || forecastItems.size() == 0) {
//                return true;
//            } else {
//                return forecastItems.get(0).dateTime.before(new Date());
//            }
//        }
    }

    @Override
    public void onTwitchLoadFinished(List<TwitchGames> twitchStreams) {
        mTwitchGames.setValue(twitchStreams);
        if (twitchStreams != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }
}
