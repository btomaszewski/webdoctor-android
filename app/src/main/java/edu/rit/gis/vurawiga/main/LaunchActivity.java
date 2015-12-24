package edu.rit.gis.vurawiga.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import edu.rit.gis.vurawiga.R;
import edu.rit.gis.vurawiga.account.AccountHelper;

/**
 * An activity which displays a splash screen for a little while and does stuff
 * in the background while the user is (presumably) looking at the splash screen
 */
public class LaunchActivity extends Activity {

    private static final String LOG_TAG = "LaunchActivity";

    /** Minimum time to display the LaunchActivity */
    private static final long WAIT_TIME = 1000 * 2;

    /**
     * The number of background tasks we are launching. If this number is
     * incorrect the activity will either finish early or it will never
     * finish at all so be careful when updating this.
     *
     * Current tasks: Wait task, Downloading assets, authentication check
     */
    private static final int NUMBER_OF_TASKS = 2; //3;

    /** Are we logged in already? */
    private boolean mIsLoggedIn = false;

    /**
     * Keeps track of the number of background tasks which still need to be
     * completed. When this reaches zero LaunchActivity should {@code finish()}
     * and start MainActivity.
     */
    private AtomicInteger mTaskCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

//        SharedPreferences score = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        SharedPreferences.Editor score_inc = score.edit();
//        int counter = score.getInt("counter",0);
//        float font_size = score.getFloat("font_size",20.0f);
//        if(counter == 0) {
//            //Put your function to copy files here
//            score_inc.putInt("counter", ++counter);
//            score_inc.commit();
//            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//        }

        // Set task count so afterTask() works
        mTaskCount = new AtomicInteger(NUMBER_OF_TASKS);

        // first start the wait thread which ensures the launcher is visible for
        // a minimum amount of time specified by WAIT_TIME
        WaitTask waitTask = new WaitTask();
        waitTask.start();

        // next start a background task to download assets
//        DownloadAllAssetsTask downloadTask = new DownloadAllAssetsTask();
//        downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        // finally start the task to see if we can authenticate with the server
        CheckAuthTask authTask = new CheckAuthTask();
        authTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     * Called after a background task is complete. ONLY call this from the UI thread.
     * This means calling from onPostExecute in most cases. This should be called
     * after each background task completes
     */
    private void afterTask() {
        if (mTaskCount.decrementAndGet() == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("internet", mIsLoggedIn);
            startActivity(intent);
            finish();
        }
    }

//    private class DownloadAllAssetsTask extends AsyncTask<Void, Boolean, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            makeToast(R.string.downloading_content).show();
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                AssetManager assetManager = AssetManager.createInstance(
//                        LaunchActivity.this);
//                assetManager.updateAllAssets();
//            } catch (IOException |JSONException e) {
//                // if something goes wrong we simply continue like we're done
//                // and just continue on with what we have
//                Log.e(LOG_TAG, e.getMessage(), e);
//                cancel(false);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onCancelled() {
//            makeToast(R.string.download_failed).show();
//            afterTask();
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            makeToast(R.string.download_success).show();
//            afterTask();
//        }
//    }

    /**
     * A thread which waits for {@code WAIT_TIME} milliseconds before calling
     * {@code afterTask()}. This is specifically a thread because if you put
     * it on an executor it might block other tasks from executing.
     */
    private class WaitTask extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                // log but ignore
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            // because afterTask needs to be called on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    afterTask();
                }
            });
        }
    }

    /**
     * Background which checks to see if we can log in using our stored credentials.
     */
    private class CheckAuthTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                AccountHelper helper = new AccountHelper(LaunchActivity.this);
                return helper.isAuthenticated(true);
            } catch (IOException e) {
                // catch IOException, log as a warning and return false
                Log.w(LOG_TAG, e.getMessage(), e);
                return false;
            } catch (Exception e) {
                // catch everything else, log it and return false
                Log.e(LOG_TAG, e.getMessage(), e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mIsLoggedIn = result;
            afterTask();
        }
    }

    private Toast makeToast(int id) {
        return Toast.makeText(this, id, Toast.LENGTH_SHORT);
    }
}
