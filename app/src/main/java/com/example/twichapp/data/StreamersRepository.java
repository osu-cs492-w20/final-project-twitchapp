package com.example.twichapp.data;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twichapp.utils.TwitchUtils;

import java.util.Date;
import java.util.List;

public class StreamersRepository implements LoadTwitchTask.AsyncCallback {
    private static final String TAG = StreamersRepository.class.getSimpleName();

    private MutableLiveData<List<TwitchStream>> mTwitchStreams;
    private MutableLiveData<Status> mLoadingStatus;

    private String mCurrentGame;

    public StreamersRepository() {
        mTwitchStreams = new MutableLiveData<>();
        mTwitchStreams.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);

        mCurrentGame = null;
    }

    public void loadStreams(String game) {
        if (shouldFetchStreams(game)) {
            mCurrentGame = game;
            mTwitchStreams.setValue(null);
            mLoadingStatus.setValue(Status.LOADING);
            String url = TwitchUtils.buildTwitchURL(game);
            Log.d(TAG, "fetching new twitch streams data with this URL: " + url);
            new LoadTwitchTask(url, this).execute();
        } else {
            Log.d(TAG, "using cached forecast data");
        }
    }

    public LiveData<List<TwitchStream>> getStreams() {
        return mTwitchStreams;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    private boolean shouldFetchStreams(String game) {
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
    public void onTwitchLoadFinished(List<TwitchStream> twitchStreams) {
        mTwitchStreams.setValue(twitchStreams);
        if (twitchStreams != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }
}
