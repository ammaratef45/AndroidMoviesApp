package com.life.ammar.movies;

/**
 * Created by ammar on 19/04/16.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewsAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<MovieReview> reviews;

    public ReviewsAdapter(Context context, ArrayList<MovieReview> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        try {
            return reviews.size();
        } catch (NullPointerException e) {}
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row=layoutInflater.inflate(R.layout.review_item, parent, false);
        MovieReview review=reviews.get(position);
        TextView ReviewAuthor=(TextView)row.findViewById(R.id.authorTV);
        ReviewAuthor.setText(review.getAuthor());
        TextView ReviewContent=(TextView)row.findViewById(R.id.theReviewTV);
        ReviewContent.setText(review.getContent());
        return row;
    }
}

