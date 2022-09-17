package com.example.topmovies.view;

import com.example.topmovies.api.MovieController;
import com.example.topmovies.model.MovieModel;
import com.example.topmovies.R;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


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

        if (!MovieController.checkConnection(getApplicationContext())) {
            displayNoConnectionMessage();
            return;
        }
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
            // TODO: WRITE DISPLAYNOCONNECTIONMESSAGE() METHOD
            // TODO: UPDATE VALUES FOR ACTIVITY'S VIEWS
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
            ((ViewGroup) view.getParent()).removeView(view);
        }

        int imageId = getResources().getIdentifier("noConnectionImageView", "id", getPackageName());
        int textId = getResources().getIdentifier("noConnectionMessageView", "id", getPackageName());
        ImageView imageView = findViewById(imageId);
        TextView textView = findViewById(textId);

        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
    }
}
