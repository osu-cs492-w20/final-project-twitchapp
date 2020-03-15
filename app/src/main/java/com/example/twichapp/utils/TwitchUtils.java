package com.example.twichapp.utils;

import android.net.Uri;

import com.example.twichapp.data.TwitchGames;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TwitchUtils {
    private final static String TWITCH_TOP_GAMES_URL = "https://api.twitch.tv/helix/games/top?first=100";

    private static final Gson gson = new Gson();

    static class TwitchGamesResults {
        ArrayList<TwitchGames> data;
    }

    public static String buildIconURL(String thumbnail) {
        return thumbnail
                .replace("{width}", "600")
                .replace("{height}", "900");
    }

    public static String buildTwitchGamesURL() {
        Uri.Builder builder = Uri.parse(TWITCH_TOP_GAMES_URL).buildUpon();

        return builder.build().toString();
    }

    public static ArrayList<TwitchGames> parseTwitchGamesSearchResults(String json) {
        TwitchGamesResults results = gson.fromJson(json, TwitchGamesResults.class);
        if (results != null && results.data != null) {
            return results.data;
        } else {
            return null;
        }
    }


}