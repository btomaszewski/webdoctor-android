package edu.rit.gis.vurawiga.discussion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;
import edu.rit.gis.vurawiga.R;

/**
 * Created by siddeshpillai on 10/25/15.
 */
public class AddDiscussionActivity extends Activity {

    Button createDiscussionButton;
    EditText discussionBody, discussionTitle, discussionUser;
    String user;
    String title;
    String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_discussion);

        discussionTitle = (EditText) findViewById(R.id.discussion_title);
        discussionBody = (EditText) findViewById(R.id.discussion_body);
        discussionUser = (EditText) findViewById(R.id.discussion_user);

        createDiscussionButton = (Button) findViewById(R.id.create_discussion_button);

        final ProgressDialog dialog = new ProgressDialog(AddDiscussionActivity.this);

        createDiscussionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.setMessage("Creating a new discussion thread...");
                dialog.show();

                // input validation
                if (inputValidator()) {
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    params.put("author", user);
                    params.put("title", title);
                    params.put("body", body);


                    client.post("http://webdoctor-discussions.rhcloud.com/newThread", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            dialog.dismiss();
                            onBackPressed();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    });
                }

                dialog.dismiss();
            }
        });
    }

    public boolean inputValidator() {
        boolean flag = false;
        boolean case1 = true, case2 = true, case3 = true;

        user = discussionUser.getText().toString();
        title = discussionTitle.getText().toString();
        body = discussionBody.getText().toString();

        if (TextUtils.isEmpty(user)) {
            discussionUser.setError("This field cannot be empty");
            case1 = false;
        }

        if (TextUtils.isEmpty(title)) {
            discussionTitle.setError("This field cannot be empty");
            case2 = false;
        }

        if (TextUtils.isEmpty(body)) {
            discussionBody.setError(getString(R.string.error_invalid_password));
            case3 = false;
        }

        if(case1 && case2 && case3) flag = true;

        return flag;
    }
}
