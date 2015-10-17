package edu.rit.gis.doctoreducator.tip_of_day;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by Siddesh on 9/23/2015.
 */
public class TipsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tips_fragment, container, false);


        return rootView;
    }


}
