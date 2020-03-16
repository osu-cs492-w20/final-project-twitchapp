package com.example.twichapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavStreamer {
    @PrimaryKey
    @NonNull
    public String user_id;
    public String user_name;
}
