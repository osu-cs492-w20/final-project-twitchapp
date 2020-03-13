package com.example.twichapp.utils;

import com.example.twichapp.BuildConfig;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {
    private static final OkHttpClient mHTTPClient = new OkHttpClient();

    public static String doHttpGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Client-ID", BuildConfig.TWITCH_API_CLIENT_ID)
                .build();
        Response response = mHTTPClient.newCall(request).execute();
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}
