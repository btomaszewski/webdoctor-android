package edu.rit.gis.doctoreducator.MedicalInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/23/15.
 */
public class DoctorsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.doctor_responses, container, false);

        return rootView;

    }
}
