package edu.rit.gis.doctoreducator.account;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import edu.rit.gis.doctoreducator.RestHelper;
import edu.rit.gis.doctoreducator.exception.HttpResponseException;
import edu.rit.gis.doctoreducator.exception.RegistrationException;

/**
 * Handles pretty much everything related to the account.
 */
public class AccountHelper {

    /**
     * Key for the auth token which is stored in the App's Shared Preferences.
     */
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * Name of Shared Preferences
     */
    public static final String SHARED_PREFERENCES = "edu.rit.gis.doctoreducator.sharedprefs";

    // BEGIN PRIVATE VARIABLES

    private Context mContext;

    // END PRIVATE VARIABLES

    public AccountHelper(Context context) {
        mContext = context;
    }

    /**
     * Test to see if our stored credentials allow us to authenticate.
     *
     * @return true if we can authenticate, false otherwise
     * @throws IOException - when an IO error occurs
     * @throws JSONException - if the JSON is invalid
     */
    public boolean isAuthenticated() throws IOException, JSONException {
        RestHelper rest = new RestHelper(mContext);
        String authToken = getAuthToken();
        if (!authToken.equals("")) {
            rest.setHeader("Authorization", "Token " + authToken);
        }
        JSONObject json = new JSONObject(rest.sendGET(rest.resolve("login/test/")));
        // if we have "result" then return that otherwise there was an authentication error
        // (likely error 401) so return false. If there is a real network error an IOException
        // will still be thrown by the rest.sendGET method.
        return json.optBoolean("result", false);
    }

    /**
     * Authenticate with the server using a username (email) and password combination.
     *
     * When authentication succeeds we store the new authentication token for reuse.
     *
     * @param email - user's email
     * @param password - user's password
     * @return true if auth succeeded and false if it failed
     * @throws IOException - when an IO error occurs
     * @throws JSONException - if the JSON is invalid
     */
    public boolean login(String email, String password) throws IOException, JSONException {
        RestHelper rest = new RestHelper(mContext);

        JSONObject authObject = new JSONObject();
        authObject.put("username", email);
        authObject.put("password", password);

        JSONObject result = new JSONObject(rest.sendPOST(
                rest.resolve("login/auth-token/"),
                authObject.toString()));

        if(result.has("token")) {
            // we've authenticated so store the token
            setAuthToken(result.getString("token"));
            return true;
        } else {
            // auth failed
            return false;
        }
    }

    /**
     * Register a new account with the server.
     *
     * If this returns then it succeeded. Otherwise it will throw a RegistrationException
     * with details on why the registration failed.
     *
     * @param name - user's full name
     * @param email - user's email
     * @param password - user's password
     * @throws IOException - if a network error occurs
     * @throws JSONException - if the JSON is invalid somehow
     * @throws RegistrationException - when registration fails for any reason
     */
    public void register(String name, String email, String password)
        throws IOException, JSONException, RegistrationException {
        RestHelper rest = new RestHelper(mContext);

        JSONObject authObject = new JSONObject();
        authObject.put("name", name);
        authObject.put("username", email);
        authObject.put("password", password);

        JSONObject result = new JSONObject(rest.sendPOST(
                rest.resolve("/login/register/"),
                authObject.toString()));

        if(result.has("token")) {
            // we've authenticated so store the token
            setAuthToken(result.getString("token"));
        } else {
            // register failed
            if(result.has("error")) {
                throw new RegistrationException(result.getString("error"));
            } else {
                throw new RegistrationException("Registration failed");
            }
        }
    }

    /**
     * Get the currently stored auth token. Returns an empty string if
     * we don't currently have an auth token.
     */
    public String getAuthToken() {
        SharedPreferences preferences = mContext.getSharedPreferences(
                SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(AUTH_TOKEN_KEY, "");
    }

    /**
     * Change the stored auth token.
     *
     * @param token - new value for the token
     */
    private void setAuthToken(String token) {
        SharedPreferences preferences = mContext.getSharedPreferences(
                SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AccountMain.AUTH_TOKEN_KEY, token);
        editor.apply();
    }
}
