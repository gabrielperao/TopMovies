package com.example.topmovies.view;

import com.example.topmovies.R;
import com.example.topmovies.model.MovieModel;
import com.example.topmovies.api.MovieController;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    protected final int N_MOVIES = 20;
    protected final String REQUEST_FAIL_MSG = "Data download failed";

    protected final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    protected final String BASE_IMG_PATH = "https://image.tmdb.org/t/p/original";
    protected final String TOKEN = "beb00a4c8c6361b20da92f6abae277d0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!MovieController.checkConnection(getApplicationContext())) {
            displayNoConnectionMessage();
            return;
        }

        AsyncLoadMoviesTask loadMoviesTask = new AsyncLoadMoviesTask();
        loadMoviesTask.execute(BASE_URL + "top_rated?api_key=" + TOKEN);
    }

    class AsyncLoadMoviesTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            return MovieController.getResponse(url);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response == null) {
                displayNoConnectionMessage();
                return;
            }

            List<MovieModel> movies = MovieController.getTopRatedMovies(response);
            if (movies == null) {
                displayNoConnectionMessage();
                Toast.makeText(getApplicationContext(), REQUEST_FAIL_MSG, Toast.LENGTH_SHORT).show();
                return;
            }

            for (MovieModel movie : movies) {
                updateViewWithMovieInfo(movie);
            }
        }
    }

    protected void updateViewWithMovieInfo(MovieModel movie) {
        final int movieId = movie.getMovieId();
        int indexId = getResources().getIdentifier("moviePosterView" + movie.getIndexId(), "id", getPackageName());
        final ImageView imageView = findViewById(indexId);

        imageView.setVisibility(View.VISIBLE);
        Picasso.get().load(BASE_IMG_PATH + movie.getPosterPath()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                // pass
            }

            @Override
            public void onError(Exception e) {
                imageView.setImageResource(R.drawable.default_movie_poster);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MovieDescActivity.class);
                i.putExtra("id", movieId);
                startActivity(i);
            }
        });

        indexId = getResources().getIdentifier("movieTitleView" + movie.getIndexId(), "id", getPackageName());
        final TextView textView = findViewById(indexId);

        textView.setText(movie.getTitle());
        textView.setVisibility(View.VISIBLE);
    }

    public void displayNoConnectionMessage() {
        for (int i = 0 ; i < N_MOVIES; i++) {
            int imageId = getResources().getIdentifier("moviePosterView" + i, "id", getPackageName());
            int textId = getResources().getIdentifier("movieTitleView" + i, "id", getPackageName());
            final ImageView imageView = findViewById(imageId);
            final TextView textView = findViewById(textId);
            ((ViewGroup) imageView.getParent()).removeView(imageView);
            ((ViewGroup) textView.getParent()).removeView(textView);
        }

        int imageId = getResources().getIdentifier("noConnectionImageView", "id", getPackageName());
        int textId = getResources().getIdentifier("noConnectionMessageView", "id", getPackageName());
        ImageView imageView = findViewById(imageId);
        TextView textView = findViewById(textId);

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
    }
}
