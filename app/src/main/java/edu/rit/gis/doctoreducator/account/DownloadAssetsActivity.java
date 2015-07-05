package edu.rit.gis.doctoreducator.account;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import edu.rit.gis.doctoreducator.AssetManager;
import edu.rit.gis.doctoreducator.R;

public class DownloadAssetsActivity extends Activity {

    private static final String LOG_TAG = DownloadAssetsActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_assets);


    }

    protected class DownloadAllAssetsTask extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            AssetManager assetManager = null;
            // Attempt to create the asset manager and if we fail log it
            // and create it without reading from the asset file.
            // If that fails we're done. Give up.
            try {
                assetManager = new AssetManager(DownloadAssetsActivity.this);
            } catch (IOException | JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                try {
                    assetManager = new AssetManager(DownloadAssetsActivity.this, false);
                } catch (IOException | JSONException e2) {
                    // should NEVER happen so fail
                    throw new RuntimeException(e2);
                }
            }
            try {
                assetManager.updateAllAssets();
            } catch (IOException|JSONException e) {
                // if something goes wrong we simply continue like we're done
                // and just continue on with what we have
                Log.e(LOG_TAG, e.getMessage(), e);
                cancel(false);
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
