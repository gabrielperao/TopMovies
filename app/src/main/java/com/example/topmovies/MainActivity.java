package com.example.topmovies;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    public final int nMovies = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AsyncLoadBannersTask task = new AsyncLoadBannersTask();
        task.execute(nMovies);
    }

    class AsyncLoadBannersTask extends AsyncTask<Integer, Integer, String>{
        @Override
        protected String doInBackground(Integer... integers) {
            int nMovieBanners = integers[0];
            for (int i = 0; i < nMovieBanners; i++) {
                publishProgress(i);
            }
            return "Finalizado";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            int id = getResources().getIdentifier("movieBannerView" + values[0], "id", getPackageName());
            final ImageView imageView = findViewById(id);
            imageView.setVisibility(View.VISIBLE);

            String url;
            if (values[0] % 3 == 0) url = "https://image.tmdb.org/t/p/original/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg";
            else url = "https://image.tmdb.org/t/p/original/q6y0Go1tsGEsmtFryDOJo3dEmqu";

            Picasso.get().load(url).into(imageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    imageView.setImageResource(R.drawable.default_movie_banner);
                }
            });

            // TODO: trocar url da image
            // TODO: usar o Picasso para pegar imagem direto do server
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, MovieDescActivity.class);
                    startActivity(i);
                }
            });
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    }
}
