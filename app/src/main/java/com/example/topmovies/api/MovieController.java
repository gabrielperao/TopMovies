package com.example.topmovies.api;

import com.example.topmovies.model.MovieModel;
import com.example.topmovies.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieController {

    public static final int N_MOVIES = 20;
    public static final String BASE_URL = "https://desafio-mobile.nyc3.digitaloceanspaces.com/movies-v2";

    public static String getResponse(String... strings) {
        String stringUrl = BASE_URL;
        if (strings != null) {
            // This method can be called with an id to be used as parameter in the API request
            if (strings.length > 0) {
                stringUrl = stringUrl + "/" + strings[0];
            }
        }

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

    public static List<MovieModel> getTopRatedMovies(String jsonResponse) {
        List<MovieModel> movieList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);

            int lenJsonArray = jsonArray.length();
            for (int i = 0; i < lenJsonArray; i++) {
                JSONObject obj = new JSONObject(jsonArray.get(i).toString());
                MovieModel movieModel = new MovieModel();
                movieModel.setIndexId(i);
                movieModel.setMovieId(Integer.valueOf(obj.getString("id")));
                movieModel.setTitle(obj.getString("title"));
                movieModel.setPosterPath(obj.getString("poster_url"));
                movieList.add(movieModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieList;
    }

    public static MovieModel getMovie(String jsonResponse) {
        MovieModel movieModel = new MovieModel();

        try {
            JSONObject obj = new JSONObject(jsonResponse);

            movieModel.setTitle(obj.getString("title"));

            movieModel.setOverview(obj.getString("overview"));

            int runtime = Integer.valueOf(obj.getString("runtime"));
            movieModel.setRuntime(Util.getRuntimeStrFormat(runtime));

            String year = Util.getYearFromDateStr(obj.getString("release_date"));
            movieModel.setReleaseYear(year);

            movieModel.setBackdropPath(obj.getString("backdrop_url"));

            float average = Float.valueOf(obj.getString("vote_average"));
            movieModel.setVoteAverage(Util.round(average));

            movieModel.setVoteCount(Integer.valueOf(obj.getString("vote_count")));


            List<String> genresList = new ArrayList<>();
            JSONArray genresJsonArray = new JSONArray(obj.getString("genres"));
            for (int i = 0; i < genresJsonArray.length(); i++) {
                genresList.add(genresJsonArray.get(i).toString());
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
}
