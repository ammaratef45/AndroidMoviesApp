package com.life.ammar.movies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {
    ArrayList<MovieReview> reviews = new ArrayList<>();
    public DetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        final Realm realm = Realm.getInstance(getContext());
        try {
            reviews.clear();
        } catch (Exception e) {}
        int id = getArguments().getInt("idAsInt");
        try {
            RealmResults<MovieReview> reviewRealmResults = realm.where(MovieReview.class).equalTo("movieId", id).findAll();
            for(int i=0; i<reviewRealmResults.size(); i++) {
                reviews.add(reviewRealmResults.get(i));
            }
            final MovieEntry results = realm.where(MovieEntry.class).equalTo("id", id).findFirst();
            ImageView BackImage = (ImageView) rootView.findViewById(R.id.backIV);
            TextView title = (TextView) rootView.findViewById(R.id.titleTextView);
            TextView overView = (TextView) rootView.findViewById(R.id.overviewTextView);
            TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDateTextView);
            final ImageView favouriteImage = (ImageView) rootView.findViewById(R.id.favouriteImage);
            ImageView posterImage = (ImageView) rootView.findViewById(R.id.posterImage);
            TextView voteAvg =(TextView) rootView.findViewById(R.id.voteAvgTextView);
            TextView trailer = (TextView) rootView.findViewById(R.id.trailerTV);
            final ListView reviewsList = (ListView) rootView.findViewById(R.id.reviewsList);
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), reviews);
            reviewsList.setAdapter(reviewsAdapter);
            reviewsList.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }

            });
            final MovieTrailer movieTrailer = realm.where(MovieTrailer.class).equalTo("movieId",results.getId()).findFirst();
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/" + "w500" + String.valueOf(results.getBackdropPath())).into(BackImage);
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/" + "w500" + String.valueOf(results.getPosterPath())).into(posterImage);
            title.setText(results.getTitle());
            voteAvg.setText("Vote: " + results.getVoteAverage() + "/10.0");
            overView.setText(results.getOverview());
            releaseDate.setText(results.getReleaseDate());
            if(results.getFavourite()) {
                favouriteImage.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            favouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(results.getFavourite()) {
                        realm.beginTransaction();
                        results.setFavourite(false);
                        realm.copyToRealmOrUpdate(results);
                        realm.commitTransaction();
                        favouriteImage.setBackgroundColor(0xffffff);
                    } else {
                        realm.beginTransaction();
                        results.setFavourite(true);
                        realm.copyToRealmOrUpdate(results);
                        realm.commitTransaction();
                        favouriteImage.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            });
            trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + movieTrailer.getKey()));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "No trailers available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (NullPointerException e) {}
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
