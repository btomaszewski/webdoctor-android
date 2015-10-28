package edu.rit.gis.doctoreducator.about;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import edu.rit.gis.doctoreducator.R;

/**
 * Created by siddeshpillai on 10/5/15.
 */
public class AboutFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.about_layout, container, false);
        VideoView view = (VideoView)rootView.findViewById(R.id.offlineVideo);
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.vura_wiga;
        view.setVideoURI(Uri.parse(path));
        view.start();
        return rootView;
    }
}
