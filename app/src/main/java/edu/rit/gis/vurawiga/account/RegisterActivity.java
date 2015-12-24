package edu.rit.gis.vurawiga.account;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

import edu.rit.gis.vurawiga.R;
import edu.rit.gis.vurawiga.exception.RegistrationException;

/**
 * Activity for registering new users. The Activity handles two extras from the Intent:
 * {@code email} and {@code password}. These will be used to fill in the email and password
 * fields respectively if they are supplied however they are optional. The user can still
 * change the email and password, these extras just supply default values.
 */
public class RegisterActivity extends Activity implements View.OnClickListener {

    private static final String LOG_TAG = "RegisterActivity";

    public static final String EXTRA_EMAIL = "email";
    public static final String EXTRA_PASSWORD = "password";

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

        if (getIntent().hasExtra(EXTRA_EMAIL)) {
            mEmailView.setText(getIntent().getStringExtra(EXTRA_EMAIL));
        }
        if (getIntent().hasExtra(EXTRA_PASSWORD)) {
            mEmailView.setText(getIntent().getStringExtra(EXTRA_PASSWORD));
        }
    }

    @Override
    public void onClick(View v) {
        if(v == mRegisterButton) {
            attemptRegister();
        }
    }

    /**
     * Check the fields and see if we are good to go. If we are then try to
     * register the new account.
     */
    private void attemptRegister() {
        if (mRegisterTask != null) {
            return;
        }

        // Reset errors
        mNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);

        boolean hasError = false;
        String password = mPasswordView.getText().toString();
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();

        // Check for required fields
        // |= is or-equals: like plus-equals (+=) but with or (||)
        hasError |= checkEmpty(mNameView);
        hasError |= checkEmpty(mEmailView);
        hasError |= checkEmpty(mPasswordView);
        hasError |= checkEmpty(mPasswordConfirmView);

        // Check if passwords match
        if (!TextUtils.equals(password, mPasswordConfirmView.getText())) {
            mPasswordConfirmView.setError(getString(R.string.error_passwords_do_not_match));
            hasError = false;
        }

        // if there was no error then start the task
        if (!hasError) {
            mRegisterTask = new UserRegisterTask(name, email, password);
            mRegisterTask.execute();
        }
    }

    /**
     * Check if {@code view} is empty and if it is set it's error to error_field_required.
     *
     * @param view - the EditText to check
     * @return true if the field is empty, false otherwise
     */
    private boolean checkEmpty(EditText view) {
        if (TextUtils.isEmpty(view.getText())) {
            view.setError(getString(R.string.error_field_required));
            return true;
        } else {
            return false;
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
            if (success) {
                // setResult(AccountMain.RESULT_AUTH_SUCCESS);
                finish();
            } else {
                mPasswordView.setError(mError);
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
        }
    }
}
