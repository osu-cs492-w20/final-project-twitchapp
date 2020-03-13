package com.example.twichapp.utils;

import android.net.Uri;

import com.example.twichapp.data.TwitchStream;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TwitchUtils {
    private final static String TWITCH_SEARCH_BASE_URL = "https://api.twitch.tv/helix/streams";
    private final static String TWITCH_GAME_QUERY_PARAM = "game_id";

    private static final Gson gson = new Gson();

    static class TwitchStreamResults {
        ArrayList<TwitchStream> data;
    }

    public static String buildIconURL(String thumbnail) {
        return thumbnail
                .replace("{width}", "1280")
                .replace("{height}", "720");
    }

    public static String buildTwitchURL(String game) {
        Uri.Builder builder = Uri.parse(TWITCH_SEARCH_BASE_URL).buildUpon();

        if (!game.equals("")) {
            builder.appendQueryParameter(TWITCH_GAME_QUERY_PARAM, game);
        }

        return builder.build().toString();
    }

    public static ArrayList<TwitchStream> parseTwitchSearchResults(String json) {
        TwitchStreamResults results = gson.fromJson(json, TwitchStreamResults.class);
        if (results != null && results.data != null) {
            return results.data;
        } else {
            return null;
        }
    }


}
