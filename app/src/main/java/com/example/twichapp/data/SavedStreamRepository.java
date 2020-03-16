package com.example.twichapp.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SavedStreamRepository {
    private SavedStreamDao mDao;

    public SavedStreamRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDao = db.savedStreamDao();
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

}
