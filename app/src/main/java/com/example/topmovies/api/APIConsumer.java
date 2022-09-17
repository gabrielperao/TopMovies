package com.example.topmovies.api;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuffer;


public class APIConsumer {
    private String url;
    private String apiToken;
    private static APIConsumer instance = null;

    private APIConsumer(String url, String apiToken) {
        this.url = url;
        this.apiToken = apiToken;
    }

    public static APIConsumer getInstance(String url, String token) {
        if (instance == null) {
            instance = new APIConsumer(url, token);
        }
        return instance;
    }

    public String consume() {
        StringBuffer buffer;

        try {
            URL url = new URL(this.url + "?api_key=" + this.apiToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
