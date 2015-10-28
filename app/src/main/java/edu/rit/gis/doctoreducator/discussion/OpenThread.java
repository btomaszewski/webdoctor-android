package edu.rit.gis.doctoreducator.discussion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/27/15.
 */
public class OpenThread extends Activity {

    TextView title, body, author;
    EditText comment;
    Button button;
    ListView listView;

    String intentAuthor, intentTitle, intentBody, intentID;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_discussion_thread);

        Bundle extras = getIntent().getExtras();

        intentTitle = extras.get("title").toString();
        intentAuthor = extras.get("author").toString();
        intentBody = extras.get("body").toString();
        intentID = extras.get("id").toString();

        title = (TextView) findViewById(R.id.threadTitle);
        body = (TextView) findViewById(R.id.threadBody);
        author = (TextView) findViewById(R.id.threadAuthor);

        title.setText(intentTitle);
        body.setText(intentBody);
        author.setText(intentAuthor);


        comment = (EditText) findViewById(R.id.makeComment);
        button = (Button) findViewById(R.id.comment);

        final ProgressDialog dialog = new ProgressDialog(OpenThread.this);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("Adding your comment to the thread...");
                dialog.show();

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("author", "anonymous");
                params.put("title", intentTitle);
                params.put("body", comment.getText().toString());
                params.put("id",intentID);


                client.post("http://webdoctor-discussions.rhcloud.com/newComment", params, new AsyncHttpResponseHandler() {
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
        });

        listView = (ListView) findViewById(R.id.viewAllComment);

        Display_Rows();

    }

    private void Display_Rows() {

        final List<DiscussionThread> listofThreads = new ArrayList<DiscussionThread>();
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching all the discussion threads...");
        dialog.show();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://webdoctor-discussions.rhcloud.com/getComments/"+intentID, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                try {
                    for (int i = 0; i < timeline.length(); i++) {
                        JSONObject jsonObject = (JSONObject) timeline.get(i);

                        listofThreads.add(new DiscussionThread(jsonObject.getString("_id"),
                                jsonObject.getString("id"), jsonObject.getString("author"),
                                jsonObject.getString("title"), jsonObject.getString("body"),
                                jsonObject.getString("time")));
                    }

                    final List<String> colorList = new LinkedList<String>();

                    for (int i = 0; i < listofThreads.size(); i++) {
                        colorList.add(listofThreads.get(i).body);
                    }

                    ArrayAdapter<String> ArAd = new ArrayAdapter<String>(OpenThread.this,
                            android.R.layout.simple_list_item_1, colorList);

                    listView.setAdapter(ArAd);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.dismiss();

    }
}
