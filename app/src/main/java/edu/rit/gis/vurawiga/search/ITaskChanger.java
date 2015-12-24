package edu.rit.gis.vurawiga.search;


import android.app.Activity;

/**
 * Poorly named interface for activities which want to allow other classes to easily
 * start fragments or activities such that the back button returns to this activity.
 */
public interface ITaskChanger {

    Activity getActivity();
}
