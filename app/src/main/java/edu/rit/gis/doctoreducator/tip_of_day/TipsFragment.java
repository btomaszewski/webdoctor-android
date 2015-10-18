package edu.rit.gis.doctoreducator.tip_of_day;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by Siddesh on 9/23/2015.
 */
public class TipsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tips_fragment, container, false);

        CardView cardViewSurgery = (CardView) rootView.findViewById(R.id.around_you);

        cardViewSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsFragment.class);
                startActivity(intent);
            }
        });



        return rootView;
    }


}
