package com.sahelmastoureshgh.spotifystream;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopSongActivityFragment extends Fragment {
    ArrayList<Song> allSongTemp;
    CustomSongAdapter songAdapter;
    SpotifyApi api;
    SpotifyService spotifyService;
    FetchTrackTask trackTask;
    // when there is no album image but there is name and album name
    String withoutImage = "https://placeimg.com/80/80/nature";
    String artistName;


    public TopSongActivityFragment() {
        api = new SpotifyApi();
        spotifyService = api.getService();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSongTemp = new ArrayList<Song>();
        if (savedInstanceState != null) {
            allSongTemp = savedInstanceState.getParcelableArrayList("AllSongsConst");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The songAdapter will take data from a source and
        // use it to populate the ListView it's attached to.

        songAdapter = new CustomSongAdapter(
                getActivity(), // The current context (this activity)
                R.layout.list_item_song_track, // The name of the layout ID.
                R.id.list_item_song_textview, // The ID of the textview to populate.
                allSongTemp);

        /* Get an Intent instance and retrieve Artist/Singer Id from EXTRA_TEXT to get his track */
        Intent intent = getActivity().getIntent();
        Bundle args = getArguments();
        if (savedInstanceState==null) {
            trackTask = new FetchTrackTask();
            String singerId=null;
            if (args != null) {
                singerId = args.getString(TopSongActivity.EXTRA_ID);
                artistName = args.getString(TopSongActivity.EXTRA_NAME);
            }
            if(intent!=null && intent.getStringExtra(TopSongActivity.EXTRA_ID)!=null) {
                singerId = intent.getStringExtra(TopSongActivity.EXTRA_ID);
                artistName = intent.getStringExtra(TopSongActivity.EXTRA_NAME);
            }
            ActionBarActivity activity = (ActionBarActivity) getActivity();
            if(artistName!=null)
               activity.getSupportActionBar().setSubtitle(artistName);
            if(singerId!=null)
               trackTask.execute(singerId);

        }

        View rootView = inflater.inflate(R.layout.fragment_top_song, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.list_item_song_track);
        listView.setAdapter(songAdapter);

        //Add Listener when Song list Item has been clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the data item for this position and send the position
                Intent playerIntent = new Intent(getActivity(), PlayerActivity.class)
                        .putExtra(Intent.EXTRA_REFERRER, position)
                        .putExtra(Intent.EXTRA_RESTRICTIONS_LIST, allSongTemp)
                        .putExtra(Intent.EXTRA_REFERRER_NAME, artistName);
                startActivity(playerIntent);

            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("AllSongsConst", allSongTemp);
        super.onSaveInstanceState(outState);
    }


    /**
     * Fetch Track from Spotify by singer Id and save it to Song ArrayList
     */

    public class FetchTrackTask extends AsyncTask<String, Void, ArrayList<Song>> {

        private final String LOG_TAG = FetchTrackTask.class.getSimpleName();
        String singerSearch;

        @Override
        protected ArrayList<Song> doInBackground(String... params) {


            ArrayList<Song> allSongs = new ArrayList<Song>();
            HashMap<String, Object> options = new HashMap<String, Object>() {
                {
                    put("country", "US");
                }
            };
            //Get Track from Spotify Api and fill Song model with the results for tracks which have name and album
            //For empty images just put dummy image Still wanted to show songs which doesnt have album image
            try {
                Tracks sTracks = spotifyService.getArtistTopTrack(params[0], options);
                for (Track record : sTracks.tracks) {
                    if (record.name != null && record.album.name != null) {
                        allSongs.add(new Song(record.name, record.album.name, record.album.images.size() > 0 ? record.album.images.get(0).url : withoutImage, record.preview_url));
                    }

                }
                return allSongs;


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the  data, there's no point in attemping
                // to parse it.
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Song> songs) {

            if (songs != null) {
                allSongTemp = songs;
                songAdapter.clear();
                songAdapter.addAll(songs);

            }

        }
    }

}
