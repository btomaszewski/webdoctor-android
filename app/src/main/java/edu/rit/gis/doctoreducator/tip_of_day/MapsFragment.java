package edu.rit.gis.doctoreducator.tip_of_day;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/17/15.
 */
public class MapsFragment extends Activity {

    private GoogleMap map;

    static final LatLng KIGALI = new LatLng(-1.9439, 30.0595);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(KIGALI, 10));

//        if (map!=null){
//            Marker kigali = map.addMarker(new MarkerOptions().position(KIGALI)
//                    .title("Hamburg"));
////            Marker kiel = map.addMarker(new MarkerOptions()
////                    .position(KIEL)
////                    .title("Kiel")
////                    .snippet("Kiel is cool")
////                    .icon(BitmapDescriptorFactory
////                            .fromResource(R.drawable.ic_launcher)));
//        }
    }

}