package edu.rit.gis.doctoreducator.MedicalInfo;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.tip_of_day.TipOfTheDay;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class MedicalInfoFragment extends Fragment {

    CardView drugs, doctors, hospitals;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.medical_info, container, false);

        drugs = (CardView) rootView.findViewById(R.id.all_drugs);

        drugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create new fragment and transaction
                Fragment medicines_Fragment = new MedicinesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, medicines_Fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        doctors = (CardView) rootView.findViewById(R.id.all_doctors);

        doctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create new fragment and transaction
                Fragment doctors_Fragment = new DoctorsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, doctors_Fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        hospitals = (CardView) rootView.findViewById(R.id.all_hospitals);

        hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create new fragment and transaction
                Fragment hospital_Fragment = new HospitalsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.container, hospital_Fragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        return rootView;

    }
}
