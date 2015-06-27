package com.sahelmastoureshgh.spotifystream;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Encapsulates fetching the Singer and displaying it as a ListView layout
 */
public class SingerFragment extends Fragment {
    ArrayList<Singer> allSingersTemp;
    CustomSingersAdapter singerAdapter;
    SpotifyApi api;
    SpotifyService spotifyService;
    String singerSearch;


    public SingerFragment() {
        api = new SpotifyApi();
        spotifyService = api.getService();



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSingersTemp = new ArrayList<Singer>();
        if (savedInstanceState != null) {
            allSingersTemp = savedInstanceState.getParcelableArrayList("AllSinger");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The singerAdapter will take data from a source and
        // use it to populate the ListView it's attached to.

        singerAdapter = new CustomSingersAdapter(
                getActivity(), // The current context (this activity)
                R.layout.list_item_singer, // The name of the layout ID.
                R.id.list_item_singer_textview, // The ID of the textview to populate.
                allSingersTemp);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_singer);
        listView.setAdapter(singerAdapter);

        //Add Listener when Singer list Item has been clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the data item for this position
                Singer clickedSinger = singerAdapter.getItem(position);
                Intent songIntent = new Intent(getActivity(), TopSongActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, clickedSinger.id)
                        .putExtra(Intent.EXTRA_REFERRER_NAME, clickedSinger.name);
                startActivity(songIntent);

            }
        });

        //Listener on Search Text
        final EditText searchSinger = (EditText) rootView.findViewById(R.id.search_text_view_artist);
        searchSinger.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (s.toString().length() > 0) {
                    FetchSingerTask singerTask = new FetchSingerTask();
                    singerTask.execute(s.toString());
                } else {
                    /* Empty string case remove arrayList items */
                    singerAdapter.clear();

                }


            }
        });


        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /* Save state of allISingers arrayList */
        outState.putParcelableArrayList("AllSinger", allSingersTemp);

        super.onSaveInstanceState(outState);
    }


    /**
     * Fetch Artist (Singer) from Spotify and save it to Singer ArrayList
     */

    public class FetchSingerTask extends AsyncTask<String, Void, ArrayList<Singer>> {

        private final String LOG_TAG = FetchSingerTask.class.getSimpleName();


        @Override
        protected ArrayList<Singer> doInBackground(String... params) {
            singerSearch = params[0];
            //Get Artist from Spotify Api and fill Singer model with the results for artist who have images and name
            try {
                ArrayList<Singer>  allSingers = new ArrayList<>();
                ArtistsPager results = spotifyService.searchArtists(singerSearch);
                for (kaaes.spotify.webapi.android.models.Artist artist : results.artists.items) {
                    if (artist.name != null && artist.images.size() > 0) {
                        allSingers.add(new Singer(artist.name, artist.images.get(0).url, artist.id));
                    }

                }
                return allSingers;


            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the  data, there's no point in attemping
                // to parse it.
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Singer> singers) {
            singerAdapter.clear();
            if (singers != null) {
                allSingersTemp = singers;
                //If there is no singer found
                if(singers.size()==0){
                    Context context = getActivity();
                    if(context != null) {
                        Toast.makeText(context, "Could not Find this artist, please refine your search", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    singerAdapter.addAll(singers);
                }


            }

        }
    }


}
