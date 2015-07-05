package edu.rit.gis.doctoreducator.account;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import edu.rit.gis.doctoreducator.AssetManager;
import edu.rit.gis.doctoreducator.IOUtil;
import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.RestHelper;
import edu.rit.gis.doctoreducator.WrappedException;

public class DownloadAssetsActivity extends Activity {

    private static final String LOG_TAG = DownloadAssetsActivity.class.getName();
    private static final String LIST_URL = "content/list?latest=true";

    private AssetManager mAssetManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_assets);

        // Attempt to create the asset manager and if we fail log it
        // and create it without reading from the asset file.
        // If that fails we're done. Give up.
        try {
            mAssetManager = new AssetManager(this);
        } catch (IOException | JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            try {
                mAssetManager = new AssetManager(this, false);
            } catch (IOException | JSONException e2) {
                // should NEVER happen so fail
                throw new WrappedException(e2);
            }
        }
    }

    protected class DownloadAllAssetsTask extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                RestHelper rest = new RestHelper(getString(R.string.url_base));

                // first we grab the list of the latest content
                JSONArray newFilesArray = new JSONArray(rest.sendGET(
                        rest.resolve(LIST_URL)));

                // now we try to download each of them
                for (int i = 0; i < newFilesArray.length(); i++) {
                    JSONObject nextFile = newFilesArray.getJSONObject(i);
                    if (mAssetManager.isNewVersion(nextFile)) {
                        // download the file
                        File output = mAssetManager.determineFile(nextFile);
                        IOUtil.downloadFile(new URL(nextFile.getString("file")), output);

                        // file is downloaded so update the data
                        mAssetManager.updateAsset(nextFile);
                    }
                }
            } catch (IOException|JSONException e) {
                // if something goes wrong we simply continue like we're done
                // and just continue on with what we have
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                try {
                    // either way try to save the updated records
                    mAssetManager.save();
                } catch (IOException | JSONException e2) {
                    // not imperative we save
                    Log.e(LOG_TAG, e2.getMessage(), e2);
                }
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(DownloadAssetsActivity.this, R.string.downloading_failed,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(DownloadAssetsActivity.this, R.string.downloading_content,
                    Toast.LENGTH_LONG).show();
        }
    }
}
