package edu.rit.gis.vurawiga.spreadsheet;


import android.app.Activity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

/**
 * Created by siddeshpillai on 9/25/15.
 */
public class SpreadSheetDataModel extends Activity{

    GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Drive.API)
            .addScope(Drive.SCOPE_FILE)
            .build();
}
