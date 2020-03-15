package com.example.twichapp.streamers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.twichapp.data.Status;
import com.example.twichapp.data.StreamersRepository;
import com.example.twichapp.data.TwitchStream;

import java.util.List;

public class StreamersViewModel extends ViewModel {
    private LiveData<List<TwitchStream>> mTwitchStreams;
    private LiveData<Status> mLoadingStatus;

    private StreamersRepository mRepository;

    public StreamersViewModel() {
        mRepository = new StreamersRepository();
        mTwitchStreams = mRepository.getStreams();
        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadStreams(String game) {
        mRepository.loadStreams(game);
    }

    public LiveData<List<TwitchStream>> getStreams() {
        return mTwitchStreams;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
