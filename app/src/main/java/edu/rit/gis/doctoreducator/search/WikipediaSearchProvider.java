package edu.rit.gis.doctoreducator.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.RestHelper;

/**
 * Provides search support for Wikipedia and related wiki sites which use the WikiMedia API.
 *
 * Documentation for the API can be found at https://www.mediawiki.org/wiki/API:Main_page
 * You can also find the wikipedia api reference at https://en.wikipedia.org/w/api.php
 *
 * The api url for wikipedia is "wikipedia.org/w/api.php".
 * The api url for a given wiki site may not be the same.
 */
public class WikipediaSearchProvider extends BaseSearchProvider {

    /** Number of search results to ask for at once.
     * This is a string because it has to be. */
    private static final String RESULT_LIMIT = "20";

    /** Rest helper to use in making api calls */
    private RestHelper mRest;
    /** Base url to use when creating the url for a particular page */
    private String mWikiUrl;

    /**
     * Create a search provider to search wikipedia. This includes default parameters
     * which search Wikipedia. To use a different WikiMedia-based site use the other
     * constructor.
     *
     * @param context - context used to get the user-agent from R.string.user_agent
     */
    public WikipediaSearchProvider(Context context) {
        this("https://en.wikipedia.org/w/api.php", "https://en.wikipedia.org/wiki/",
                context.getString(R.string.user_agent));
    }

    /**
     * Create a search provider which searches using the wikimedia api
     * with the api endpoint at the given url.
     *
     * @param apiUrl - url for accessing the mediawiki api
     * @param wikiUrl - base url to use when creating the url for a particular page
     * @param userAgent - the user-agent to use when making requests from the mediawiki api
     */
    public WikipediaSearchProvider(String apiUrl, String wikiUrl, String userAgent) {
        mRest = new RestHelper(apiUrl);
        mRest.setHeader("User-Agent", userAgent);
        mRest.setHeader("Accept", "application/json");
        mWikiUrl = wikiUrl;
    }

    @Override
    protected Collection<ISearchResult> performSearch(String text) {
        /*
         * After doing some research I finally found out how to do a search of wikipedia.
         * A good example can be found at
         * http://blog.comperiosearch.com/blog/2012/06/27/make-an-instant-search-application-using-json-ajax-and-jquery/
         */
        try {
            List<ISearchResult> results = new ArrayList<>();
            JSONObject json = makeSearch(text, 0);
            if (json.has("error")) {
                JSONObject error = json.getJSONObject("error");
                throw new IOException(error.getString("info"));
            } else {
                JSONArray resultList = json.getJSONObject("query").getJSONArray("search");
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject item = resultList.getJSONObject(i);
                    String title = item.getString("title");
                    String snippet = stripHtml(item.getString("snippet"));
                    results.add(new WikipediaResult(title, snippet));
                }
            }
            return results;
        } catch (IOException | JSONException e) {
            // deal with it the easy way: pass it on
            throw new RuntimeException(e);
        }
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    private JSONObject makeSearch(String text, int resultOffset) throws IOException, JSONException {
        String query = mRest.makeQuery(
                "srsearch", text,
                "action", "query",
                "list", "search",
                "format", "json",
                "srprops", "snippet",
                "srlimit", RESULT_LIMIT,
                "sroffset", Integer.toString(resultOffset));
        HttpURLConnection conn = mRest.createGET(mRest.resolve(query));
        conn.connect();
        return new JSONObject(RestHelper.readStreamToString(conn.getInputStream()));
    }

    private void openWebsite(Context context, String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    protected class WikipediaResult implements ISearchResult {

        private String mTitle;
        private String mSnippet;
        private String mViewText;

        public WikipediaResult(String title, String snippet) {
            mTitle = title;
            mSnippet = snippet;
            mViewText = "Wikipedia: " + mTitle + "\n" + mSnippet;
        }

        @Override
        public void open(ITaskChanger parent) {
            String urlString = mWikiUrl + mTitle;
            openWebsite(parent.getActivity(), urlString);
        }

        @Override
        public View getView(Context context, Point size, View convertView) {
            TextView view;
            if (convertView != null && convertView instanceof TextView) {
                view = (TextView) convertView;
            } else {
                view = new TextView(context);
            }
            view.setMaxLines(3);
            view.setText(mViewText);
            return view;
        }

        @Override
        public String getLocation() {
            return "Wikipedia: " + mTitle;
        }

        @Override
        public float getMatchPercent() {
            return 1;
        }
    }
}
