package com.example.twichapp.favorites;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.twichapp.data.FavStreamer;
import com.example.twichapp.data.Status;
import com.example.twichapp.data.TwitchStream;

import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    private FavoritesRepository mFavoritesRepository;

    public FavoritesViewModel(Application application) {
        super(application);
        mFavoritesRepository = new FavoritesRepository(application);
    }

    public void insertFavorite(FavStreamer favStreamer) {
        mFavoritesRepository.insertFavorite(favStreamer);
    }

    public void deleteFavorite(FavStreamer favStreamer) {
        mFavoritesRepository.deleteFavorite(favStreamer);
    }

    public LiveData<List<FavStreamer>> getAllFavorites() {
        return mFavoritesRepository.getAllFavorites();
    }

    public LiveData<FavStreamer> getFavById(String id) {
        return mFavoritesRepository.getFavById(id);
    }

    public LiveData<Status> getLoadingStatus() {
        return mFavoritesRepository.getLoadingStatus();
    }

    public void loadFavorites(List<FavStreamer> favStreamers) {
        mFavoritesRepository.loadFavorites(favStreamers);
    }

    public LiveData<List<TwitchStream>> getAllFavStreams() {
        return mFavoritesRepository.getAllFavStreams();
    }
}
