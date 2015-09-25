package edu.rit.gis.doctoreducator.account;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.rit.gis.doctoreducator.utils.IOUtil;
import edu.rit.gis.doctoreducator.utils.RestHelper;
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


    // These parts of the class are static but are private so you still need
    // to create an instance of the class to access them

    /**
     * Have we already tried to authenticate via the internet
     */
    private static boolean hasTriedAuthentication = false;

    /**
     * Are we authenticated.
     */
    private static boolean hasAuthenticated = false;

    /**
     * Non-static Context for accessing the shared preferences
     */
    private Context mContext;

    public AccountHelper(Context context) {
        mContext = context;
    }


    /**
     * Check to see if we've authenticated. Only uses cached value and does not attempt
     * to communicate with the server.
     *
     * This is mostly useful if you are sure {@code testAuthentication} has been called
     * at least once.
     *
     * @return true if we've authenticated false if not
     */
    public boolean isAuthenticated() {
        return hasAuthenticated;
    }

    /**
     * Check to see if we've authenticated. If we have already attempted authenticated then
     * return the result of that, if not run {@code testAuthentication()} and return the result
     * of that.
     *
     * @return true if we've authenticated
     * @throws IOException - when testAuthentication throws an IOException
     * @throws JSONException - when testAuthentication throws a JSONException
     */
    public boolean isAuthenticated(boolean testIfNot) throws IOException, JSONException {
        if (hasTriedAuthentication || !testIfNot) {
            return hasAuthenticated;
        } else {
            return testAuthentication();
        }
    }

    /**
     * Test to see if our stored credentials allow us to authenticate. This always
     * accesses the server and checks stored credentials.
     *
     * @return true if we can authenticate, false otherwise
     * @throws IOException - when an IO error occurs
     * @throws JSONException - if the JSON is invalid
     */
    public boolean testAuthentication() throws IOException, JSONException {
        try {
            RestHelper rest = IOUtil.configureJson(new RestHelper(mContext));
            String authToken = getAuthToken();
            if (!authToken.equals("")) {
                rest.setHeader("Authorization", "Token " + authToken);
            }
            JSONObject json = new JSONObject(rest.sendGET(rest.resolve("login/test/")));
            setAuthenticated(json.optBoolean("result", false));
        } catch (HttpResponseException e) {
            // if 401 (authentication) error then our token was invalid so
            // set result to false
            if (e.getErrorCode() == 401) {
                setAuthenticated(true);
            } else {
                throw e;
            }
        }
        return hasAuthenticated;
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
        RestHelper rest = IOUtil.configureJson(new RestHelper(mContext));

        JSONObject authObject = new JSONObject();
        authObject.put("username", email);
        authObject.put("password", password);

        JSONObject result = new JSONObject(rest.sendPOST(
                rest.resolve("login/auth-token/"),
                authObject.toString()));

        if(result.has("token")) {
            // we've authenticated so store the token
            setAuthToken(result.getString("token"));
            setAuthenticated(true);
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
        RestHelper rest = IOUtil.configureJson(new RestHelper(mContext));

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
            setAuthenticated(true);
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
     * Reset the stored authentication token. Yep. That's really all this does.
     * This effectively logs you out of the system.
     */
    public void logout() {
        setAuthToken("");
        setAuthenticated(false);
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
        preferences.edit().putString(AUTH_TOKEN_KEY, token).apply();
    }

    /**
     * Set if we have successfully authenticated with the server. This will
     * set hasTriedAuthenticated to true and set hasAuthenticated to auth.
     *
     * @param auth - did we authenticate successfully
     */
    private void setAuthenticated(boolean auth) {
        hasTriedAuthentication = true;
        hasAuthenticated = auth;
    }
}
