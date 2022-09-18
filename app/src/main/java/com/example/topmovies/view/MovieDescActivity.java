package com.example.topmovies.view;

import com.example.topmovies.api.MovieController;
import com.example.topmovies.model.MovieModel;
import com.example.topmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class MovieDescActivity extends AppCompatActivity {

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

        if (!MovieController.hasInternetConnection(getApplicationContext())) {
            displayNoConnectionMessage();
            return;
        }


        Bundle b = getIntent().getExtras();
        int id = b.getInt("id");
        String path = MovieController.BASE_URL + id + "?api_key=" + MovieController.TOKEN;

        AsyncLoadMovieInfoTask loadMovieInfoTask = new AsyncLoadMovieInfoTask();
        loadMovieInfoTask.execute(path);
    }

    class AsyncLoadMovieInfoTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings){
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

            MovieModel movie = MovieController.getMovie(response);
            updateViewsWithMovieInfo(movie);
        }
    }

    private void displayNoConnectionMessage() {

        String[] widgetIds = {
                "movieTitleView",
                "voteAverageView",
                "voteCountView",
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
                "movieBannerView",
                "starView"
        };

        int id;
        for (String idStr : widgetIds) {
            id = getResources().getIdentifier(idStr, "id", getPackageName());
            View view = findViewById(id);
            if (view == null) {
                System.out.println(idStr);
                continue;
            }
            ((ViewGroup) view.getParent()).removeView(view);
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
        setTextForTextView("voteAverageView", String.valueOf(movie.getVoteAverage()));
        setTextForTextView("voteCountView", "(" + movie.getVoteCount() + ")");

        setVisibility("movieTitleView", View.VISIBLE);
        setVisibility("movieOverviewLabelView", View.VISIBLE);
        setVisibility("movieOverviewContentView", View.VISIBLE);
        setVisibility("movieReleaseYearLabelView", View.VISIBLE);
        setVisibility("movieReleaseYearContentView", View.VISIBLE);
        setVisibility("movieGenresLabelView", View.VISIBLE);
        setVisibility("movieGenresContentView", View.VISIBLE);
        setVisibility("movieLanguagesLabelView", View.VISIBLE);
        setVisibility("movieLanguagesContentView", View.VISIBLE);
        setVisibility("movieRuntimeLabelView", View.VISIBLE);
        setVisibility("movieRuntimeContentView", View.VISIBLE);
        setVisibility("voteAverageView", View.VISIBLE);
        setVisibility("voteCountView", View.VISIBLE);
        setVisibility("starView", View.VISIBLE);
    }

    private void setPathForImageView(String path, String idStr) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        final ImageView imageView = findViewById(viewId);

        imageView.setVisibility(View.VISIBLE);
        Picasso.get().load(MovieController.BASE_IMG_PATH + path).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                // pass
            }

            @Override
            public void onError(Exception e) {
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

        String text = "";
        int nElements = list.size();
        for (int i = 0; i < nElements; i++) {
            text += list.get(i);
            if (i < (nElements - 1)) {
                text += ", ";
            }
        }
        textView.setText(text);
    }

    private void setVisibility(String idStr, int visibility) {
        int viewId = getResources().getIdentifier(idStr, "id", getPackageName());
        View view = findViewById(viewId);

        view.setVisibility(visibility);
    }
}
