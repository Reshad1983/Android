package com.example.reshad.assignment_1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by jlnmsi on 2015-09-01.
 */

public class HeadlinesFragment extends ListFragment {
    AdaptLayoutActivity mCallback;    // Our MainActivity. A reference is need to inform about article selections

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int item_layout = android.R.layout.simple_list_item_activated_1;

        // Create an array adapter for the list view, using the ArticeLibrary headlines array
        setListAdapter(new ArrayAdapter<String>(getActivity(), item_layout, ArticeLibrary.Headlines));
    }
    @Override   // Called when a fragment is first attached to its activity.
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (AdaptLayoutActivity) activity;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(position);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}