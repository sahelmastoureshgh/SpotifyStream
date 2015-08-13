package com.sahelmastoureshgh.spotifystream;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements SingerFragment.Callback{
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, new TopSongActivityFragment(), DETAILFRAGMENT_TAG)
                .commit();
            }
            } else {
                mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String name, String Id) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putString(TopSongActivity.EXTRA_ID, Id);
            bundle.putString(TopSongActivity.EXTRA_NAME, name);
            TopSongActivityFragment fragment = new TopSongActivityFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(
                    R.id.detail_container, fragment).commit();
        } else {
            Intent songIntent = new Intent(this, TopSongActivity.class)
             .putExtra(TopSongActivity.EXTRA_ID, Id)
             .putExtra(TopSongActivity.EXTRA_NAME, name);
            startActivity(songIntent);
        }

    }
}
