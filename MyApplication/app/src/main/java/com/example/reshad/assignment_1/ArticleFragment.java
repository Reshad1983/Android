package com.example.reshad.assignment_1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jlnmsi on 2015-09-01. Reuses the layout
 * from ExampleFragement.
 */
public class ArticleFragment extends Fragment {

    final static String ARG_POSITION = "position";
    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_layout, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        }

    }

    public void updateArticleView(int position) {

        ((TextView) rootView.findViewById(R.id.header_view)).setText(ArticeLibrary.Headlines[position]);
        ((ImageView)rootView.findViewById(R.id.image_id)).setBackgroundResource(ArticeLibrary.Articles[position].getImage_id());
        ((TextView) rootView.findViewById(R.id.body_view)).setText("Name: " + ArticeLibrary.Articles[position].getBeer_name() + "\n" +
                "Rating: " + ArticeLibrary.Articles[position].getRating() + "\n" +
                "Brewery: " + ArticeLibrary.Articles[position].getBrewery() + "\n" +
                "Style: " + ArticeLibrary.Articles[position].getStyling() + "\n" +
                "ABV: " + ArticeLibrary.Articles[position].getAbv_value() + "\n" +
                "Review: "+ ArticeLibrary.Articles[position].getReview());
   //     mCurrentPosition = position;
    }

}