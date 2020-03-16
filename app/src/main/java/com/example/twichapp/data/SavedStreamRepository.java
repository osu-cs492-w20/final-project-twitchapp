package com.example.twichapp.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.twichapp.utils.TwitchUtils;

import java.util.ArrayList;
import java.util.List;

public class SavedStreamRepository implements LoadTwitchTask.AsyncCallback {
    private SavedStreamDao mDao;

    private List<TwitchStream> favStreamersCurr;
    private MutableLiveData<Status> mLoadingStatus;

    public SavedStreamRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDao = db.savedStreamDao();
        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public void insertSavedStream(TwitchStream stream) {
        new InsertAsyncTask(mDao).execute(stream);
    }

    public void deleteSavedStream(TwitchStream stream) {
        new DeleteAsyncTask(mDao).execute(stream);
    }

    public LiveData<List<TwitchStream>> getAllStreams() {
        return mDao.getAllStreams();
    }

    public LiveData<TwitchStream> getStreamByName(String userName) {
        return mDao.getStreamByName(userName);
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    private static class InsertAsyncTask extends AsyncTask<TwitchStream, Void, Void> {
        private SavedStreamDao mAsyncTaskDao;
        InsertAsyncTask(SavedStreamDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(TwitchStream... twitchStreams) {
            mAsyncTaskDao.insert(twitchStreams[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<TwitchStream, Void, Void> {
        private SavedStreamDao mAsyncTaskDao;
        DeleteAsyncTask(SavedStreamDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(TwitchStream... twitchStreams) {
            mAsyncTaskDao.delete(twitchStreams[0]);
            return null;
        }
    }

    public void loadFavorites(List<TwitchStream> favStreamers) {
        if (favStreamers == null || favStreamers.size() == 0) {
            return;
        }
        favStreamersCurr = favStreamers;
        mLoadingStatus.setValue(Status.LOADING);
        String url = TwitchUtils.buildTwitchFavoritesURL(favStreamers);
        new LoadTwitchTask(url,this).execute();
    }

    @Override
    public void onTwitchLoadFinished(List<TwitchStream> twitchStreams) {
        List<TwitchStream> favStreams = twitchStreams;
        if (twitchStreams != null) {
            for (int i = 0; i < favStreamersCurr.size(); i++) {
                TwitchStream favStreamer = favStreamersCurr.get(i);
                boolean active = false;
                for (int j = 0; j <twitchStreams.size(); j++) {
                    if (favStreamer.user_id.equals(twitchStreams.get(j).user_id)) {
                        active = true;
                        insertSavedStream(twitchStreams.get(j));
                        break;
                    }
                }
                if (!active) {
                    TwitchStream newStream = new TwitchStream();
                    newStream.user_name = favStreamer.user_name;
                    newStream.user_id = favStreamer.user_id;
                    newStream.title = "Offline";
                    newStream.viewer_count = 0;
                    newStream.thumbnail_url = null;
                    insertSavedStream(newStream);
                }
            }
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

}
