package com.example.twichapp.utils;

import android.net.Uri;

import com.example.twichapp.data.TwitchGame;
import com.example.twichapp.data.TwitchStream;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TwitchUtils {

    private final static String TWITCH_TOP_GAMES_URL = "https://api.twitch.tv/helix/games/top?first=100";
    private final static String TWITCH_SEARCH_BASE_URL = "https://api.twitch.tv/helix/streams";
    private final static String TWITCH_GAME_QUERY_PARAM = "game_id";
    private final static String TWITCH_USER_QUERY_PARAM = "user_id";

    private static final Gson gson = new Gson();

    static class TwitchGamesResults {
        ArrayList<TwitchGame> data;
    }

    static class TwitchStreamResults {
        ArrayList<TwitchStream> data;
    }

    public static String buildGameIconURL(String thumbnail) {
        return thumbnail
                .replace("{width}", "600")
                .replace("{height}", "900");
    }

    public static String buildIconURL(String thumbnail) {
        return thumbnail
                .replace("{width}", "1280")
                .replace("{height}", "720");
    }

    public static String buildTwitchGamesURL() {
        Uri.Builder builder = Uri.parse(TWITCH_TOP_GAMES_URL).buildUpon();
        return builder.build().toString();
    }

    public static String buildTwitchURL(String game) {
        Uri.Builder builder = Uri.parse(TWITCH_SEARCH_BASE_URL).buildUpon();

        if (!game.equals("")) {
            builder.appendQueryParameter(TWITCH_GAME_QUERY_PARAM, game);
        }

        return builder.build().toString();
    }

    public static String buildTwitchFavoritesURL(List<TwitchStream> streams) {
        Uri.Builder builder = Uri.parse(TWITCH_SEARCH_BASE_URL).buildUpon();

        for (int i = 0; i < streams.size(); i++) {
            builder.appendQueryParameter(TWITCH_USER_QUERY_PARAM,streams.get(i).user_id);
        }

        return builder.build().toString();
    }

    public static ArrayList<TwitchGame> parseTwitchGamesSearchResults(String json) {
        TwitchGamesResults results = gson.fromJson(json, TwitchGamesResults.class);

        if (results != null && results.data != null) {
            return results.data;
        } else {
            return null;
        }
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

