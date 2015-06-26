package edu.rit.gis.doctoreducator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * A wrapper around HttpURLConnection which simplifies sending and retrieving
 * data from it.
 *
 * This class is not yet implemented and might not ever be. RestHelper is good enough
 * for now and has pretty much everything you need.
 */
public class RestConnection {

    /** The underlying HttpURLConnection */
    private HttpURLConnection connection;

    public RestConnection(HttpURLConnection conn) {
        connection = conn;
        // assume we will do output
        connection.setDoOutput(true);
    }

    public RestConnection(URL url) throws IOException {
        this((HttpURLConnection) url.openConnection());
    }

    public void send(String data) throws IOException {
        connection.connect();
        RestHelper.writeStringToStream(connection.getOutputStream(), data);
    }

    public void send(JSONObject obj) throws IOException {
        send(obj.toString());
    }

    public void send(JSONArray list) throws IOException {
        send(list.toString());
    }

    public String receiveString() throws IOException {
        connection.connect();
        return RestHelper.readStreamToString(connection.getInputStream());
    }

    public void setRequestHeader(String key, String value) {
        connection.setRequestProperty(key, value);
    }

    public void setRequestMethod(String method) throws ProtocolException {
        connection.setRequestMethod(method);
    }

    public int getResponseCode() throws IOException {
        connection.connect();
        return connection.getResponseCode();
    }

    public HttpURLConnection getConnection() {
        return connection;
    }
}
