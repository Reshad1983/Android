package com.example.reshad.assignment_1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by jlnmsi on 2015-09-01.
 * A simplified version of FragmentsBasic presented
 * in Android's Fragemnt Tutorial.
 */
public class AdaptLayoutActivity extends FragmentActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_adaptive_layout);

        // Check if activity is using the layout with
        // the fragment_container. If so,  we are
        // in portrait mode and must add headlines fragment
        if (findViewById(R.id.fragment_container) != null) {
            System.out.println("Portrait in AdaptLayout.onCreate(---)");

            // Create and display instance of HeadlineFragment
            HeadlinesFragment firstFragment = new HeadlinesFragment();
            FragmentManager fragMan = getSupportFragmentManager();
            FragmentTransaction transaction = fragMan.beginTransaction();
            transaction.add(R.id.fragment_container, firstFragment);
            transaction.commit();

        }
        else
        {
           // Landscape ==> layout setup in news_article (land)
            System.out.println("Landscape in AdaptLayout.onCreate(---)");
        }
    }

    // Called when user selected a headline of an article from the HeadlinesFragment
    public void onArticleSelected(int position) {

        if (findViewById(R.id.fragment_container) != null) {
            // We're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();      // Transports position to newly created fragment
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();


        } else {
            // We're in two-pane layout...
            ArticleFragment articleFrag = (ArticleFragment)
                    getSupportFragmentManager().findFragmentById(R.id.article_fragment);

            // Call method in the ArticleFragment to update its content
            articleFrag.updateArticleView(position);
        }

    }


}

