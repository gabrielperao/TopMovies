package com.example.topmovies.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.topmovies.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieController {

    public static String getResponse(String stringUrl) {
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;
        StringBuffer buffer;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return buffer.toString();
    }

    public static List<MovieModel> getTopRatedMovies(String response) {
        List<MovieModel> movieList = new ArrayList<>();

        try {
            JSONObject responseObject = new JSONObject(response);
            String arrayStr = responseObject.getString("results");
            JSONArray jsonArray = new JSONArray(arrayStr);

            int lenJsonArray = jsonArray.length();
            for (int i = 0; i < lenJsonArray; i++) {
                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                MovieModel movieModel = new MovieModel();
                movieModel.setIndexId(i);
                movieModel.setMovieId(Integer.valueOf(obj.getString("id")));
                movieModel.setTitle(obj.getString("title"));
                movieModel.setPosterPath(obj.getString("poster_path"));
                movieList.add(movieModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieList;
    }

    public static MovieModel getMovie(String response) {
        MovieModel movieModel = new MovieModel();

        try {
            JSONObject obj = new JSONObject(response);
            movieModel.setTitle(obj.getString("title"));
            movieModel.setOverview(obj.getString("overview"));
            movieModel.setRuntime(Integer.valueOf(obj.getString("runtime")));
            String year = getYearFromDateStr(obj.getString("release_date"));
            movieModel.setReleaseYear(year);

            List<String> genresList = new ArrayList<>();
            JSONArray genresJsonArray = new JSONArray(obj.getString("genres"));
            for (int i = 0; i < genresJsonArray.length(); i++) {
                JSONObject genreObj = new JSONObject(genresJsonArray.get(i).toString());
                genresList.add(genreObj.getString("name"));
            }
            movieModel.setGenres(genresList);

            List<String> languagesList = new ArrayList<>();
            JSONArray languagesJsonArray = new JSONArray(obj.getString("spoken_languages"));
            for (int i = 0; i < languagesJsonArray.length(); i++) {
                JSONObject genreObj = new JSONObject(languagesJsonArray.get(i).toString());
                languagesList.add(genreObj.getString("name"));
            }
            movieModel.setLanguages(languagesList);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieModel;
    }

    public static boolean checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) return false;
        int type = activeNetwork.getType();
        return (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE);
    }

    private static String getYearFromDateStr(String dateStr) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
            Date date = formatter.parse(dateStr);
            if (date == null) return "-";

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return String.valueOf(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            return "-";
        }
    }
}