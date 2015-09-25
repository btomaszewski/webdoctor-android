package edu.rit.gis.doctoreducator.tip_of_day;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by Siddesh on 9/23/2015.
 */
public class TipsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.card_layout, container, false);
        return rootView;
    }
}
