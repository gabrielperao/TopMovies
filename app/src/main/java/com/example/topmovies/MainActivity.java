package com.example.topmovies;
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

    public final int nMovies = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        AsyncChangeVisibilityTask task = new AsyncChangeVisibilityTask();
        task.execute(nMovies);
    }

    class AsyncChangeVisibilityTask extends AsyncTask<Integer, Integer, String>{
        @Override
        protected String doInBackground(Integer... integers) {
            int nMovieBanners = integers[0];
            for (int i = 0; i < nMovieBanners; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finalizado";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int id = getResources().getIdentifier("imageView" + values[0], "id", getPackageName());
            ImageView imageView = findViewById(id);
            imageView.setVisibility(View.VISIBLE);
            // TODO: trocar url da image
            // TODO: usar o Picasso para pegar imagem direto do server
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    System.out.println("Clicou");
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
