package edu.rit.gis.doctoreducator.search;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

/**
 * Poorly named interface for activities which want to allow other classes to easily
 * start fragments or activities such that the back button returns to this activity.
 */
public interface ITaskChanger {

    void addFragment(Fragment fragment);

    void startSubActivity(Intent intent);

    Activity getActivity();
}
