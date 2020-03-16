package com.example.twichapp.favorites;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.twichapp.data.FavStreamer;

import java.util.List;

@Dao
public interface FavStreamerDao {

    @Insert
    void insert(FavStreamer favStreamer);

    @Delete
    void delete(FavStreamer favStreamer);

    @Query("SELECT * FROM favorites")
    LiveData<List<FavStreamer>> getAllFavorites();

    @Query("SELECT * FROM favorites WHERE user_id = :id LIMIT 1")
    LiveData<FavStreamer> getFavById(String id);
}
