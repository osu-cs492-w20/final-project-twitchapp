package com.example.twichapp.favorites;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.twichapp.data.FavStreamer;

@Database(entities = {FavStreamer.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavStreamerDao favStreamerDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "favorites_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
