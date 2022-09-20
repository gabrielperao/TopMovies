package com.example.topmovies.view;

import com.example.topmovies.api.MovieController;
import com.example.topmovies.model.MovieModel;
import com.example.topmovies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setVisibility("progressBar", View.VISIBLE);

        AsyncLoadMoviesTask loadMoviesTask = new AsyncLoadMoviesTask();
        loadMoviesTask.execute();
    }

    class AsyncLoadMoviesTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            return MovieController.getResponse();
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);

            setVisibility("progressBar", View.INVISIBLE);

            if (jsonResponse == null) {
                displayNoConnectionMessage();
                return;
            }

            List<MovieModel> movies = MovieController.getTopRatedMovies(jsonResponse);
            if (movies == null) {
                displayNoConnectionMessage();
                return;
            }

            for (MovieModel movie : movies) {
                updateViewWithMovieInfo(movie);
            }
        }
    }

    protected void updateViewWithMovieInfo(MovieModel movie) {
        final int movieId = movie.getMovieId();
        int viewId = getResources().getIdentifier("moviePosterView" + movie.getIndexId(), "id", getPackageName());
        final ImageView moviePosterView = findViewById(viewId);

        moviePosterView.setContentDescription(movie.getTitle());
        moviePosterView.setVisibility(View.VISIBLE);
        Picasso.get().load(movie.getPosterPath()).into(moviePosterView, new Callback() {
            @Override
            public void onSuccess() {
                // No further action needed
            }

            @Override
            public void onError(Exception e) {
                moviePosterView.setImageResource(R.drawable.default_movie_poster);
            }
        });

        moviePosterView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MovieDescActivity.class);
                i.putExtra("id", movieId);
                startActivity(i);
            }
        });

        viewId = getResources().getIdentifier("movieTitleView" + movie.getIndexId(), "id", getPackageName());
        TextView movieTitleView = findViewById(viewId);
        movieTitleView.setText(movie.getTitle());
        movieTitleView.setContentDescription(movie.getTitle());
        movieTitleView.setVisibility(View.VISIBLE);
    }

    public void displayNoConnectionMessage() {
        for (int i = 0 ; i < MovieController.N_MOVIES; i++) {
            removeView("moviePosterView" + i);
            removeView("movieTitleView" + i);
        }

        setVisibility("noConnectionImageView", View.VISIBLE);
        setVisibility("noConnectionTextView", View.VISIBLE);
    }

    private void setTextForTextView(String idStr, String text) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        TextView textView = findViewById(viewId);
        textView.setText(text);
    }

    private void removeView(String idStr) {
        setContentView(R.layout.activity_main);
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        View view = findViewById(viewId);
        ((ViewGroup) view.getParent()).removeView(view);
    }

    private void setVisibility(String idStr, int visibility) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        View view = findViewById(viewId);

        view.setVisibility(visibility);
    }
}
