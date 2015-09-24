package dv606.lecture4.provider;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import dv606.lecture4.R;

/**
 * An example of working with Content Providers and
 * Loader Manager.
 *
 * Created by Kostiantyn Kucher on 13/09/2015.
 */
public class ContactsActivity extends ListActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * An arbitrary ID to use for the Loader Manager
     */
    public static final int LOADER_MANAGER_ID = 0;

    /**
     *  Content URI used to access contacts
     */
    public static final Uri CONTACTS_URI = ContactsContract.Contacts.CONTENT_URI;

    /**
     * List of columns to include when querying for contacts
     */
    public static final String[] CONTACTS_PROJECTION
            = new String[] {ContactsContract.Contacts._ID,
                            ContactsContract.Contacts.DISPLAY_NAME};

    /**
     * Adapter used to display contacts
     */
    SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the adapter
        listAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1}, 0);
        setListAdapter(listAdapter);

        getLoaderManager().initLoader(LOADER_MANAGER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_MANAGER_ID:
                return new CursorLoader(this, CONTACTS_URI, CONTACTS_PROJECTION,
                        null, null, null);
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_MANAGER_ID:
                listAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_MANAGER_ID:
                listAdapter.swapCursor(null);
                break;
        }
    }
}
