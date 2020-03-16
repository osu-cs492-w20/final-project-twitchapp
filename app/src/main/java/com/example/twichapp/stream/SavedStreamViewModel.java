package com.example.twichapp.stream;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.twichapp.data.SavedStreamRepository;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchStream;

import java.util.List;

public class SavedStreamViewModel extends AndroidViewModel {
    private SavedStreamRepository mRepository;

    public SavedStreamViewModel(Application application) {
        super(application);
        mRepository = new SavedStreamRepository(application);
    }

    public void insertSavedStream(TwitchStream stream) {
        mRepository.insertSavedStream(stream);
    }

    public void deleteSavedStream(TwitchStream stream) {
        mRepository.deleteSavedStream(stream);
    }

    public LiveData<List<TwitchStream>> getAllStreams() {
        return mRepository.getAllStreams();
    }

    public LiveData<TwitchStream> getStreamByName(String userName) {
        return mRepository.getStreamByName(userName);
    }

    public LiveData<Status> getLoadingStatus() {
        return mRepository.getLoadingStatus();
    }

    public void loadFavorites(List<TwitchStream> favorites) {
        mRepository.loadFavorites(favorites);
    }
}
