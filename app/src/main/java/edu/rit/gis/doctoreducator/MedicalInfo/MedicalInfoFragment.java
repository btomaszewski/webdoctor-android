package edu.rit.gis.doctoreducator.MedicalInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class MedicalInfoFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.medical_info, container, false);
        return rootView;
    }
}