package com.example.topmovies.view;

import com.example.topmovies.api.MovieController;
import com.example.topmovies.model.MovieModel;
import com.example.topmovies.util.Util;
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

import java.util.List;


public class MovieDescActivity extends AppCompatActivity {

    private String[] widgetIds = {
            "movieTitleView",
            "movieVoteAverageView",
            "movieVoteCountView",
            "movieOverviewLabelView",
            "movieOverviewContentView",
            "movieReleaseYearLabelView",
            "movieReleaseYearContentView",
            "movieGenresLabelView",
            "movieGenresContentView",
            "movieLanguagesLabelView",
            "movieLanguagesContentView",
            "movieRuntimeLabelView",
            "movieRuntimeContentView",
            "backArrowImageView",
            "moviePosterView",
            "starView"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_desc);


        ImageView backArrowImageView = findViewById(R.id.backArrowImageView);
        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });

        if (Util.hasNoConnection(getApplicationContext())) {
            displayNoConnectionMessage(false);
            return;
        }

        setVisibility("progressBar", View.VISIBLE);

        // Getting movie id passed as 'parameter' to the current activity
        // It will be used to send a request to the API and get the movie's detailed information
        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");

        AsyncLoadMovieInfoTask loadMovieInfoTask = new AsyncLoadMovieInfoTask();
        loadMovieInfoTask.execute(String.valueOf(id));
    }

    class AsyncLoadMovieInfoTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings){
            String url = strings[0];
            return MovieController.getResponse(url);
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);

            setVisibility("progressBar", View.INVISIBLE);

            MovieModel movie;
            if (jsonResponse == null || (movie = MovieController.getMovie(jsonResponse)) == null) {
                displayNoConnectionMessage(true);
                return;
            }

            updateViewsWithMovieInfo(movie);
        }
    }

    private void displayNoConnectionMessage(boolean requestFail) {

        int id;
        for (String idStr : widgetIds) {
            id = getResources().getIdentifier(idStr, "id", getPackageName());
            View view = findViewById(id);
            if (view == null) {
                continue;
            }
            ((ViewGroup) view.getParent()).removeView(view);
        }

        if (requestFail) {
            setTextForTextView("noConnectionView", MovieController.REQUEST_FAIL_MSG);
        }
        setVisibility("noConnectionImageView", View.VISIBLE);
        setVisibility("noConnectionTextView", View.VISIBLE);
    }

    private void updateViewsWithMovieInfo(MovieModel movie) {
        String path = movie.getBackdropPath();
        setPathForImageView(path, "moviePosterView");

        setTextForTextView("movieTitleView", movie.getTitle());
        setTextForTextView("movieOverviewContentView", movie.getOverview());
        setTextForTextView("movieReleaseYearContentView", movie.getReleaseYear());
        setTextForTextView("movieGenresContentView", movie.getGenres());
        setTextForTextView("movieLanguagesContentView", movie.getLanguages());
        setTextForTextView("movieRuntimeContentView", movie.getRuntime());
        setTextForTextView("movieVoteAverageView", String.valueOf(movie.getVoteAverage()));
        setTextForTextView("movieVoteCountView", "(" + movie.getVoteCount() + ")");

        for (String id : widgetIds) {
            setVisibility(id, View.VISIBLE);
        }
    }

    private void setPathForImageView(String path, String idStr) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        final ImageView imageView = findViewById(viewId);

        imageView.setVisibility(View.VISIBLE);
        Picasso.get().load(path).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                // No further action needed
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.default_movie_poster);
            }
        });
    }

    private void setTextForTextView(String idStr, String text) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        TextView textView = findViewById(viewId);
        textView.setText(text);
    }

    private void setTextForTextView(String idStr, List<String> list) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        TextView textView = findViewById(viewId);

        StringBuilder strBuilder = new StringBuilder();
        int nElements = list.size();
        for (int i = 0; i < nElements; i++) {
            strBuilder.append(list.get(i));
            if (i < (nElements - 1)) {
                strBuilder.append(", ");
            }
        }
        textView.setText(strBuilder.toString());
    }

    private void setVisibility(String idStr, int visibility) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        View view = findViewById(viewId);

        view.setVisibility(visibility);
    }
}
