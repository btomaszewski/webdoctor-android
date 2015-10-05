package edu.rit.gis.doctoreducator.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;

import edu.rit.gis.doctoreducator.R;
import edu.rit.gis.doctoreducator.account.AccountHelper;
import edu.rit.gis.doctoreducator.account.LoginActivity;
import edu.rit.gis.doctoreducator.search.SearchActivity;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static String LOG_TAG = "MainActivity";

    /**
     * Activities should set this as their result to cause MainActivity
     * to update the Login/Logout menu item along with anything else relevant
     * to authentication.
     */
    public static final int RESULT_UPDATE_AUTHENTICATED = RESULT_FIRST_USER + 0x0CA0;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * We keep an instance of this around because we use it a lot.
     */
    private AccountHelper mAccountHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mAccountHelper = new AccountHelper(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = PlaceholderFragment.newInstance(position + 1);

                break;
            case 1:
                fragment = PlaceholderFragment.newInstance(position + 1);

                break;
            case 2:

                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            case 3:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            case 4:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            case 5:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            default:
                // ABORT!
                return;
        }

        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment).commit();


        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }


//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, nextFragment)
//                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = "CME";
            case 5:
                mTitle = "MOH Guidelines";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            // initialize the search widget
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            MenuItem miSearch = menu.findItem(R.id.action_search);
            ComponentName searchableName = new ComponentName(this, SearchActivity.class);
            SearchView searchView = (SearchView) miSearch.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(searchableName));
            searchView.setIconifiedByDefault(false);

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem miLogin = menu.findItem(R.id.action_login);
        MenuItem miLogout = menu.findItem(R.id.action_logout);
        if (miLogin != null) {
            boolean auth = mAccountHelper.isAuthenticated();
            miLogin.setVisible(!auth);
            miLogout.setVisible(auth);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.action_logout:
                mAccountHelper.logout();
                break;

            case R.id.action_search:
//            startActivity(new Intent(this, SearchActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    // Home
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
                    break;
                case 2:
                    // Medical Info
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
                    break;
                case 3:
                    // Discussion
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
                    break;
                case 4:
                    // CME
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
                    break;
                case 5:
                    // MOH Guidelines
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
                    break;
                case 6:
                    // About
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.card_layout, container, false);
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
