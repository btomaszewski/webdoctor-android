package edu.rit.gis.doctoreducator.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import edu.rit.gis.doctoreducator.AssetManager;
import edu.rit.gis.doctoreducator.R;

public class SearchActivity extends Activity implements
        ITaskChanger, ISearchProvider.SearchListener {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private SearchFrontend mProvider;
    private SearchListAdapter mListAdapter;

    // Views
    private ListView mListView;
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mProvider = new StreamingSearcher();
        mProvider.setListener(this);
        populateProviders();

        mSearchView = (SearchView) findViewById(R.id.searchView);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ISearchResult result = (ISearchResult) mListAdapter.getItem(position);
                result.open(SearchActivity.this);
            }
        });

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            mSearchView.setQuery(query, true);
        }
    }


    private void populateProviders() {
        mProvider.addProvider(new WikipediaSearchProvider(this));

        try {
            AssetManager assetManager = new AssetManager(this);
            for (JSONObject obj : assetManager.getCurrentAssets().values()) {
                if (obj.getString("type").equals("pdf")) {
                    File file = assetManager.determineFile(obj);
                    mProvider.addProvider(new PdfSearchProvider(this, file.getAbsolutePath()));
                }
            }
        } catch (IOException|JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void performSearch(String query) {
        if (mProvider.isSearching()) {
            mProvider.cancelSearch(true);
        }
        mListAdapter = new SearchListAdapter();
        mListView.setAdapter(mListAdapter);
        mProvider.beginSearch(query);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onSearchComplete(ISearchProvider provider, Collection<ISearchResult> results) {
        // do nothing
    }

    @Override
    public void onSearchError(ISearchProvider provider, Exception cause) {
        // also do nothing
    }

    /**
     * Extends SearchFrontend to call back and update the result list as results are returned.
     */
    private class StreamingSearcher extends SearchFrontend {
        @Override
        public void onSearchComplete(ISearchProvider provider,
                                     final Collection<ISearchResult> results) {
            super.onSearchComplete(provider, results);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListAdapter.addAll(results);
                }
            });
        }
    }
}
