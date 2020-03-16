package com.example.twichapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "streams")
public class TwitchStream implements Serializable {
    @PrimaryKey
    @NonNull
    public String user_name;

    public String game_id;
    public String title;
    public int viewer_count;

    public String thumbnail_url;
}
