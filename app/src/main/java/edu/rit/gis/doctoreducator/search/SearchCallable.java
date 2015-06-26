package edu.rit.gis.doctoreducator.search;

import java.util.Collection;
import java.util.concurrent.Callable;

/**
 * A class which wraps around a search provider and handles the callbacks and such.
 * The purpose it to facilitate a synchronous search query.
 *
 * This can easily be wrapped in an AsyncTask for explicit use with the UI thread.
 */
public class SearchCallable implements Callable<Collection<ISearchResult>>,
        ISearchProvider.SearchListener {

    /** Search provider */
    private ISearchProvider mProvider;
    /** Search query */
    private String mQuery;
    /** Lock to synchronize on */
    private Object mLock = new Object();
    /** The results of the search. This should only be accessed if you have the lock. */
    private Collection<ISearchResult> mResults;
    /** If an error occurs we record it in mError. Only access if you have the lock. */
    private Exception mError;

    public SearchCallable(ISearchProvider provider, String query) {
        mProvider = provider;
        mQuery = query;
    }

    @Override
    public Collection<ISearchResult> call() throws Exception {
        mProvider.setListener(this);
        mProvider.beginSearch(mQuery);
        synchronized (mLock) {
            while(mResults == null && mError == null) {
                mLock.wait();
            }
            if (mError != null) {
                throw mError;
            } else {
                return mResults;
            }
        }

    }

    @Override
    public void onSearchComplete(ISearchProvider provider, Collection<ISearchResult> results) {
        synchronized (mLock) {
            mResults = results;
            mLock.notifyAll();
        }
    }

    @Override
    public void onSearchError(ISearchProvider provider, Exception cause) {
        synchronized (mLock) {
            mError = cause;
            mLock.notifyAll();
        }
    }
}
