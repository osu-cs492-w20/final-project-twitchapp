package com.example.twichapp.favorites;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twichapp.data.FavStreamer;
import com.example.twichapp.data.LoadFavoritesTask;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchStream;
import com.example.twichapp.utils.TwitchUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoritesRepository implements LoadFavoritesTask.AsyncCallback {
    private static final String TAG = FavoritesRepository.class.getSimpleName();
    private FavStreamerDao mFavStreamerDao;
    private MutableLiveData<Status> mLoadingStatus;
    private MutableLiveData<List<TwitchStream>> mFavStreams;
    private List<FavStreamer> favStreamersCurr;

    public FavoritesRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mFavStreamerDao = db.favStreamerDao();
        mLoadingStatus = new MutableLiveData<>();
        mFavStreams = new MutableLiveData<>();
        mFavStreams.setValue(null);
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    public void insertFavorite(FavStreamer favStreamer) {
        Log.d(TAG,"INSERTING: " + favStreamer.user_name);
        if (favStreamersCurr != null) {
            for (int i = 0; i < favStreamersCurr.size(); i++) {
                if (favStreamersCurr.get(i).user_id.equals(favStreamer.user_id)) {
                    return;
                }
            }
        }
        new InsertFavAsyncTask(mFavStreamerDao).execute(favStreamer);
    }

    public void deleteFavorite(FavStreamer favStreamer) {
        new DeleteFavAsyncTask(mFavStreamerDao).execute(favStreamer);
    }

    public LiveData<List<FavStreamer>> getAllFavorites() {
        return mFavStreamerDao.getAllFavorites();
    }

    public LiveData<List<TwitchStream>> getAllFavStreams() {
        return mFavStreams;
    }

    public LiveData<FavStreamer> getFavById(String id) {
        return mFavStreamerDao.getFavById(id);
    }

    public void loadFavorites(List<FavStreamer> favStreamers) {
        if (favStreamers == null || favStreamers.size() == 0) {
            mFavStreams.setValue(new ArrayList<TwitchStream>());
            return;
        }
        favStreamersCurr = favStreamers;
        mFavStreams.setValue(null);
        mLoadingStatus.setValue(Status.LOADING);
        String url = TwitchUtils.buildTwitchFavoritesURL(favStreamers);
        new LoadFavoritesTask(url,this).execute();
    }

    @Override
    public void onFavLoadFinished(List<TwitchStream> twitchStreams) {
        List<TwitchStream> favStreams = twitchStreams;
        if (twitchStreams != null) {
            for (int i = 0; i < favStreamersCurr.size(); i++) {
                FavStreamer favStreamer = favStreamersCurr.get(i);
                Log.d(TAG,"FAV STREAMER: " + favStreamer.user_name);
                boolean active = false;
                for (int j = 0; j <twitchStreams.size(); j++) {
                    if (favStreamer.user_id.equals(twitchStreams.get(j).user_id)) {
                        active = true;
                    }
                }
                if (!active) {
                    TwitchStream newStream = new TwitchStream();
                    newStream.user_name = favStreamer.user_name;
                    newStream.user_id = favStreamer.user_id;
                    newStream.title = "Offline";
                    newStream.viewer_count = 0;
                    newStream.thumbnail_url = null;
                    twitchStreams.add(newStream);
                }
            }
            mFavStreams.setValue(twitchStreams);
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    private static class InsertFavAsyncTask
            extends AsyncTask<FavStreamer, Void, Void> {
        private FavStreamerDao mAsyncTaskDao;

        InsertFavAsyncTask(FavStreamerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavStreamer... favStreamers) {
            mAsyncTaskDao.insert(favStreamers[0]);
            return null;
        }
    }

    private static class DeleteFavAsyncTask
            extends AsyncTask<FavStreamer, Void, Void> {
        private FavStreamerDao mAsyncTaskDao;

        DeleteFavAsyncTask(FavStreamerDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavStreamer... favStreamers) {
            mAsyncTaskDao.delete(favStreamers[0]);
            return null;
        }
    }
}
