package com.xeoneux.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movies = new ArrayList<>();
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchMovies("popular");
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
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
