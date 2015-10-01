package com.example.reshad.assignment_1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jlnmsi on 2015-08-27.
 */
public class ExampleFragment extends Fragment {
    final static String HEADER_TEXT = "header_text";
    final static String BODY_TEXT = "body_text";
    private ViewGroup rootView;



    public static ExampleFragment create(String header, BeerDetails body) {
        ExampleFragment newFragment = new ExampleFragment();
        Bundle args = new Bundle();
        args.putString(HEADER_TEXT, header);
        args.putParcelable(BODY_TEXT, body);
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout containing a header and body text.
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_layout, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the header and body text.

        String headerText;
        BeerDetails bodyText;
        Bundle args = getArguments();
        if (args != null) {
            headerText = args.getString(HEADER_TEXT);
            bodyText = args.getParcelable(BODY_TEXT);
        }
        else {
            headerText = "Default Header";
            bodyText =  new BeerDetails(R.drawable.pliny_the_younger,"Pliny The Younger",
                    "Russian River Brewing Company, California, United States",
                    "4.68", "11.00%", "American Double / Imperial IPA",
                    "Had this on opening day Feb 6, 2015 after waiting in line for 4.5 hrs in the pouring rain. It is a spectacular beer to have straight from the source. Although a very high abv it does not taste alcohol forward and is truly balanced with a good amount of hops on your tongue, a bit of grapefruit follow through and a floral scent. Had a great time drying off and warming up and 3 10oz pours along with some Elder and Blind Pig hit the spot. It is a special beer worth the effort.\n"
            );
        }

        // Set the header and body text.
        ((TextView) rootView.findViewById(R.id.header_view)).setText(headerText);

        ((ImageView)rootView.findViewById(R.id.image_id)).setBackgroundResource(bodyText.getImage_id());
        ((TextView) rootView.findViewById(R.id.body_view)).setText(bodyText.getBeer_name() +"\n"+
        bodyText.getRating() +"\n"+
        bodyText.getBeerName()+"\n"+
        bodyText.getStyling()+"\n"+
        bodyText.getAbv_value() +"\n"+
        bodyText.getReview());

    }

}
