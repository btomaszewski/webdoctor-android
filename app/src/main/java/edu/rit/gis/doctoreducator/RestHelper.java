package edu.rit.gis.doctoreducator;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.rit.gis.doctoreducator.exception.HttpResponseException;

/**
 * Used to create standard HttpURLConnection objects from a few parameters.
 * RestHelper also helps with having to create each URL. You can specify a
 * base URL and then pass in the path for each URL.
 *
 */
public class RestHelper {

    private final static String LOG_TAG = "RestHelper";

    private static final int READ_LENGTH = 1024 * 2;
    private static final String UTF_8 = "UTF-8";

    private static final int defaultBaseUrlId = R.string.url_base;

    /** Holds default headers to add to each request */
    private Map<String,String> headers;

    /** Optional proxy */
    private Proxy proxy;

    /** Chunk size to use when downloading data */
    private int chunkLength;

    /** Android context used for retrieving the baseURL */
    private Context context;

    /**
     * Beginning of the URL used for the REST service.
     * This can be blank but should not be null.
     */
    private String baseURL;

    public RestHelper() {
        headers = new HashMap<>();
        context = null;
        baseURL = "";
        proxy = null;
    }

    public RestHelper(Context ctx) {
        this();
        context = ctx;
        setBaseURL(context.getResources().getString(defaultBaseUrlId));
    }

    public RestHelper(String baseurl) {
        this();
        setBaseURL(baseurl);
    }

    /**
     * Create a URL from the given string. If the RestHelper has a base URL
     * it will be prepended to the given string (if necessary).
     *
     */
    public URL resolve(String s) throws MalformedURLException {
        if(s.startsWith("http://") || s.startsWith("https://")) {
            return new URL(s);
        } else {
            return new URL(baseURL + s);
        }
    }


    /**
     * Make a URL-encoded query string from a map of key-value pairs.
     *
     * @param pairs - set of key-value pairs to URL-encode
     * @return a url-encoded string safe to tack on to the end of a URL
     */
    public String makeQuery(Map<String,String> pairs) {
        try {
            StringBuilder sb = new StringBuilder("?");
            for(Map.Entry<String,String> entry : pairs.entrySet()) {
                sb.append(URLEncoder.encode(entry.getKey(), UTF_8));
                sb.append('=');
                sb.append(URLEncoder.encode(entry.getValue(), UTF_8));
                sb.append('&');
            }
            // remove the last &
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            // umm, not sure what to do if they don't support UTF-8.
            Log.e(LOG_TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Make a URL-encoded query string from pairs of strings
     *
     * e.g. {@code makeQuery("key1", "value1", "key2", "value2");}
     *
     * Such bad practice...<br/>
     * So convenient...
     *
     * @param params - a series of strings representing alternately keys and values
     * @return a URL-encoded query string starting with the ?
     */
    public String makeQuery(String... params) {
        // make sure it's got an even number of items so that we won't have any problems
        // accessing values for each key
        if(params.length % 2 != 0) {
            throw new IndexOutOfBoundsException("Must have even number of parameters");
        }
        try {
            StringBuilder sb = new StringBuilder("?");
            for(int i = 0; i < params.length; i++) {
                // encode key and increment i
                sb.append(URLEncoder.encode(params[i++], UTF_8));
                sb.append('=');
                // encode value but let for loop do i++
                sb.append(URLEncoder.encode(params[i], UTF_8));
                sb.append('&');
            }
            // remove the last &
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Creates a new HttpURLConnection and sets all the default settings.
     *
     * @param url - URL for the HttpURLConnection
     * @return a new HttpURLConnection for the given URL.
     * @throws IOException - if something goes wrong with url.openConnection()
     */
    public HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection result;

        // proxy support
        if (proxy == null) {
            result = (HttpURLConnection)url.openConnection();
        } else {
            result = (HttpURLConnection)url.openConnection(proxy);
        }

        // default timeout should not be never
        if (result.getConnectTimeout() == 0) {
            result.setConnectTimeout(2000);
        }

        for(Map.Entry<String,String> entry : headers.entrySet()) {
            result.setRequestProperty(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Create a new HttpURLConnection with all the headers specified by the
     * RestFactory and using GET.
     *
     * @param url - URL to connect to
     * @return a new HttpURLConnection
     * @throws IOException - when url.openConnection() fails.
     */
    public HttpURLConnection createGET(URL url) throws IOException {
        HttpURLConnection result = createConnection(url);
        result.setRequestMethod("GET");
        return result;
    }

    /**
     * Create a new HttpURLConnection with all the headers specified by the
     * RestFactory and using PUT.
     *
     * @param url - URL to connect to
     * @return a new HttpURLConnection
     * @throws IOException - when url.openConnection() fails.
     */
    public HttpURLConnection createPUT(URL url) throws IOException {
        HttpURLConnection result = createConnection(url);
        result.setDoOutput(true);
        result.setRequestMethod("PUT");
        return result;
    }

    /**
     * Create a new HttpURLConnection with all the headers specified by the
     * RestFactory and using POST.
     *
     * @param url - URL to connect to
     * @return a new HttpURLConnection
     * @throws IOException - when url.openConnection() fails.
     */
    public HttpURLConnection createPOST(URL url) throws IOException {
        HttpURLConnection result = createConnection(url);
        result.setDoOutput(true);
        result.setRequestMethod("POST");
        return result;
    }

    /**
     * Send a GET request to a server.
     *
     * @param url - URL to connect to
     * @return the data sent back from the server as a String
     * @throws IOException - if any IO methods fail
     */
    public String sendGET(URL url) throws IOException {
        HttpURLConnection conn = createGET(url);
        return performNetIO(conn, null, false);
    }

    /**
     * Send a POST request to a server.
     *
     * @param url - URL to connect to
     * @param data - data to send
     * @return the data sent back from the server as a String
     * @throws IOException - if any IO methods fail
     */
    public String sendPOST(URL url, String data) throws IOException {
        HttpURLConnection conn = createPOST(url);
        return performNetIO(conn, data, false);
    }

    /**
     * Send a PUT request to a server.
     *
     * @param url - URL to connect to
     * @param data - data to send
     * @return the data sent back from the server as a String
     * @throws IOException - if any IO methods fail
     */
    public String sendPUT(URL url, String data) throws IOException {
        HttpURLConnection conn = createPUT(url);
        return performNetIO(conn, data, false);
    }

    /**
     * Utility method for dealing with reading and writing data to/from
     * an HttpURLConnection. To only perform a GET make sure data is null.
     *
     * @param conn - The HttpURLConnection to use
     * @param data - Data to send to the server
     * @param raise - true to raise HttpResponseException, false to return the server response
     * @return a String containing the result from the server
     * @throws IOException - when an internal IO call fails
     * @throws HttpResponseException - when the response code is not 200 and raise is true
     */
    public static String performNetIO(HttpURLConnection conn, String data, boolean raise)
            throws IOException {
        try {
            if (data != null) {
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(data.length());
                conn.connect();
                IOUtil.writeString(conn.getOutputStream(), data);
            } else {
                conn.connect();
            }
            if (checkResponseCode(conn.getResponseCode(), raise)) {
                return IOUtil.readString(conn.getInputStream());
            } else if (conn.getErrorStream() != null) {
                return IOUtil.readString(conn.getErrorStream());
            } else {
                throw new HttpResponseException(conn.getResponseCode());
            }
        } finally {
            conn.disconnect();
        }
    }

    private static boolean checkResponseCode(int code, boolean raise)
            throws HttpResponseException {
        boolean result = code / 100 == 2;
        if (!result && raise) {
            // it's not a 20X code so something went wrong
            throw new HttpResponseException(code);
        } else {
            return result;
        }
    }

    /**
     * Set a header value to be added to all created HttpURLConnections.
     */
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Remove a header from the header of each newly-created HttpURLConnection.
     * Does not affect already-existing URLConnections.
     */
    public void removeHeader(String key) {
        headers.remove(key);
    }

    /**
     * Get the default value of a header.
     */
    public String getHeader(String key) {
        return headers.get(key);
    }

    /**
     * Set the proxy which will be used to create each new connection.
     * This will not affect already-existing connections.
     * @param p - a proxy to use for newly created connections
     */
    public void setProxy(Proxy p) {
        this.proxy = proxy;
    }

    /**
     * Change the base URL used when resolving URLs via the resolve()
     * method. This prefix should always start with http:// or https://
     * and should end with a forward slash (/).
     * @param base - prefix to use for resolving URLs
     */
    public void setBaseURL(String base) {
        // we never want baseURL to be null so we set it to the empty string
        if(base == null) {
            baseURL = "";
        } else if(!base.endsWith("/")) {
            // add a / on the end if it doesn't have one to make sure there
            // are no problems with string concatenation latter
            baseURL = base + "/";
        } else {
            baseURL = base;
        }
    }
}
