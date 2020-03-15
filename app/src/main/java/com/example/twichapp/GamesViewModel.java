package com.example.twichapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.twichapp.data.GamesRepository;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchGame;

import java.util.List;

public class GamesViewModel extends ViewModel {
    private LiveData<List<TwitchGame>> mTwitchGames;
    private LiveData<Status> mLoadingStatus;

    private GamesRepository mRepository;

    public GamesViewModel() {
        mRepository = new GamesRepository();
        mTwitchGames = mRepository.getStreams();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadGames() {
        mRepository.loadGames();
    }

    public LiveData<List<TwitchGame>> getGames() {
        return mTwitchGames;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
