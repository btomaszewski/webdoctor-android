package edu.rit.gis.vurawiga.tip_of_day;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rit.gis.vurawiga.R;
import edu.rit.gis.vurawiga.discussion.DiscussionFragment;

/**
 * Created by Siddesh on 9/23/2015.
 */
public class HomeFragment extends Fragment {

    CardView tipOfDay, viewLatestDiscussions, videoTutorials, aroundYou, eventsPage, medscapeSearch, ncbiSearch;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tips_fragment, container, false);

        tipOfDay = (CardView) rootView.findViewById(R.id.tip_of_day);
        viewLatestDiscussions = (CardView) rootView.findViewById(R.id.view_latest_discussion);
        videoTutorials = (CardView) rootView.findViewById(R.id.video_tutorials);
        aroundYou = (CardView) rootView.findViewById(R.id.around_you);
        eventsPage = (CardView) rootView.findViewById(R.id.events_page);
        medscapeSearch = (CardView) rootView.findViewById(R.id.medi_search);
        ncbiSearch = (CardView) rootView.findViewById(R.id.article_search);

        tipOfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TipOfTheDay.class);
                startActivity(intent);
            }
        });

        viewLatestDiscussions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new DiscussionFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
//                Intent intent = new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
            }
        });

        videoTutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VideoTutorialsActivity.class);
                startActivity(intent);
            }
        });

        aroundYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });

        eventsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventsActivity.class);
                startActivity(intent);
            }
        });

        medscapeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Medsearch.class);
                startActivity(intent);
            }
        });

        ncbiSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NCBISearchActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


}
