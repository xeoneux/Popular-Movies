package com.xeoneux.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = getIntent().getParcelableExtra("Movie");

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(movie.title);

        String url = "https://image.tmdb.org/t/p/w500/" + movie.moviePoster;
        ImageView imageView = (ImageView) findViewById(R.id.movie_poster);
        Picasso.with(getApplicationContext()).load(url).into(imageView);

        TextView releaseDate = (TextView) findViewById(R.id.release_date);
        releaseDate.setText("Release Date: " + movie.releaseDate);

        TextView voteAverage = (TextView) findViewById(R.id.vote_average);
        voteAverage.setText("Vote Average: " + movie.voteAverage);

        TextView plotSynopsis = (TextView) findViewById(R.id.plot_synopsis);
        plotSynopsis.setText(movie.plotSynopsis);
    }
}
