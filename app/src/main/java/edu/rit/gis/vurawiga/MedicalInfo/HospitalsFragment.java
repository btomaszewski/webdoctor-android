package edu.rit.gis.vurawiga.MedicalInfo;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import edu.rit.gis.vurawiga.R;

/**
 * Created by siddeshpillai on 10/23/15.
 */
public class HospitalsFragment extends Fragment {

    ImageView locate, phoneCall;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.hospital_responses, container, false);

        locate = (ImageView) rootView.findViewById(R.id.navi);
        phoneCall = (ImageView) rootView.findViewById(R.id.call);

        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:5078844429"));
                startActivity(callIntent);
            }
        });

        return rootView;
    }
}
