package edu.rit.gis.doctoreducator.account;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import edu.rit.gis.doctoreducator.AssetManager;
import edu.rit.gis.doctoreducator.PassThroughMixin;
import edu.rit.gis.doctoreducator.R;

/**
 * Downloads assets from the server.
 * These assets correspond to the content module from the server.
 *
 * If this activity is started with an extra called "next" then
 * it will parse that as a class and start that class as an activity
 * after completing the download.
 */
public class DownloadAssetsActivity extends Activity {

    private static final String LOG_TAG = "DownloadAssetsActivity";

    private DownloadAllAssetsTask mTask;
    private PassThroughMixin mPassThrough = new PassThroughMixin(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_assets);

        mTask = new DownloadAllAssetsTask();
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void afterTask() {
        try {
            mPassThrough.finish();
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        finish();
    }

    protected class DownloadAllAssetsTask extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                AssetManager assetManager = AssetManager.createInstance(
                        DownloadAssetsActivity.this);
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
            afterTask();
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(DownloadAssetsActivity.this, R.string.downloading_content,
                    Toast.LENGTH_LONG).show();
            afterTask();
        }
    }
}
