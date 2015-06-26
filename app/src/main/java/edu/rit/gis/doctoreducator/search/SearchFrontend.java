package edu.rit.gis.doctoreducator.search;

import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * The backend of the search system. It maintains a list of search providers
 * and acts as the gateway to the search system.
 */
public class SearchFrontend implements ISearchProvider, ISearchProvider.SearchListener {

    /**
     * Tag used for logging
     */
    private static final String LOG_TAG = SearchFrontend.class.getName();

    private Set<ISearchProvider> mProviders;
    private LinkedList<ISearchResult> mResultList;
    private SearchListener mListener;
    private int mSearchesLeft;
    private boolean mIsSearching;

    public SearchFrontend() {
        mProviders = new HashSet<>();
        mResultList = new LinkedList<>();
        mSearchesLeft = -1;
        mIsSearching = false;
    }

    @Override
    public synchronized void beginSearch(String text) {
        if(mListener == null) {
            throw new IllegalStateException("Listener is null");
        }
        if(!mIsSearching) {
            mIsSearching = true;
            mResultList.clear();
            mSearchesLeft = mProviders.size();
            for(ISearchProvider provider : mProviders) {
                provider.setListener(this);
                provider.beginSearch(text);
            }
        } else {
            throw new IllegalStateException("Cannot begin search while searching");
        }
    }

    @Override
    public synchronized void cancelSearch(boolean force) {
        if(mIsSearching) {
            mIsSearching = false;
            for(ISearchProvider provider : mProviders) {
                provider.cancelSearch(force);
            }
        }
    }

    @Override
    public void setListener(SearchListener l) {
        mListener = l;
    }

    public synchronized void addProvider(ISearchProvider provider) {
        mProviders.add(provider);
    }

    public synchronized void removeProvider(ISearchProvider provider) {
        mProviders.remove(provider);
    }

    @Override
    public synchronized void onSearchComplete(ISearchProvider provider,
                                              Collection<ISearchResult> results) {
        mResultList.addAll(results);
        providerCompleted();
    }

    @Override
    public synchronized void onSearchError(ISearchProvider provider, Exception cause) {
        // Well we got an error. Log it and move on. Nothing to see here, folks
        Log.e(LOG_TAG, cause.getMessage(), cause);
        providerCompleted();
    }

    /**
     * Decrements the number of search providers still running and checks to see if we've
     * finished all our searches. If we have then send the result to our own listener.
     */
    private void providerCompleted() {
        mSearchesLeft--;
        if(mSearchesLeft == 0) {
            mIsSearching = false;
            mListener.onSearchComplete(this, mResultList);
        }
    }

    public synchronized Collection<ISearchResult> getResults() {
        return mResultList;
    }
}
