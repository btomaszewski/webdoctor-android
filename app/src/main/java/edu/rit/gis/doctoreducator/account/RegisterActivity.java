package edu.rit.gis.doctoreducator.account;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.RestHelper;
import edu.rit.gis.doctoreducator.exception.RegistrationException;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final String LOG_TAG = "RegisterActivity";

    private UserRegisterTask mRegisterTask;

    private EditText mNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private Button mRegisterButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_register);

        // grab the views
        mNameView = (EditText) findViewById(R.id.name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordConfirmView = (EditText) findViewById(R.id.password2);
        mRegisterButton = (Button) findViewById(R.id.button_register);

        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mRegisterButton) {
            attemptRegister();
        }
    }

    private void attemptRegister() {
        String pass1 = mPasswordView.getText().toString();
        String pass2 = mPasswordConfirmView.getText().toString();
        Log.i(LOG_TAG, pass1);
        Log.i(LOG_TAG, pass2);
        if(pass1.equals(pass2)) {
            String name = mNameView.getText().toString();
            String email = mEmailView.getText().toString();
            mRegisterTask = new UserRegisterTask(name, email, pass1);
            mRegisterTask.execute();
        } else {
            mPasswordView.setError("Passwords do not match");
        }
    }


    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String LOG_TAG = UserRegisterTask.class.getName();

        private final String mName;
        private final String mEmail;
        private final String mPassword;
        private String mError;

        UserRegisterTask(String name, String email, String password) {
            mName = name;
            mEmail = email;
            mPassword = password;
            mError = getString(R.string.error_incorrect_password);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                AccountHelper helper = new AccountHelper(RegisterActivity.this);
                helper.register(mName, mEmail, mPassword);
                return true;
            } catch (IOException | JSONException | RegistrationException e) {
                // report exception and tell user we failed
                Log.e(LOG_TAG, e.getMessage(), e);
                mError = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
//            showProgress(false);

            if (success) {
                setResult(AccountMain.RESULT_AUTH_SUCCESS);
                finish();
            } else {
                mPasswordView.setError(mError);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
//            showProgress(false);
        }
    }
}
