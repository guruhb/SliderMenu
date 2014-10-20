package com.example.guruhb.smenu;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;


public class MyActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.v("MainActivity", "onNavigationDrawerItemSelected position : " + position );
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        Log.v("MainActivity", "onSectionAttached number : " + number );
        switch (number) {
            case 1:
                mTitle = getString(R.string.nav_pref_location_update_frequency);
                break;
            case 2:
                mTitle = getString(R.string.nav_pref_select_vehicle);
                break;
            case 3:
                mTitle = getString(R.string.nav_pref_mode);
                break;
        }
    }

    public void restoreActionBar() {
        Log.v("MainActivity", "restoreActionBar ");
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v("MainActivity", "onCreateOptionsMenu menu : ");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            Log.v("MainActivity", "isDrawerOpen not open");
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.my, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v("MainActivity", "onOptionsItemSelected itemId : " + item.getItemId());
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.setting_updatefrequency) {
            //showUpdateLocationPopUp();
            SettingsUpdateFrequency suf = new SettingsUpdateFrequency(MyActivity.this);
            suf.launchSettingsUpdateFrequency();
            return true;
        }

        if(id == R.id.setting_vehicle) {
            FragmentManager mgr = getFragmentManager();
            VehicleListSelectFragment dlg = new VehicleListSelectFragment();
            dlg.show(mgr, "VehicleSelectListFragment");
            return true;
        }
        if(id == R.id.setting_mode) {
            FragmentManager mgr = getFragmentManager();
            ModeSelectFragment dlg = new ModeSelectFragment();
            dlg.show(mgr, "ModeSelectFragment");
            return true;
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
            Log.v("MainActivity", "newInstance sectionNumber : " + sectionNumber);
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
            Log.v("MainActivity", "PlaceholderFragment cTor ");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            Log.v("MainActivity", "PlaceholderFragment onCreateView ");
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            SeekBar mySeek = (SeekBar)rootView.findViewById(R.id.location_update_frequency);
            if(mySeek != null) {
                mySeek.setMax(500);
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Log.v("MainActivity", "PlaceholderFragment onAttach ");
            ((MyActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private int mLocationUpdateFreq = 30;

    public void showUpdateLocationPopUp() {

        Dialog localFreqDialog = new Dialog(this);
        localFreqDialog.setContentView(R.layout.updateseekbar);
        localFreqDialog.setTitle("Location update ... ");
        SeekBar mySeek = (SeekBar)localFreqDialog.findViewById(R.id.location_update_frequency);
        if(mySeek != null) {
            //set seekbar observer
            Log.v("MyActivity", "Set seek bar observer");
            mySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mLocationUpdateFreq = progress;
                    Log.v("MyActivity", "Seek bar onProgressChanged");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Log.v("MyActivity", "Seek bar onStartTrackingTouch");
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Log.v("MyActivity", "Seek bar onStopTrackingTouch");
                }
            });

            /*
            onCancel is called first and then onDismiss
             */
            localFreqDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.v("MyActivity", "Location update dialog cancelled");
                }
            });

            localFreqDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Log.v("MyActivity", "Location OnDismissListener");
                }
            });

        }

        localFreqDialog.show();
    }
}