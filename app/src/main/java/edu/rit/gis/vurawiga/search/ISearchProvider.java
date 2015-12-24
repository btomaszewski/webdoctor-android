package edu.rit.gis.vurawiga.search;

import java.util.Collection;

/**
 * The {@code SearchProvider} interface provides a way to implement searchable data.
 * Searches should usually be performed asynchronously in a different thread so as not
 * to block the calling thread.
 *
 * None of the interface methods should be called if the SearchProvider does not have
 * a listener attached.
 */
public interface ISearchProvider {

    /**
     * Begin searching for text. This should start the search and then return.
     * Using classes from java.util.concurrent is recommended.
     */
    void beginSearch(String text);

    /**
     * Cancel a search. This should only return when the search provider is in
     * a safe state to begin a new search. If this means blocking until the
     * cancel is complete then do so (though blocking the UI thread is
     * undesirable).
     *
     * If the search is already complete nothing needs to be done.
     *
     * @param force - can we interrupt the task if it's in the middle of running
     */
    void cancelSearch(boolean force);

    /**
     * Set the listener for searches.
     * @param l - listener.
     */
    void setListener(SearchListener l);

    /**
     * Interface for classes that want to deal with {@code SearchProvider}s.
     * A {@code SearchListener} should
     */
    interface SearchListener {

        /**
         * Called when the search is complete.
         *
         * Note that these will be called asynchronously, possibly from another thread
         * so make sure to synchronize on something.
         *
         * @param provider - the search provider calling the method
         * @param results - a collection of search results
         */
        void onSearchComplete(ISearchProvider provider,
                              Collection<ISearchResult> results);

        /**
         * Called when an exception is thrown by the search
         * @param provider - the search provider calling the method
         * @param cause - the exception which caused the error
         */
        void onSearchError(ISearchProvider provider, Exception cause);
    }
}
