package edu.rit.gis.doctoreducator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Assists in creating Activities which can be started and when they finish
 * will start another Activity.
 *
 * To use this mixin. Simply put an extra called "next" with the full
 * class name for the Activity you're launching. Once that activity is done
 * it will create the Activity from the class specified in "next". If you want
 * to include extra data for that Activity put it in a Bundle and put the Bundle
 * as an extra called "nextIntent".
 *
 * {@literal
 * - next = full class name
 * - nextIntent = Bundle containing extras for "next" Activity
 *   - data = Uri to call setData with
 * }
 */
public class PassThroughMixin {

    /**
     * Reference to the activity which will be using the mixin
     */
    private Activity mSelf;

    public PassThroughMixin(Activity self) {
        mSelf = self;
    }

    /**
     * Call this when your activity is finishing.
     */
    public void finish() throws ClassNotFoundException {
        if (mSelf.getIntent().hasExtra("next")) {
            Class nextClazz = Class.forName(mSelf.getIntent().getStringExtra("next"));

            Bundle nextIntent = mSelf.getIntent().getBundleExtra("nextIntent");

            Uri nextData = null;
            if (nextIntent != null) {
                nextData = nextIntent.getParcelable("data");
                nextIntent.remove("data");
            }
            Intent intent = new Intent(mSelf, nextClazz);
            intent.setData(nextData);
            intent.putExtras(nextIntent);
            mSelf.startActivity(intent);
        }
    }
}
