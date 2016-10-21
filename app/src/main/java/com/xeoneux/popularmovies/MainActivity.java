package com.xeoneux.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    PosterAdapter posterAdapter;
    ArrayList<Movie> movies = new ArrayList<>();
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchMovies("popular");
        gridView = (GridView) findViewById(R.id.grid_view);
        posterAdapter = new PosterAdapter();

        gridView.setAdapter(posterAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("Movie", movie);
                startActivity(intent);
            }
        });
    }

    void fetchMovies(String sortCriteria) {
        String baseUrl = "https://api.themoviedb.org/3/movie/";
        String apiKey = BuildConfig.THE_MOVIE_DATABASE_API_KEY;

        String url = baseUrl + sortCriteria + "/?api_key=" + apiKey;

        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject response = new JSONObject(new String(responseBody));
                    JSONArray results = response.getJSONArray("results");

                    movies.clear();

                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie();

                        JSONObject result = results.getJSONObject(i);

                        movie.title = result.getString("title");
                        movie.releaseDate = result.getString("release_date");
                        movie.moviePoster = result.getString("poster_path");
                        movie.voteAverage = result.getString("vote_average");
                        movie.plotSynopsis = result.getString("overview");

                        movies.add(movie);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    posterAdapter.notifyDataSetChanged();
                    gridView.setAdapter(posterAdapter);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public class PosterAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return movies.size();
        }

        @Override
        public Object getItem(int position) {
            return movies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView = (ImageView) convertView;

            if (imageView == null) {
                imageView = new ImageView(getApplicationContext());
            }

            Movie movie = (Movie) getItem(position);
            String poster = movie.moviePoster;
            String url = "https://image.tmdb.org/t/p/w500/" + poster;

            Picasso
                    .with(getApplicationContext())
                    .load(url)
                    .into(imageView);

            return imageView;
        }
    }
}
