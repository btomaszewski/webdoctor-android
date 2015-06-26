package edu.rit.gis.doctoreducator.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;

/**
 *
 */
public interface ISearchResult {

    /**
     * Open the search result as a fragment or launch a new activity using an intent.
     *
     * @param parent - parent of the Fragment or Activity to open
     */
    void open(ITaskChanger parent);

    /**
     * Get a preview to display to the user. The preview should be the size specified
     * by {@code size}.
     *
     *
     * @param context - context used to create a new view if necessary
     * @param size - size of the preview box and of the Bitmap to return
     * @param convertView - view to reuse, make sure to check if it's null and if the type is
     *                    correct
     * @return a bitmap of size {@code size} containing a preview of the search result
     */
    View getView(Context context, Point size, View convertView);

    /**
     * Returns the location as a human-readable text string.
     * e.g. "example.pdf - Page 5"
     *
     * @return the location as a human-readable text string
     */
    String getLocation();

    /**
     * Returns a number between 0.0 and 1.0 representing how close the match is to
     * the original search string. For now all implementations should return 1
     *
     * @return how close the match is to the original search string.
     */
    float getMatchPercent();
}
