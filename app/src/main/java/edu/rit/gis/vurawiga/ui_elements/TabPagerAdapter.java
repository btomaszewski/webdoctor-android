package edu.rit.gis.vurawiga.ui_elements;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Siddesh on 9/23/2015.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
//                return new FragmentClassName();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 0; // no of tabs
    }
}
