package com.example.twichapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SavedStreamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TwitchStream stream);

    @Delete
    void delete(TwitchStream stream);

    @Query("SELECT * FROM streams")
    LiveData<List<TwitchStream>> getAllStreams();

    @Query("SELECT * FROM streams WHERE user_name = :userName LIMIT 1")
    LiveData<TwitchStream> getStreamByName(String userName);
}
