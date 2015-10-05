package edu.rit.gis.doctoreducator.CME;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class CMEFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_layout, container, false);
        return rootView;
    }
}
