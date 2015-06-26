package edu.rit.gis.doctoreducator.account;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLClassLoader;
import java.net.URLConnection;

import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.RestHelper;
import edu.rit.gis.doctoreducator.main.MainActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountMain extends Activity implements View.OnClickListener {

    /**
     * Tag to use for logging.
     * We use AccountMain.class.getName() so that if we rename the class
     * the IDE will change LOG_TAG automatically with the refactor.
     */
    private static final String LOG_TAG = AccountMain.class.getName();

    /**
     * Sub-intents should set this as their result code when they succeed at logging in.
     * This tells AccountMain to start up the main activity and end itself.
     */
    public static final int RESULT_AUTH_SUCCESS = RESULT_FIRST_USER + 1;

    /**
     * Key for the auth token which is stored in the App's Shared Preferences.
     */
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private CheckAuthTask mAuthTask;

    private ProgressBar mProgressView;
    private TextView mTextDisplay;
    private Button mButtonSignIn;
    private Button mButtonRegister;
    private Button mButtonVerify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_main);

        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);
        mTextDisplay = (TextView) findViewById(R.id.text_display);
        mButtonSignIn = (Button) findViewById(R.id.button_sign_in);
        mButtonRegister = (Button) findViewById(R.id.button_register);
        mButtonVerify = (Button) findViewById(R.id.button_verify);

        mButtonSignIn.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
        mButtonVerify.setOnClickListener(this);

        showProgress(true);
        attemptLogin();
    }

    @Override
    public void onClick(View v) {
        // if they pressed Sign In
        if(v == mButtonSignIn) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }

        // if they pressed Register
        if(v == mButtonRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 0);
        }

        // if they pressed Verify
        if(v == mButtonVerify) {
            Intent intent = new Intent(this, VerifyActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_AUTH_SUCCESS) {
            runApp(true);
        }
        // TODO handle this
    }

    /**
     * Show or hide the progress circle.
     * @param show - display the progress circle or not
     */
    public void showProgress(final boolean show) {
        showView(mTextDisplay, !show);
        showView(mButtonSignIn, !show);
        showView(mButtonRegister, !show);
        showView(mButtonVerify, !show);
        showView(mProgressView, show);
    }

    private void showView(final View view, final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        view.setVisibility(show ? View.VISIBLE : View.GONE);
        view.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Attempt to continue on to the main activity. This will first check to make sure
     * our authentication is still valid then it will start the new activity and end
     * this one.
     */
    public void attemptLogin() {
        if(mAuthTask != null) {
            return;
        }

        mAuthTask = new CheckAuthTask();
        mAuthTask.execute();
    }

    /**
     * An AsyncTask which checks to see if we can log in using our stored credentials.
     */
    protected class CheckAuthTask extends AsyncTask<Void, Void, Boolean> {

        private final String LOG_TAG = getClass().getName();

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                SharedPreferences preferences = getSharedPreferences(
                        getString(R.string.shared_prefs_key), Context.MODE_PRIVATE);
                String token = preferences.getString(AUTH_TOKEN_KEY, "");

                RestHelper rest = new RestHelper(AccountMain.this);
                rest.setHeader("Accept", "application/json");
                rest.setHeader("Content-type", "application/json");
                rest.setHeader("Authenticate", "Token " + token);

                HttpURLConnection conn = rest.createGET(rest.resolve("/login/test/"));
                Log.i(LOG_TAG, "Default timeout = " + conn.getConnectTimeout());
                conn.setConnectTimeout(1000);
                conn.connect();
                return conn.getResponseCode() == 200;
            } catch (SocketException|SocketTimeoutException e) {
                // no need to do a stack trace for a network error
                Log.e(LOG_TAG, e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            mAuthTask = null;
            if(result == null) {
                // IO failed so continue on to app and assume we have no internet
                runApp(false);
            } else if(result) {
                // auth success! continue to the app with everything enabled
                runApp(true);
            } else {
                // auth failed, make them log in again
                showProgress(false);
            }
        }
    }

    public void runApp(boolean withInternet) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("internet", withInternet);
        startActivity(intent);
        finish();
    }
}
