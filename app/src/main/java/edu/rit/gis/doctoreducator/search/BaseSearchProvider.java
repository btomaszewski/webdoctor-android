package edu.rit.gis.doctoreducator.search;

import android.util.Log;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.rit.gis.doctoreducator.Util;


public abstract class BaseSearchProvider implements ISearchProvider {

    private static final String LOG_TAG = BaseSearchProvider.class.getName();

    /**
     * Executor service used by default for running search tasks.
     * We create it as a fixed thread pool with 1 thread per processor.
     */
    private static ExecutorService executor;

    private SearchListener mListener;
    private Future<?> mTask;

    static {
        int numCores = Util.getNumCores();
        Log.i(LOG_TAG, "Num cores = " + numCores);
        executor = Executors.newFixedThreadPool(numCores);
    }

    @Override
    public void beginSearch(String text) {
        mTask = executor.submit(new BaseSearchTask(text));
    }

    @Override
    public void cancelSearch(boolean force) {
        if(mTask != null) {
            mTask.cancel(force);
        }
    }

    @Override
    public void setListener(SearchListener l) {
        mListener = l;
    }

    /**
     * Method to be implemented by subclasses which will be called to actually
     * perform the search. This will be called asynchronously on a different
     * thread.
     *
     * @param text - text to search for
     * @return collection of results, empty if there are no results
     */
    protected abstract Collection<ISearchResult> performSearch(String text);

    /**
     * An Runnable which simply runs performSearch and returns the result.
     */
    protected class BaseSearchTask implements Runnable {

        private String mText;

        public BaseSearchTask(String text) {
            mText = text;
        }

        @Override
        public void run() {
            try {
                Collection<ISearchResult> results = performSearch(mText);
                mListener.onSearchComplete(BaseSearchProvider.this, results);
            } catch (Exception e) {
                // log exception and call onSearchComplete with null as value
                Log.e(LOG_TAG, e.getMessage(), e);
                mListener.onSearchError(BaseSearchProvider.this, e);
            } finally {
                // reset our task to null so we know we're done
                mTask = null;
            }
        }
    }
}
