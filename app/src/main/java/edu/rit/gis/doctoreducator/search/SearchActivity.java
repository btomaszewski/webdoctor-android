package edu.rit.gis.doctoreducator.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;

import edu.rit.gis.doctoreducator.R;

public class SearchActivity extends Activity {

    private SearchFrontend mProvider;
    private SearchTask mSearchTask;
    private SearchListAdapter mListAdapter;

    // Views
    private ListView mListView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;
    private TextView mEmptyMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mProvider = new SearchFrontend();
        mProvider.addProvider(new WikipediaSearchProvider(this));

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName searchName = new ComponentName(this, SearchActivity.class);
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
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mEmptyMessage = (TextView) findViewById(R.id.empty);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            mSearchView.setQuery(query, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void displayResults(Collection<ISearchResult> results) {
        mSearchTask = null;
        mListAdapter = new SearchListAdapter(results);
        mListView.setAdapter(mListAdapter);
        showView(mProgressBar, false);
        if (mListAdapter.isEmpty()) {
            showView(mEmptyMessage, true);
        } else {
            showView(mListView, true);
        }
    }

    protected void performSearch(String query) {
        if (mSearchTask == null) {
            showView(mListView, false);
            showView(mEmptyMessage, false);
            showView(mProgressBar, true);
            mSearchTask = new SearchTask(query);
            mSearchTask.execute();
        }
    }

    /**
     * Show view code copied from AccountMain.
     *
     * @param view - view to display or hide
     * @param show - display the view or hide it
     */
    private void showView(final View view, final boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected class SearchTask extends AsyncTask<Void, Void, Collection<ISearchResult>> {

        private SearchCallable mCallable;

        public SearchTask(String query) {
            mCallable = new SearchCallable(mProvider, query);
        }

        @Override
        protected Collection<ISearchResult> doInBackground(Void... params) {
            try {
                return mCallable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Collection<ISearchResult> results) {
            // display the results
            displayResults(results);
        }

        @Override
        protected void onCancelled() {
            // display no results
            // TODO display an actual error for this
            displayResults(Collections.EMPTY_LIST);
        }
    }
}
