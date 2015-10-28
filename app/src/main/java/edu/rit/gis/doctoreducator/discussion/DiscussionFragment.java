package edu.rit.gis.doctoreducator.discussion;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import edu.rit.gis.doctoreducator.R;

/**
 * Created by Siddesh on 9/23/2015.
 */
public class DiscussionFragment extends Fragment {

    ListView listView;
    ImageButton addNewDiscussion;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.all_discussions, container, false);

        listView = (ListView) rootView.findViewById(R.id.viewAllRulesView);
        addNewDiscussion = (ImageButton) rootView.findViewById(R.id.add_button);

        addNewDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(),
                        AddDiscussionActivity.class);
                startActivity(myIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Display_Rows();

    }

    private void Display_Rows() {

        final List<DiscussionThread> listofThreads = new ArrayList<DiscussionThread>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching all the discussion threads...");
        dialog.show();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://webdoctor-discussions.rhcloud.com/getAllthreads", new JsonHttpResponseHandler() {

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

                    List<String> lst = new ArrayList<>();

                    for (int i = 0; i < listofThreads.size(); i++) {
                        lst.add(listofThreads.get(i).title);
                    }

                    ArrayAdapter<String> ArAd = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, lst);

                    listView.setAdapter(ArAd);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });


        dialog.dismiss();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), OpenThread.class);
                intent.putExtra("title", listofThreads.get(i).title);
                intent.putExtra("author", listofThreads.get(i).author);
                intent.putExtra("body", listofThreads.get(i).body);
                intent.putExtra("id", listofThreads.get(i).id);
                startActivity(intent);

            }
        });

    }

}
